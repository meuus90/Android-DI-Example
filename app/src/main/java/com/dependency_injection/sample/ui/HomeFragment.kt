package com.dependency_injection.sample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.demo.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class HomeFragment() : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @Inject
    lateinit var factory: HomeViewModel.Factory
    val mViewModel: HomeViewModel by lazy {
        val viewModel = ViewModelProviders.of(this, factory)
                .get(HomeViewModel::class.java)

        viewModel
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // todo : init fragment
    }
}