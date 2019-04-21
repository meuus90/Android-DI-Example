package com.example.demo.network

import io.reactivex.Single
import retrofit2.http.*

interface APISample {
    @GET("/api/user/")
    fun getSampleList(): Single<Any>

    @GET("/api/user/")
    fun getSample(@Query("query") query: String): Single<Any>

    @POST("/api/user/")
    fun postSample(@Body body: Any): Single<Any>

    @FormUrlEncoded
    @POST("/api/user/")
    fun postSampleWithField(@Field("field") field: String): Single<Any>
}
