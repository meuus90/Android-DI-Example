package com.dependency_injection.base.utility

import com.dependency_injection.base.network.NetworkBoundResource
import javax.inject.Singleton

@Singleton
class Query {
    var params: List<Any> = listOf()
    var boundType: Int = 0

    companion object{
        fun doQuery(params: List<Any>) =
                Query().apply {
                    this.params = params
                    boundType = NetworkBoundResource.BOUND_FROM_BACKEND
                }
    }
}