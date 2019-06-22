package com.example.sample

import com.example.sample.di.InjectorInterface
import com.example.sample.navigator.BaseSchedulerProvider
import com.example.sample.navigator.SchedulerProvider
import com.example.sample.repositiory.AppRepository
import com.example.sample.repositiory.RepositoryInterface
import com.example.sample.storage.APILocal
import com.example.sample.storage.APIServer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class Injection : InjectorInterface {
    @Provides
    @Singleton
    override fun provideBaseSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider()
    }

    @Provides
    @Singleton
    override fun provideRepository(navi: BaseSchedulerProvider, apiServer: APIServer, apiLocal: APILocal): RepositoryInterface {
        return AppRepository(navi, apiServer, apiLocal)
    }
}
