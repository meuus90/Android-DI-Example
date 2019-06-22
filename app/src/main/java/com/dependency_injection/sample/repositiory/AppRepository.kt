package com.dependency_injection.sample.repositiory

import com.dependency_injection.sample.model.DataUser
import com.dependency_injection.sample.navigator.BaseSchedulerProvider
import com.dependency_injection.sample.storage.APILocal
import com.dependency_injection.sample.storage.APIServer
import com.dependency_injection.sample.storage.BaseRepository
import io.reactivex.Single

class AppRepository(
        navi: BaseSchedulerProvider,
        private val apiServer: APIServer,
        private val apiLocal: APILocal
) : BaseRepository(navi), RepositoryInterface {
    /**
     * onServerApi
     */
    override fun serverGetUser(): Single<DataUser> {
        return makeSingleResponse(apiServer.getUser())
    }


    /**
     * onLocalApi
     */
    override fun localSetUser(dataUser: DataUser) {
        apiLocal.setUser(dataUser)
    }

    override fun localGetUser(): DataUser? {
        return apiLocal.getUser()
    }

}