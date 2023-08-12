package com.examlpe.qxstockwatch.ui.main.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.database.StockDB
import com.examlpe.qxstockwatch.ui.base.BaseViewModel

class NotificationsViewModel(application: Application): BaseViewModel(application) {

    var stocksLiveData = MutableLiveData<List<StockDB>>(emptyList())

    init {
        setDefault()
    }


    fun getStocksByString(request: String){
        App.dbRepository.getMarkStocksByString(request)
            .subscribe {
                stocksLiveData.postValue(it)
            }.let {
                compositeDisposable.clear()
                compositeDisposable.add(it)
            }
    }

    fun setDefault(){
        App.dbRepository.getMarkStocks()
            .subscribe {
                stocksLiveData.postValue(it)
            }.let {
                compositeDisposable.clear()
                compositeDisposable.add(it)
            }
    }
}