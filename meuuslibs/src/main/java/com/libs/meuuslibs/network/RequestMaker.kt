package com.libs.meuuslibs.network

import android.os.Looper
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Modifier
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class RequestMaker(private var networkSetting: NetworkInterface) {
    private var okHttpClient: OkHttpClient? = null
    private var dispatcher: Dispatcher? = null
    private var builder: Retrofit.Builder? = null

    private val protocols: ArrayList<Protocol>
        get() {
            val protocols = ArrayList<Protocol>()
            protocols.add(Protocol.HTTP_2)
            protocols.add(Protocol.HTTP_1_1)
            return protocols
        }

    private val gson: Gson
        get() = GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .create()

    private fun getHttpClient(): OkHttpClient? {
        if (okHttpClient == null) {
            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
            httpClientBuilder.readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)
            httpClientBuilder.writeTimeout(TIMEOUT_WRITE.toLong(), TimeUnit.SECONDS)
            httpClientBuilder.protocols(protocols)

            if (networkSetting.setPrintLog()) {
                httpClientBuilder.addInterceptor(MockInterceptor(networkSetting))
                httpClientBuilder.addNetworkInterceptor(StethoInterceptor())
                val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        if (isJSONValid(message))
                            Logger.json(message)
                        else
                            Log.d(PRETTY_LOGGER, message)
                    }

                    fun isJSONValid(jsonInString: String): Boolean {
                        try {
                            JSONObject(jsonInString)
                        } catch (ex: JSONException) {
                            try {
                                JSONArray(jsonInString)
                            } catch (ex1: JSONException) {
                                return false
                            }
                        }
                        return true
                    }

                })
                logging.level = HttpLoggingInterceptor.Level.BODY
                httpClientBuilder.addInterceptor(logging)
            }

            dispatcher = Dispatcher()
            httpClientBuilder.dispatcher(dispatcher!!)
            httpClientBuilder.addInterceptor { convertInterceptToResponse(it) }
            httpClientBuilder.addInterceptor(ReceivedCookiesInterceptor(networkSetting))
            okHttpClient = httpClientBuilder.build()
        }
        return okHttpClient
    }

    private fun threadName(): String {
        return if (Looper.myLooper() == Looper.getMainLooper()) "(MAIN THREAD)" else "(BACKGROUND THREAD)"
    }

    private fun headerBuilder(request: Request): Request {
        val builder = networkSetting.setHeaderBuilder(request.newBuilder())
        builder.method(request.method(), request.body())

        return builder.build()
    }

    @Throws(IOException::class)
    fun convertInterceptToResponse(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val responseOri = chain.proceed(headerBuilder(original))
        val responseBodyOri = responseOri.body()
        val bodyStrOri = responseBodyOri!!.string()

        Log.e("addNetworkInterceptor", threadName() + " " + "**http-status:${responseOri.code()}")
        Log.e("addNetworkInterceptor", threadName() + " " + "**http-body:$bodyStrOri")

        if (responseOri.code() != HttpURLConnection.HTTP_OK)
            networkSetting.setErrorHandler(bodyStrOri)


        return getResponse(responseOri, responseBodyOri, bodyStrOri)
    }

    fun clearAllCalls() {
        val client = getHttpClient()
        if (client != null) {
            Log.e("PRETTY", "queuedCallsCount: " + client.dispatcher().queuedCallsCount())
            Log.e("PRETTY", "runningCallsCount: " + client.dispatcher().runningCallsCount())

            for (call in client.dispatcher().queuedCalls()) {
                call.cancel()
            }
            for (call in client.dispatcher().runningCalls()) {
                call.cancel()
            }
        }
    }

    private fun getResponse(response: Response, body: ResponseBody, bodyStr: String): Response {
        val builder = response.newBuilder()
        return builder
                .body(ResponseBody.create(body.contentType(), bodyStr.toByteArray()))
                .build()
    }

    private fun getRetrofitBuilder(): Retrofit.Builder? {
        if (builder == null)
            builder = Retrofit.Builder()
                    .baseUrl(networkSetting.setServerUrl())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
        return builder
    }

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = getHttpClient()?.let {
            getRetrofitBuilder()!!
                    .client(it)
                    .build()
        }
        return retrofit?.create(serviceClass)!!
    }

    companion object {
        const val TIMEOUT_READ = 20
        const val TIMEOUT_CONNECT = 20
        const val TIMEOUT_WRITE = 30

        const val PRETTY_LOGGER = "PRETTY_LOGGER"
    }

    internal class ReceivedCookiesInterceptor(private var networkSetting: NetworkInterface) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            networkSetting.setResponse(originalResponse)
            return originalResponse
        }
    }
}