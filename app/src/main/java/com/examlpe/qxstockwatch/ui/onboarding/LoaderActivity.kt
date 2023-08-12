package com.examlpe.qxstockwatch.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.ui.main.MainActivity
import com.examlpe.qxstockwatch.ui.main.WebActivity


class LoaderActivity : AppCompatActivity() {

    private lateinit var viewModel: LoaderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)
        viewModel = ViewModelProvider(this)[LoaderViewModel::class.java]

        viewModel.liveData.observe(this) {
            when (it) {
                1 -> startWhite()
                2 -> startBlack()
            }
        }
    }

    private fun startWhite(){
        val intent = if (App.isOnboarding){
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, WelcomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun startBlack(){
        val intent = Intent(this, WebActivity::class.java)
        startActivity(intent)
        finish()
    }

}