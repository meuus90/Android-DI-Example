package com.dependency_injection.sample.storage

import com.dependency_injection.sample.model.DataUser

interface APILocal {
    fun setUser(dataUser: DataUser)
    fun getUser(): DataUser?
}