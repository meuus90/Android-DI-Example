package com.dependency_injection.sample.di.module

import android.content.Context
import com.dependency_injection.sample.AppApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(app: AppApplication): Context = app
}