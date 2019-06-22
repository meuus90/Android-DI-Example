package com.dependency_injection.sample.di

import com.dependency_injection.sample.AppApplication
import com.dependency_injection.sample.Injection
import com.dependency_injection.sample.di.module.APIModule
import com.dependency_injection.sample.di.module.ActivityBuildModule
import com.dependency_injection.sample.di.module.AppModule
import com.dependency_injection.sample.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            NetworkModule::class,
            APIModule::class,
            ActivityBuildModule::class,
            Injection::class
        ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun applicationBind(application: AppApplication): Builder
    }

    fun inject(application: AppApplication)
}