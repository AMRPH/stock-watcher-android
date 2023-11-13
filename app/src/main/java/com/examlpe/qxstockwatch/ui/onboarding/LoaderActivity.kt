package com.examlpe.qxstockwatch.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.ui.main.MainActivity


class LoaderActivity : AppCompatActivity() {

    private lateinit var viewModel: LoaderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)
        viewModel = ViewModelProvider(this)[LoaderViewModel::class.java]

        viewModel.liveData.observe(this) {
            if (it){
                start()
            }
        }
    }

    private fun start(){
        val intent = if (App.isOnboarding){
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, WelcomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }


}