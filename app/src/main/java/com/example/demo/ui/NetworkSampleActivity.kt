package com.example.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demo.R
import com.example.demo.network.SampleRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_network_sample.*

class NetworkSampleActivity : AppCompatActivity() {
    private var _disposables: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_sample)
        _disposables = CompositeDisposable()

        button.setOnClickListener {
            _disposables!!.add(SampleRepository(application, this)
                    .getSampleList()
                    .subscribe({
                        contents.text = it.toString()
                    }, {

                    }))
        }
    }

    override fun onDestroy() {
        if (_disposables != null) {
            _disposables!!.clear()
            _disposables!!.dispose()
        }

        super.onDestroy()
    }
}