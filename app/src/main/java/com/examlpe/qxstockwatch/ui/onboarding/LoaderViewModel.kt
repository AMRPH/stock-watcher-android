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

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    private var isFlag: Boolean = false
    private var listGeo: List<String> = emptyList()
    private var listIps: List<String> = emptyList()

    var liveData = MutableLiveData(0)

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        getData()
    }

    private fun getData(){
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    isFlag = remoteConfig.getBoolean("isCloakaOn")

                    val itemType = object : TypeToken<List<String>>() {}.type
                    listIps = Gson().fromJson(remoteConfig.getString("ListRestrictedIPs"), itemType)
                    listGeo = Gson().fromJson(remoteConfig.getString("ListRestrictedGeo"), itemType)

                    if (isFlag && getIP() !in listIps && getGeo() !in listGeo){
                        liveData.postValue(2)
                    } else {
                        App.dbRepository.isEmpty()
                            .subscribe(Consumer {res ->
                                if (res == 0) getStocks()
                                else { liveData.postValue(1) }
                            }).let { compositeDisposable.add(it) }
                    }
                }
            }
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

        liveData.postValue(1)
    }

    private fun addStock(symbol: String, name: String){
        val stock = StockDB(0, symbol, name,
            0F, 0F, 0F,
            "", isTracking = false, isNotification = false,
            isMark = false, mark = 0F, isMarkReached = false, trendMark = ""
        )

        App.dbRepository.insert(stock)
    }

    private fun getGeo(): String {
        val tm = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        return tm.networkCountryIso
    }

    private fun getIP(): String {
        val wm: WifiManager = application.getSystemService(Context.WIFI_SERVICE) as WifiManager

        return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }
}