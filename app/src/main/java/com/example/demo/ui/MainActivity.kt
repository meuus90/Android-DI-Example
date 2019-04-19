package com.example.demo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.demo.R.layout.activity_main)

        b_gotoEditTextSample.setOnClickListener {
            val intent = Intent(this@MainActivity, EditTextSampleActivity::class.java)
            startActivity(intent)
        }

        b_gotoNetworkSample.setOnClickListener {
            val intent = Intent(this@MainActivity, NetworkSampleActivity::class.java)
            startActivity(intent)
        }
    }
}