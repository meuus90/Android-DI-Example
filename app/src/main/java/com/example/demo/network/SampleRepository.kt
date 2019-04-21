package com.example.demo.network

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.example.demo.ui.NetworkSampleActivity
import com.libs.meuuslibs.network.NetworkInterface
import com.libs.meuuslibs.network.RootRepository
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_network_sample.*
import okhttp3.Request
import okhttp3.Response

class SampleRepository(val rootApplication: Application, val activity: Activity) : RootRepository(rootApplication), APISample {
    override fun getSampleList(): Single<Any> {
        return makeResponse(createService(APISample::class.java).getSampleList(),
                object : RootRepository.ServiceProvider {
                    override fun onService(it: Any): Any {

                        return it
                    }
                })
    }

    override fun getSample(query: String): Single<Any> {
        return makeResponse(createService(APISample::class.java).getSample(query),
                object : RootRepository.ServiceProvider {
                    override fun onService(it: Any): Any {

                        return it
                    }
                })
    }

    override fun postSample(body: Any): Single<Any> {
        return makeResponse(createService(APISample::class.java).postSample(body),
                object : RootRepository.ServiceProvider {
                    override fun onService(it: Any): Any {

                        return it
                    }
                })
    }

    override fun postSampleWithField(field: String): Single<Any> {
        return makeResponse(createService(APISample::class.java).postSampleWithField(field),
                object : RootRepository.ServiceProvider {
                    override fun onService(it: Any): Any {

                        return it
                    }
                })
    }

    override fun setNetworkSetting(): NetworkInterface {
        return object : NetworkInterface {
            override fun setServerUrl(): String {
                return "http://meuus.sample.com/"
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