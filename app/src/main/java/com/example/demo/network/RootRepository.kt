package com.example.demo.network

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.example.demo.ui.NetworkSampleActivity
import com.libs.meuuslibs.network.BaseRepository
import com.libs.meuuslibs.network.NetworkSetting
import kotlinx.android.synthetic.main.activity_network_sample.*
import okhttp3.Request
import okhttp3.Response

open class RootRepository(val rootApplication: Application, val activity: Activity) : BaseRepository(rootApplication) {
    override fun setNetworkSetting(): NetworkSetting {
        return object : NetworkSetting {
            override fun setServerUrl(): String {
                return ""
            }

            override fun setPrintLog(): Boolean {
                return true
            }

            override fun setHeaderBuilder(headerBuilder: Request.Builder): Request.Builder {
                headerBuilder.header("Version", getAppVersionCode(rootApplication))
                headerBuilder.header("Accept", "application/json")
                return headerBuilder
            }

            override fun setResponse(response: Response) {
            }

            override fun setErrorHandler(body: String) {
                (activity as NetworkSampleActivity).contents.text = body
            }
        }
    }

    private fun getAppVersionCode(context: Context): String {
        val packageInfo: PackageInfo
        try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return "android:" + packageInfo.versionCode + ":" + packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }
}