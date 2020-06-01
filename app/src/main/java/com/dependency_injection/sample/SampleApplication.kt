package com.dependency_injection.sample

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide
import com.dependency_injection.sample.di.helper.AppInjector
import com.facebook.stetho.Stetho
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.exitProcess


class SampleApplication : Application(), CameraXConfig.Provider, LifecycleObserver, HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    internal var isInForeground = false

    companion object {
        fun exitApplication(activity: Activity) {
            ActivityCompat.finishAffinity(activity)
            exit()
        }

        private fun exit() {
            android.os.Process.killProcess(android.os.Process.myPid())
            System.runFinalizersOnExit(true)
            exitProcess(0)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if ("robolectric" != Build.FINGERPRINT && BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)

        JodaTimeAndroid.init(this)
        Logger.addLogAdapter(AndroidLogAdapter())

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        AppInjector.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    /** @returns Camera2 default configuration */
    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    override fun androidInjector() = dispatchingAndroidInjector

    class ShareableAssetComponentCallback(private val app: SampleApplication) : ComponentCallbacks2 {
        override fun onConfigurationChanged(newConfig: Configuration?) {}

        override fun onTrimMemory(level: Int) {
            Glide.get(app).trimMemory(level)
            if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
                app.isInForeground = true
            }
        }

        override fun onLowMemory() {
            Timber.d("onLowMemory")

            Glide.get(app).clearMemory()
        }
    }
}