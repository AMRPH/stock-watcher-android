package com.examlpe.qxstockwatch

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.examlpe.qxstockwatch.database.DatabaseRepository
import com.examlpe.qxstockwatch.database.StockDatabase
import com.examlpe.qxstockwatch.net.FinnhubRepository
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient

const val FINNHUB_APP_ID = "cj91le1r01qjjsj7kmrgcj91le1r01qjjsj7kms0"

class App: Application() {

    companion object {
        lateinit var dbRepository: DatabaseRepository
            private set

        lateinit var netRepository: FinnhubRepository
            private set

        var isOnboarding = false
    }

    override fun onCreate() {
        super.onCreate()
        ApiClient.apiKey["token"] = FINNHUB_APP_ID
        netRepository = FinnhubRepository(DefaultApi())

        val db = Room.databaseBuilder(this, StockDatabase::class.java, "database").build()
        dbRepository = DatabaseRepository(db.getStockDao())

        isOnboarding = getSharedPreferences("stock_app", Context.MODE_PRIVATE).getBoolean("isOnboarding", false)
    }
}