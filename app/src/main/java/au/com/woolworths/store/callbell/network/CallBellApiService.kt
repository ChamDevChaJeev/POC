package au.com.woolworths.store.callbell.network


import au.com.woolworths.store.callbell.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val interceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}
val client = OkHttpClient.Builder().apply {
    this.addInterceptor(interceptor)
        // time out setting
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(20,TimeUnit.SECONDS)
        .writeTimeout(25,TimeUnit.SECONDS)

}.build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BuildConfig.BASE_URL)
    .client(client)
    .build()

object CallBellService{
    val callBellApiService: CallBellApi =
        retrofit.create(CallBellApi::class.java)
}