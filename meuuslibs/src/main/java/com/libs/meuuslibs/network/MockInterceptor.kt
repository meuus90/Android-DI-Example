package com.libs.meuuslibs.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

class MockInterceptor(private var networkSetting: NetworkInterface) : Interceptor {

    private val events = ArrayDeque<Any>()
    private val requests = ArrayDeque<Request>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        requests.addLast(request)

        addDelay(1000)

        val req = RequestMaker(networkSetting)
        return req.convertInterceptToResponse(chain)
    }

    fun enqueueResponse(response: Response.Builder) {
        events.addLast(response)
    }

    fun enqueueUnexpectedException(exception: RuntimeException) {
        events.addLast(exception)
    }

    fun enqueueIOException(exception: IOException) {
        events.addLast(exception)
    }

    fun takeRequest(): Request {
        return requests.removeFirst()
    }

    private fun addDelay(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}