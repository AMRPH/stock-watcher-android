package com.examlpe.qxstockwatch.ui.main.add

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.ui.base.BaseViewModel
import io.reactivex.functions.Consumer
import java.net.CacheRequest
import kotlin.math.roundToInt

class AddTrackingViewModel(application: Application) : BaseViewModel(application) {

    var stocksLiveData = MutableLiveData<List<StockDB>>(emptyList())
    var addedLiveData = MutableLiveData<StockDB>(null)

    fun getStocksByString(request: String){
        App.dbRepository.getStocksByString(request)
            .subscribe {
                stocksLiveData.postValue(it)
            }.let {
                compositeDisposable.clear()
                compositeDisposable.add(it)
            }
    }

    fun setEmpty(){
        stocksLiveData.postValue(emptyList())
    }

}