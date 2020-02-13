package com.dependency_injection.base.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dependency_injection.base.utility.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor(private val boundType: Int) {
    companion object {
        private const val NONE_ITEM_COUNT = 20

        const val BOUND_TO_BACKEND = 0
        const val BOUND_FROM_BACKEND = 1
        const val BOUND_FROM_LOCAL = 2
    }

    private val result = MediatorLiveData<Resource>()
    private val resource = Resource()

    private var cacheData: LiveData<ResultType>? = null

    init {
        resource.loading(null)
        runBlocking {
            when (boundType) {
                BOUND_FROM_BACKEND -> {
                    runNetworkJob(MediatorLiveData())
                }

                BOUND_FROM_LOCAL -> {
                    fetchFromLocal()
                }

                BOUND_TO_BACKEND -> {

                }
            }
        }
    }

    private suspend fun fetchFromLocal() {
        fetchDataFromCache(this)
    }

    private suspend fun runNetworkJob(cacheSource: MediatorLiveData<ResultType>) {
        handleDataFromNetwork(cacheSource)
    }

    private suspend fun fetchDataFromCache(resource: NetworkBoundResource<ResultType, RequestType>) {
        val cacheData = withContext(Dispatchers.IO) {
            resource.loadFromCache(false, NONE_ITEM_COUNT, 0)
        }

        cacheData?.let { cd ->
            resource.result.addSource(cd) {
                resource.result.removeSource(cd)
                resource.result.addSource(cd) { updatedData ->
                    resource.result.postValue(resource.resource.success(updatedData))
                }
            }
        }
    }

    private suspend fun handleDataFromNetwork(cacheSource: MediatorLiveData<ResultType>) {
        val responseData = doNetworkJob()

        // we re-attach cacheSource as a new source, it will dispatch its latest value quickly
//        result.postValue(resource.loading("loading"))
        result.value = resource.loading("loading")
        result.addSource(responseData) { response ->
            result.removeSource(responseData)
            result.removeSource(cacheSource)
            //no inspection ConstantConditions
            when (response) {
                is ApiSuccessResponse -> {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            workToCache(response.body)
                            cacheData = loadFromCache(false, NONE_ITEM_COUNT, 0)
                        }

                        if (cacheData != null) {
                            result.addSource(cacheData!!) { newData ->
                                result.postValue(resource.success(newData))
                            }
                        } else {
                            result.postValue(resource.success(response.body))
                        }
                    }
                }

                is ApiEmptyResponse -> {
                    result.postValue(resource.success(""))
                }

                is ApiErrorResponse -> {
                    failed(response)
                }
            }
        }
    }

    private fun failed(response: ApiErrorResponse<RequestType>) {
//        result.postValue(resource.error(response.errorMessage, response.statusCode))
        result.value = resource.error(response.errorMessage, response.statusCode)
        onNetworkError(response.errorMessage, response.statusCode)
    }

    protected abstract fun onNetworkError(errorMessage: String?, errorCode: Int)

    protected abstract fun onFetchFailed(failedMessage: String?)

    fun getAsLiveData(): MediatorLiveData<Resource> {
        return result
    }

    fun getAsSingleLiveEvent(): SingleLiveEvent<Resource> {
        val resultEvent = SingleLiveEvent<Resource>()
        resultEvent.addSource(result) {
            resultEvent.value = it
        }
        return resultEvent
    }

    @WorkerThread
    protected open suspend fun workToCache(item: RequestType) {
    }

    @WorkerThread
    protected open suspend fun loadFromCache(
            isLatest: Boolean,
            itemCount: Int,
            pages: Int
    ): LiveData<ResultType>? {
        return cacheData
    }

    @WorkerThread
    protected abstract suspend fun doNetworkJob(): LiveData<ApiResponse<RequestType>>

    @WorkerThread
    protected open suspend fun clearCache() {
    }
}