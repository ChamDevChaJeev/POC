package au.com.woolworths.mpos.callbellpoc.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.woolworths.sie.lib.workspaceoneclient.AirwatchSDKClientManager
import com.woolworths.sie.lib.workspaceoneclient.BuildConfig
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

object EnvironmentService {
    private const val PREFERENCES_SERIAL_KEY = "serial_key"
    const val PREFERENCES_FILE_KEY = "file_key"
    fun getSerialNumber(context: Context?, sharedPref: SharedPreferences): String? {
        val _serialNumber = AtomicReference<String?>()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            _serialNumber.set(Build.SERIAL)
        } else {
            val serialFromPreferences = sharedPref.getString(PREFERENCES_SERIAL_KEY, null)
            if (serialFromPreferences == null) {
                Log.i(
                    "EnvironmentService",
                    "serialFromPreferences is null, about to fetch from airwatch sdk"
                )
                val countDownLatch = CountDownLatch(1)

                // next try and retrieve from the sdk
                AirwatchSDKClientManager.getSerialNumber(context) { serialNumberFromSDK: String? ->
                    Log.i(
                        "EnvironmentService",
                        "AirwatchClientSDK. serialNumber=$serialNumberFromSDK"
                    )
                    if (serialNumberFromSDK == null && BuildConfig.DEBUG) {
                        // For testing purposes only
                        // serialNumberFromSDK = "20021521401407";
                    }
                    if (!serialNumberFromSDK.isNullOrEmpty()) {
                        Log.i(
                            "EnvironmentService",
                            "Build.SERIAL... Save serial number to preferences=$serialNumberFromSDK"
                        )
                        // We have a serialNumber, lets save to preferences
                        val editor = sharedPref.edit()
                        editor.putString(PREFERENCES_SERIAL_KEY, serialNumberFromSDK)
                        editor.apply()
                        _serialNumber.set(serialNumberFromSDK)
                    } else {
                        Log.i(
                            "EnvironmentService",
                            "serialNumberFromSDK was unable to be fetched from Airwatch SDK"
                        )
                    }
                    countDownLatch.countDown()
                }
                try {
                    Log.i("EnvironmentService", "About to wait for latch")
                    countDownLatch.await(
                        10L,
                        TimeUnit.SECONDS
                    ) // join thread with timeout of second
                    if (_serialNumber.get() == null) {
                        // For testing purposes only
                        // _serialNumber.set("20021521401407");

                        // We have a serialNumber, lets save to preferences
                        val editor = sharedPref.edit()
                        editor.putString(PREFERENCES_SERIAL_KEY, _serialNumber.get())
                        editor.apply()
                    }
                    Log.i(
                        "EnvironmentService",
                        "Woken up from latch. serial=" + _serialNumber.get()
                    )
                } catch (e: InterruptedException) {
                    Log.e("EnvironmentService", "Error counting down latch for serial number", e)
                }
            } else {
                _serialNumber.set(serialFromPreferences)
            }
        }
        return _serialNumber.get()
    }
}