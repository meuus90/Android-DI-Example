package com.dependency_injection.base.network

class Resource {
    private lateinit var status: Status

    private var message: String? = null

    private var data: Any? = null

    internal var errorCode: Int = 0

    fun success(data: Any?): Resource {
        status = Status.SUCCESS
        this.data = data
        message = null

        return this
    }

    fun error(msg: String?, errorCode: Int): Resource {
        status = Status.ERROR
        this.errorCode = errorCode
        message = msg

        return this
    }

    fun loading(data: Any?): Resource {
        status = Status.LOADING
        this.data = data
        message = null

        return this
    }

    fun getMessage(): String? {
        return message
    }

    fun getStatus(): Status? {
        return status
    }

    fun getData(): Any? {
        return data
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val resource = other as Resource?

        if (status !== resource?.status) {
            return false
        }

        if (if (message != null) message == resource.message else resource.message == null)
            if (if (data != null) data == resource.data else resource.data == null) return true
        return false

    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + if (message != null) message!!.hashCode() else 0
        result = 31 * result + if (data != null) data?.hashCode()!! else 0
        return result
    }

    override fun toString(): String {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\''.toString() +
                ", data=" + data +
                '}'.toString()
    }
}
