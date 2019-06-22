package com.dependency_injection.sample.di.module

import android.content.Context
import com.dependency_injection.sample.storage.LocalStorage
import com.dependency_injection.sample.storage.APILocal
import com.dependency_injection.sample.storage.APIServer
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class APIModule {
    @Singleton
    @Provides
    fun provideLocalStorageInterface(context: Context): APILocal {
        return LocalStorage(context)
    }

    @Singleton
    @Provides
    fun provideRemoteStorageInterface(retrofit: Retrofit): APIServer {
        return retrofit.create(APIServer::class.java)
    }
}