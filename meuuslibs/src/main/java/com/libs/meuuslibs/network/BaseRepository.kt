package com.libs.meuuslibs.network

import android.app.Application
import com.google.common.base.Preconditions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import okhttp3.Response

open class BaseRepository(rootApplication: Application) {
    init {
        Preconditions.checkNotNull(rootApplication, "RootApplication cannot be null")
    }

    interface ServiceProvider<R> {
        fun onService(it: R): R
    }

    fun resetService() {
        mRequestMaker = null
    }

    private var mRequestMaker: RequestMaker? = null
    fun <S> createService(serviceClass: Class<S>): S {
        if (mRequestMaker == null)
            mRequestMaker = RequestMaker(setNetworkSetting())
        return mRequestMaker!!.createService(serviceClass)
    }

    fun <T> makeResponse(service: Single<T>, response: ServiceProvider<T>): Single<T> {
        return service
                .subscribeOn(Schedulers.io())
                .map {
                    response.onService(it) ?: it
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    open fun setNetworkSetting(): NetworkSetting {
        return object : NetworkSetting {
            override fun setServerUrl(): String {
                return ""
            }

            override fun setPrintLog(): Boolean {
                return false
            }

            override fun setHeaderBuilder(headerBuilder: Request.Builder): Request.Builder {
                headerBuilder.header("Accept", "application/json")
                return headerBuilder
            }

            override fun setResponse(response: Response) {
            }

            override fun setErrorHandler(body: String) {
            }
        }
    }
}