package com.dependency_injection.sample

import com.dependency_injection.sample.di.InjectorInterface
import com.dependency_injection.sample.navigator.BaseSchedulerProvider
import com.dependency_injection.sample.navigator.SchedulerProvider
import com.dependency_injection.sample.repositiory.AppRepository
import com.dependency_injection.sample.repositiory.RepositoryInterface
import com.dependency_injection.sample.storage.APILocal
import com.dependency_injection.sample.storage.APIServer
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
