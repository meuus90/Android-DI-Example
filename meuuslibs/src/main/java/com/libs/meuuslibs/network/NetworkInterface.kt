package com.libs.meuuslibs.network

import okhttp3.Request
import okhttp3.Response

interface NetworkInterface {
    fun setServerUrl():String

    fun setPrintLog(): Boolean

    fun setHeaderBuilder(headerBuilder: Request.Builder): Request.Builder

    fun setResponse(response: Response)

    fun setErrorHandler(body: String)
}