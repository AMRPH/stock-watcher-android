package com.examlpe.qxstockwatch.ui.onboarding

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.text.format.Formatter
import androidx.lifecycle.MutableLiveData
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.ui.base.BaseViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.functions.Consumer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class LoaderViewModel(private val application: Application): BaseViewModel(application){

    var liveData = MutableLiveData(false)

    init {
        App.dbRepository.isEmpty()
            .subscribe(Consumer {res ->
                if (res == 0) getStocks()
                else { liveData.postValue(true) }
            }).let { compositeDisposable.add(it) }
    }

    private fun getStocks(){
        val inputStream: InputStream = application.resources.openRawResource(R.raw.stocks)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var i = 0
        do{
            val eachline: String? = bufferedReader.readLine()
            i += 1
            if (eachline != null){
                val s = eachline.split("/")
                addStock(s[0], s[1])
            }
        } while (eachline != null && i <= 9080)

        liveData.postValue(true)
    }

    private fun addStock(symbol: String, name: String){
        val stock = StockDB(0, symbol, name,
            0F, 0F, 0F,
            "", isTracking = false, isNotification = false,
            isMark = false, mark = 0F, isMarkReached = false, trendMark = ""
        )

        App.dbRepository.insert(stock)
    }
}