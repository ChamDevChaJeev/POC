package au.com.woolworths.store.callbell

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import au.com.woolworths.mpos.callbellpoc.utils.EnvironmentService
import au.com.woolworths.store.callbell.utils.NetworkUtils

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CallBellViewModel
    private lateinit var  mainBackground:ConstraintLayout
    private lateinit var imageButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[CallBellViewModel::class.java]
        mainBackground =findViewById(R.id.main_background)
        imageButton = findViewById(R.id.bell_button)
        val sharedPreference =  getSharedPreferences(EnvironmentService.PREFERENCES_FILE_KEY,Context.MODE_PRIVATE)
        var serialNumber: String? = null
        try {
            serialNumber = EnvironmentService.getSerialNumber(this, sharedPreference)
        }catch (e:Exception){
            Log.d("devs","could not fetch serial number... $e")
        }

        imageButton.setOnClickListener(View.OnClickListener {
            var ipAddress: String? =null
            imageButton.isEnabled = false
            try {
                ipAddress = NetworkUtils.getIPAddress(this)
            }catch (e:Exception){
                Log.d("devs","could not fetch IP... $e")
            }

            if (ipAddress.isNullOrEmpty()) ipAddress = "0.0.0.0"
            Toast.makeText(this, "ip= $ipAddress, serialNumber= $serialNumber", Toast.LENGTH_LONG).show()
            viewModel.messageStores(ipAddress , serialNumber?:"000")
        })

        viewModel.callBellAction.observe(this , Observer {
            when(val action = it){
                is CallBellAction.CallSucceedWithResponse->{
                    Log.d("devs","CallSucceedWithResponse")
                    imageButton.background = ContextCompat.getDrawable(this, R.drawable.state2button)
                    mainBackground.background = ContextCompat.getDrawable(this, R.drawable.state2bg)
                    imageButton.isEnabled = false
                    Toast.makeText(this, "${action.body}", Toast.LENGTH_LONG).show()
                    viewModel.startDelay()

                }
                is CallBellAction.CallFailed -> {
                    Log.d("devs","CallFailed")
                    Toast.makeText(this, "${action.message}", Toast.LENGTH_LONG).show()
                    imageButton.isEnabled = true

                }
                is CallBellAction.DelayEnd ->{
                    mainBackground.background = ContextCompat.getDrawable(this, R.drawable.state1bg)
                    imageButton.background = ContextCompat.getDrawable(this, R.drawable.state1button)
                    imageButton.isEnabled = true

                }

                else -> {}
            }
        })
    }
}