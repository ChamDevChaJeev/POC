package au.com.woolworths.store.callbell.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import java.net.NetworkInterface

object NetworkUtils {
    fun getIPAddress(context: Context): String? {
        // Get the IP address using WifiManager
        val wifiIpAddress = getWifiIPAddress(context)
        if (wifiIpAddress != null) {
            return wifiIpAddress
        }

        // If WifiManager didn't provide the IP, try another method
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address.address.size == 4) {
                        // Check if it's an IPv4 address
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("NetworkUtils", "Error getting IP address", e)
        }
        return null
    }

    private fun getWifiIPAddress(context: Context): String? {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager != null) {
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            return formatIpAddress(ipAddress)
        }
        return null
    }

    private fun formatIpAddress(ipAddress: Int): String {
        return String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )
    }
}