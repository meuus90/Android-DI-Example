package com.dependency_injection.sample.di.module

import com.dependency_injection.sample.di.ActivityScope
import com.dependency_injection.sample.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentBuildModule::class, ViewModelFactoryModule::class])
    abstract fun bindMainActivity(): MainActivity
}