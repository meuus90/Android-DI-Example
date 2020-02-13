package com.dependency_injection.sample.di.module.activity

import com.dependency_injection.sample.di.module.fragment.HomeFragmentModule
import com.dependency_injection.sample.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}