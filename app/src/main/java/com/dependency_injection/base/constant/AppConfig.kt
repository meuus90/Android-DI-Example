package com.dependency_injection.base.constant

import com.example.demo.BuildConfig


object AppConfig {
    val isProductionVersion: Boolean
        get() = BuildConfig.FLAVOR.contains("prod") && !BuildConfig.DEBUG

    val isDevVersion: Boolean
        get() = BuildConfig.FLAVOR.contains("dev")

    val isMockVersion: Boolean
        get() = BuildConfig.FLAVOR.contains("mock")

    val isTypeDebug: Boolean
        get() = BuildConfig.BUILD_TYPE.contains("debug")

    val isTypeRelease: Boolean
        get() = BuildConfig.BUILD_TYPE.contains("release")
}