package com.examlpe.qxstockwatch.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [StockDB::class]
)
abstract class StockDatabase : RoomDatabase() {

    abstract fun getStockDao(): StockDao
}