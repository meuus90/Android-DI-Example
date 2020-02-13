package com.dependency_injection.base.utility

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.dependency_injection.base.network.Resource
import com.dependency_injection.base.network.Status
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MediatorLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Timber.w("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (t is Resource) {
                if (t.getStatus() == Status.SUCCESS) {
                    if (pending.compareAndSet(true, false)) {
                        removeObservers(owner)
                        observer.onChanged(t)
                    }
                } else
                    observer.onChanged(t)
            } else {
                if (pending.compareAndSet(true, false)) {
                    removeObservers(owner)
                    observer.onChanged(t)
                }
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}