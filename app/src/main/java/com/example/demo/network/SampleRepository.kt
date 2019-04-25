package com.example.demo.network

import android.app.Activity
import android.app.Application
import io.reactivex.Completable
import io.reactivex.Single

class SampleRepository(rootApplication: Application, activity: Activity) : RootRepository(rootApplication, activity), APISample {
    override fun getSample(query: String): Single<ArrayList<DUser>> {
        return makeSingleResponse(createService(APISample::class.java).getSample(query),
                object : SingleProvider<ArrayList<DUser>> {
                    override fun onService(it: ArrayList<DUser>): ArrayList<DUser> {
                        return it
                    }
                })
    }

    override fun postSample(body: Any): Single<DUser> {
        return makeSingleResponse(createService(APISample::class.java).postSample(body),
                object : SingleProvider<DUser> {
                    override fun onService(it: DUser): DUser {
                        return it
                    }
                })
    }

    override fun postSampleWithField(field: String): Single<DUser> {
        return makeSingleResponse(createService(APISample::class.java).postSampleWithField(field),
                object : SingleProvider<DUser> {
                    override fun onService(it: DUser): DUser {
                        return it
                    }
                })
    }

    override fun deleteUser(password: String): Completable {
        return makeCompletableResponse(createService(APISample::class.java).deleteUser(password))
    }

}