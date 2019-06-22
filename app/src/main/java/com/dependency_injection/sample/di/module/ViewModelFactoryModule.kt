package com.dependency_injection.sample.di.module

import com.dependency_injection.sample.repositiory.RepositoryInterface
import com.dependency_injection.sample.ui.HomeViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelFactoryModule {
    @Provides
    fun provideHomeViewModelFactory(
            repo: RepositoryInterface
    ): HomeViewModel.Factory {
        return HomeViewModel.Factory(repo)
    }
}