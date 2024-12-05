package au.com.woolworths.store.callbell.network

import au.com.woolworths.mpos.callbellpoc.network.model.CallRequest
import au.com.woolworths.mpos.callbellpoc.network.model.CallResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
const val auth = "DGn5GKJhjA767s91Y9x5K2qaXOcAKMNBKxxy||OxNYaYAzFuevBcA=="
interface CallBellApi {
    @POST("/api/button")
    suspend fun clickRequest(
        @Body request: CallRequest,
        @Header("x-functions-key") authentication: String = auth,
        @Header("Content-Type") contentType:String ="application/json" ): Response<CallResponse>
}
