package au.com.woolworths.mpos.callbellpoc.network.model

import com.google.gson.annotations.SerializedName

data class CallResponse(
    @SerializedName("sentOK")
    val sentOK: Boolean,
    @SerializedName("message")
    val message: String
)