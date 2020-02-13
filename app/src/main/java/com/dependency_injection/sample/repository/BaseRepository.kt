package com.dependency_injection.sample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dependency_injection.base.network.Resource
import com.dependency_injection.sample.datasource.network.ServerAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class BaseRepository<T> {
    @field:Inject
    lateinit var serverAPI: ServerAPI

    abstract suspend fun work(liveData: MutableLiveData<T>): LiveData<Resource>

    companion object {
        internal const val PER_PAGE = 20
        internal const val INDEX = 1
    }
}