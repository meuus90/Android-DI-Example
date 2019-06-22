package com.dependency_injection.sample.storage

import com.dependency_injection.sample.model.DataUser
import io.reactivex.Single
import retrofit2.http.GET

interface APIServer {
    @GET("/user")
    fun getUser(): Single<DataUser>
}