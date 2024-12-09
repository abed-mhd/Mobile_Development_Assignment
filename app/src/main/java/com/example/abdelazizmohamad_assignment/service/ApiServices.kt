package service

import com.example.abdelazizmohamad_assignment.service.RoomApiService
import com.example.abdelazizmohamad_assignment.service.WindowApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.Credentials
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import service.ApiServices.API_PASSWORD
import service.ApiServices.API_USERNAME
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object ApiServices {
    const val API_USERNAME = "user"
    const val API_PASSWORD = "password"
    private const val BASE_URL = "http://automacorp.devmind.cleverapps.io/api/"

    val roomsApiService: RoomApiService by lazy {
        val client = getUnsafeOkHttpClient()
            .addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
            .build()

        Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(RoomApiService::class.java)
    }

    val windowsApiService: WindowApiService by lazy {
        val client = getUnsafeOkHttpClient()
            .addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
            .build()

        Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(WindowApiService::class.java)
    }

}

class BasicAuthInterceptor(private val username: String, private val password: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", Credentials.basic(username, password))
            .build()
        return chain.proceed(request)
    }
}
private fun getUnsafeOkHttpClient(): OkHttpClient.Builder =
    OkHttpClient.Builder().apply {
        val trustManager = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        val sslContext = SSLContext.getInstance("SSL").also {
            it.init(null, arrayOf(trustManager), SecureRandom())
        }
        sslSocketFactory(sslContext.socketFactory, trustManager)
        hostnameVerifier { hostname, _ -> hostname.contains("cleverapps.io") }
        addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
    }
