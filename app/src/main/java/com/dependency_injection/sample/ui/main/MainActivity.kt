package com.dependency_injection.sample.ui.main

import android.os.Bundle
import com.dependency_injection.sample.ui.BaseActivity
import com.example.demo.R


class MainActivity : BaseActivity() {
    override val frameLayoutId = R.id.contentFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragmentInActivity(
                HomeFragment(),
                frameLayoutId
        )
    }

    override fun setContentView() {
        setContentView(R.layout.activity_main)
    }
}