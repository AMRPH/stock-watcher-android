package com.examlpe.qxstockwatch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks")
data class StockDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val symbol: String,
    @ColumnInfo val name: String,
    @ColumnInfo var cost: Float,
    @ColumnInfo var day: Float,
    @ColumnInfo(name = "day_perc") var dayPerc: Float,
    @ColumnInfo var logo: String,
    @ColumnInfo(name = "is_tracking") var isTracking: Boolean,
    @ColumnInfo(name = "is_notification") var isNotification: Boolean,
    @ColumnInfo(name = "is_mark_reached") var isMarkReached: Boolean,
    @ColumnInfo(name = "is_mark") var isMark: Boolean,
    @ColumnInfo var mark: Float,
    @ColumnInfo(name = "trend_mark") var trendMark: String,
)