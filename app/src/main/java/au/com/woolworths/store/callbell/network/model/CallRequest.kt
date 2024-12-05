package au.com.woolworths.mpos.callbellpoc.network.model

import com.google.gson.annotations.SerializedName

data class CallRequest(
    @SerializedName("serialNumber") val serialNumber :String,
    @SerializedName("click") val click :String,
    @SerializedName("ip") val ip :String
)
