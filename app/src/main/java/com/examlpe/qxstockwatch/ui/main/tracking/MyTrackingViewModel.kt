package com.examlpe.qxstockwatch.ui.main.tracking

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.ui.base.BaseViewModel

class MyTrackingViewModel(application: Application) : BaseViewModel(application) {

    var stocksLiveData = MutableLiveData<List<StockDB>>(emptyList())
    var deletedLiveData = MutableLiveData<StockDB>(null)

    init {
        setDefault()
    }


    fun getStocksByString(request: String){
        App.dbRepository.getTrackingStocksByString(request)
            .subscribe {
                stocksLiveData.postValue(it)
            }.let {
                compositeDisposable.clear()
                compositeDisposable.add(it)
            }
    }

    fun setDefault(){
        App.dbRepository.getTrackingStocks()
            .subscribe {
                stocksLiveData.postValue(it)
            }.let {
                compositeDisposable.clear()
                compositeDisposable.add(it)
            }
    }
}