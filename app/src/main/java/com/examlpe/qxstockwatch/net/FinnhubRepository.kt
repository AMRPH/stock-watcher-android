package com.examlpe.qxstockwatch.net

import com.examlpe.qxstockwatch.App
import io.reactivex.Observable

import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.models.CompanyProfile2
import io.finnhub.api.models.Quote
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FinnhubRepository(private val api: DefaultApi) {

    fun quote(symbol: String): Single<Quote> {
        return Single.fromCallable { api.quote(symbol) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}