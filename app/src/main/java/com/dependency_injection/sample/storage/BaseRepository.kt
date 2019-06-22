package com.dependency_injection.sample.storage

import com.dependency_injection.sample.navigator.BaseSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single

abstract class BaseRepository constructor(val navi: BaseSchedulerProvider) {
    fun <T> makeSingleResponse(service: Single<T>, customResponse: ((T) -> T)? = null): Single<T> {
        return service
                .subscribeOn(navi.io())
                .map {
                    if (customResponse != null)
                        customResponse(it)
                    else it
                }.observeOn(navi.ui())
    }

    fun makeCompletableResponse(service: Completable): Completable {
        return service
                .subscribeOn(navi.io())
                .observeOn(navi.ui())
    }
}