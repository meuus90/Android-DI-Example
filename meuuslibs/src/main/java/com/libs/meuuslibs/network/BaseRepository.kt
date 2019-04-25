package com.libs.meuuslibs.network

import android.app.Application
import com.google.common.base.Preconditions
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import okhttp3.Response

open class BaseRepository(rootApplication: Application) {
    init {
        Preconditions.checkNotNull(rootApplication, "RootApplication cannot be null")
    }

    interface SingleProvider<T> {
        fun onService(it: T): T
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

    fun <T> makeSingleResponse(service: Single<T>, response: SingleProvider<T>): Single<T> {
        return service
                .subscribeOn(Schedulers.io())
                .map {
                    response.onService(it) ?: it
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun makeCompletableResponse(service: Completable): Completable {
        return service
                .subscribeOn(Schedulers.io())
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