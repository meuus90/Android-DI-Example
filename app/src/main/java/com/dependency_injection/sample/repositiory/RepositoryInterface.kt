package com.dependency_injection.sample.repositiory

import com.dependency_injection.sample.model.DataUser
import io.reactivex.Single

interface RepositoryInterface {
    /**
     * onServerApi
     */
    fun serverGetUser(): Single<DataUser>

    /**
     * onLocalApi
     */
    fun localSetUser(dataUser: DataUser)

    fun localGetUser(): DataUser?
}