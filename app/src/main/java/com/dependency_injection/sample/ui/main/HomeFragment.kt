package com.dependency_injection.sample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dependency_injection.base.view.AutoClearedValue
import com.dependency_injection.sample.ui.BaseFragment
import com.example.demo.R

class HomeFragment : BaseFragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val acvView =
                AutoClearedValue(
                        this,
                        inflater.inflate(R.layout.fragment_home, container, false)
                )
        return acvView.get()?.rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getList()
    }

    fun getList(){

    }
}