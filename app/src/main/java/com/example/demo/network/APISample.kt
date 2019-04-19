package com.example.demo.network

import io.reactivex.Single
import retrofit2.http.*

interface APISample {
    @GET("/")
    fun getSample(@Query("query") query: String): Single<Any>

    @POST("/")
    fun postSample(@Body body: Any): Single<Any>

    @FormUrlEncoded
    @POST("/")
    fun postSampleWithField(@Field("field") field: String): Single<Any>
}
