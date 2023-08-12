package com.examlpe.qxstockwatch.ui.main

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.examlpe.qxstockwatch.databinding.ActivityWebBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig


class WebActivity: AppCompatActivity()  {

    private lateinit var binding: ActivityWebBinding
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        url = Firebase.remoteConfig.getString("BlackLink")

        binding.web.settings.javaScriptEnabled = true
        binding.web.webViewClient = WebViewClient()
        binding.web.loadUrl(url)

        binding.btnBack.setOnClickListener {
            if (binding.web.canGoBack()) binding.web.goBack()
        }

        binding.btnRefresh.setOnClickListener {
            binding.web.reload()
        }
    }
}