/*
 * Copyright (C)  2020 MeUuS90
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dependency_injection.sample.di.module

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.room.Room
import com.dependency_injection.base.network.NetworkError
import com.dependency_injection.base.storage.LocalStorage
import com.dependency_injection.sample.datasource.model.Cache
import com.dependency_injection.sample.datasource.network.LiveDataCallAdapterFactory
import com.dependency_injection.sample.datasource.network.ServerAPI
import com.example.demo.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * A module for Android-specific dependencies which require a [Context] or
 * [android.app.Application] to create.
 */
@Module
class AppModule {
    companion object {
        const val timeout_read = 30L
        const val timeout_connect = 30L
        const val timeout_write = 30L
    }

    @Provides
    @Singleton
    fun appContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideLocalStorage(context: Context): LocalStorage {
        return LocalStorage(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor, context: Context): OkHttpClient {
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

        val ok = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(timeout_connect, TimeUnit.SECONDS)
                .readTimeout(timeout_read, TimeUnit.SECONDS)
                .writeTimeout(timeout_write, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            if ("robolectric" != Build.FINGERPRINT && BuildConfig.DEBUG)
                ok.addNetworkInterceptor(StethoInterceptor())

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

        ok.addInterceptor(interceptor)
        return ok.build()
    }

    @Provides
    @Singleton
    fun provideGSon(): Gson {
        return GsonBuilder()
                .create()
    }

    @Provides
    //@Singleton
    fun getRequestInterceptor(/*context: Context, localRepository: LocalRepository*/): Interceptor {
        return Interceptor {
            Timber.tag("PRETTY_LOGGER")

            val original = it.request()

            val requested = with(original) {
                val builder = newBuilder()

                builder.header("Accept", "application/json")

                builder.build()
            }

            val response = it.proceed(requested)
            val body = response.body
            val bodyStr = body?.string()
            Timber.e("**http-num: ${response.code}")
            Timber.e("**http-body: $body")

            val cookies = HashSet<String>()
            for (header in response.headers("Set-Cookie")) {
                cookies.add(header)
            }

            if (!response.isSuccessful) {
                val networkError = Gson().fromJson(bodyStr, NetworkError::class.java)

                networkError.error.code?.let {
                    //todo : do something on error
                }
            }

            val builder = response.newBuilder()

            builder.body(
                    ResponseBody.create(
                            body?.contentType()
                            , bodyStr?.toByteArray()!!
                    )
            ).build()

        }
    }

    @Singleton
    @Provides
    fun provideAPI(gson: Gson, okHttpClient: OkHttpClient): ServerAPI {
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.apiServer)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build()

        return retrofit.create(ServerAPI::class.java)
    }

    @Singleton
    @Provides
    internal fun provideCache(app: Application) =
            Room.databaseBuilder(app, Cache::class.java, "sa.db").build()

    @Singleton
    @Provides
    internal fun provideItemDao(cache: Cache) = cache.itemDao()
}