package com.dependency_injection.sample.di.module

import android.util.Log
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    companion object {
        private const val timeout_read = 5L
        private const val timeout_connect = 20L
        private const val timeout_write = 30L
        const val key = "general-jwt="
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val ok = OkHttpClient.Builder()
                .connectTimeout(timeout_connect, TimeUnit.SECONDS)
                .readTimeout(timeout_read, TimeUnit.SECONDS)
                .writeTimeout(timeout_write, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    if (isJSONValid(message))
                        Logger.json(message)
                    else
                        Log.d("pretty", message)
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
            ok.addInterceptor(logging)

        }

        /*dispatcher = Dispatcher()
        httpClientBuilder.dispatcher(dispatcher)*/

        ok.addInterceptor(interceptor)
        return ok.build()
    }
}