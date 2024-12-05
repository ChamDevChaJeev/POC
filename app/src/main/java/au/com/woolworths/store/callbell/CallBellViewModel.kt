package au.com.woolworths.store.callbell

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.woolworths.store.callbell.network.CallBellService
import au.com.woolworths.mpos.callbellpoc.network.model.CallRequest
import au.com.woolworths.mpos.callbellpoc.network.model.CallResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CallBellViewModel:ViewModel() {
    private val _callBellAction = MutableLiveData<CallBellAction>()
    val callBellAction :LiveData<CallBellAction> = _callBellAction

    fun messageStores(ipAddress: String, serialNumber: String="000") {
        viewModelScope.launch {
            try {
                val result = CallBellService.callBellApiService.clickRequest(request = CallRequest(serialNumber,"single",ipAddress))
                Log.d("devs","$result")
                if (result.isSuccessful){
                    Log.d("devs","${result.body()}")
                    _callBellAction.postValue(CallBellAction.CallSucceedWithResponse(result.body()))

                }else{
                    Log.d("devs"," Not Successful ${result.body()}")
                    _callBellAction.postValue(CallBellAction.CallFailed("Something went wrong"))
                }
            }catch (e:Exception){
                _callBellAction.postValue(CallBellAction.CallFailed("Something went wrong"))

            }

        }
    }

    fun startDelay() {
        GlobalScope.launch { delay(10000)
            _callBellAction.postValue(CallBellAction.DelayEnd)
        }

    }

}

sealed class CallBellAction{
    object ProcessDeviceData: CallBellAction()
    class CallSucceedWithResponse(val body: CallResponse?) : CallBellAction()
    class CallFailed(val message:String): CallBellAction()
    object DelayEnd: CallBellAction()
}