package com.examlpe.qxstockwatch.database

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseRepository(private val dao: StockDao) {

    fun insert(stock: StockDB){
        Thread {
            dao.insert(stock)
        }.start()
    }

    fun update(stock: StockDB){
        Thread {
            dao.update(stock)
        }.start()
    }



    fun getStocksByString(request: String): Flowable<List<StockDB>>{
        return dao.getByString("$request%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }



    fun getTrackingStocks(): Flowable<List<StockDB>>{
        return dao.getIsTracking()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTrackingStocksByString(request: String): Flowable<List<StockDB>>{
        return dao.getTrackingByString("$request%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }



    fun getMarkStocks(): Flowable<List<StockDB>>{
        return dao.getIsMark()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getMarkStocksByString(request: String): Flowable<List<StockDB>>{
        return dao.getMarkByString("$request%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }



    fun getMarkStocksSingle(): Single<List<StockDB>>{
        return dao.getIsMarkSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }

    fun getTrackingStocksSingle(): Single<List<StockDB>>{
        return dao.getIsTrackingSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }



    fun isEmpty(): Single<Int>{
        return dao.getDataCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}