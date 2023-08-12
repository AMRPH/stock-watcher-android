package com.examlpe.qxstockwatch.ui.onboarding

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.databinding.ActivityReviewBinding
import com.examlpe.qxstockwatch.ui.main.MainActivity
import com.examlpe.qxstockwatch.util.UIHelper
import com.google.android.play.core.review.ReviewManagerFactory

class ReviewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private var grantedLiveData = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_review)
        binding.ivTrader.setImageBitmap(UIHelper.getRoundedCroppedBitmap(icon))

        binding.btnContinue.setOnClickListener {
            startMainActivity()
        }

        grantedLiveData.observe(this) {
            if (it) review()
        }

        permission()
    }

    private fun permission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                grantedLiveData.postValue(granted)
            }.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            grantedLiveData.postValue(true)
        }
    }

    private fun review(){
        val reviewManager = ReviewManagerFactory.create(this)
        val requestReviewFlow = reviewManager.requestReviewFlow()
        requestReviewFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result

                val flow = reviewManager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener {
                   //startMainActivity()
                }
            } else {
                //startMainActivity()
            }
        }
    }

    private fun startMainActivity(){
        App.isOnboarding = true
        getSharedPreferences("stock_app", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("isOnboarding", App.isOnboarding)
            .apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}