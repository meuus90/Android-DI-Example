package com.dependency_injection.sample.di

import com.dependency_injection.sample.navigator.BaseSchedulerProvider
import com.dependency_injection.sample.repositiory.RepositoryInterface
import com.dependency_injection.sample.storage.APILocal
import com.dependency_injection.sample.storage.APIServer

interface InjectorInterface {
    fun provideBaseSchedulerProvider(): BaseSchedulerProvider

    fun provideRepository(
            navi: BaseSchedulerProvider,
            apiServer: APIServer,
            apiLocal: APILocal): RepositoryInterface
}