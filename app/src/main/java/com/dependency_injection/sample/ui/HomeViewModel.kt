package com.dependency_injection.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dependency_injection.sample.model.DataUser
import com.dependency_injection.sample.repositiory.RepositoryInterface
import io.reactivex.Completable
import io.reactivex.Single

class HomeViewModel(val repo: RepositoryInterface) : ViewModel() {
    @Suppress("UNCHECKED_CAST")
    class Factory constructor(private val repo: RepositoryInterface) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
                HomeViewModel(repo) as T
    }

    fun setUserOnLocal(): Completable {
        return repo.serverGetUser()
                .flatMap {
                    repo.localSetUser(it)
                    Single.just(it)
                }.doOnError {
                    it.printStackTrace()
                }.ignoreElement()
    }

    fun getUserOnLocal(): DataUser {
        return repo.localGetUser() ?: DataUser("", "")
    }
}