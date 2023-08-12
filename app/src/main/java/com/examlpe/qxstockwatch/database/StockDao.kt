package com.examlpe.qxstockwatch.database

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface StockDao {

    @Insert
    fun insert(stockDbEntity: StockDB)

    @Update
    fun update(stockDbEntity: StockDB)

    @Query("SELECT * FROM stocks WHERE symbol LIKE :request AND is_tracking = 0")
    fun getByString(request: String): Flowable<List<StockDB>>


    @Query("SELECT * FROM stocks WHERE is_tracking = 1")
    fun getIsTracking(): Flowable<List<StockDB>>

    @Query("SELECT * FROM stocks WHERE symbol LIKE :request AND is_tracking = 1")
    fun getTrackingByString(request: String): Flowable<List<StockDB>>


    @Query("SELECT * FROM stocks WHERE is_mark = 1")
    fun getIsMark(): Flowable<List<StockDB>>

    @Query("SELECT * FROM stocks WHERE symbol LIKE :request AND is_mark = 1")
    fun getMarkByString(request: String): Flowable<List<StockDB>>


    @Query("SELECT * FROM stocks WHERE is_tracking = 1")
    fun getIsTrackingSingle(): Single<List<StockDB>>

    @Query("SELECT * FROM stocks WHERE is_mark = 1")
    fun getIsMarkSingle(): Single<List<StockDB>>

    @Query("SELECT COUNT(*) FROM stocks")
    fun getDataCount(): Single<Int>
}