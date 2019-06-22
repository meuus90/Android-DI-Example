package com.dependency_injection.sample.di.module

import com.dependency_injection.sample.di.FragmentScope
import com.dependency_injection.sample.ui.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuildModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ViewModelFactoryModule::class])
    fun bindSplashFragment(): HomeFragment
}