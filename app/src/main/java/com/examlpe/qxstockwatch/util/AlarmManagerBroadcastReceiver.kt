package com.examlpe.qxstockwatch.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import com.examlpe.qxstockwatch.App
import com.examlpe.qxstockwatch.R
import com.examlpe.qxstockwatch.database.DatabaseRepository
import com.examlpe.qxstockwatch.database.StockDatabase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class AlarmManagerBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("ALARM", "onReceive")

        getData(context!!)
    }

    private fun getData(context: Context){
        App.dbRepository.getTrackingStocksSingle()
            .subscribe({
                Log.d("ALARM", "getTrackingStocksSingle")
                if (it.isNotEmpty()){
                    for (stock in it) {
                        App.netRepository.quote(stock.symbol)
                            .subscribe({ quote ->
                                Log.d("ALARM", "${stock.symbol} old: ${stock.cost} new: ${(quote.c!! * 100).roundToInt() / 100.0F}")
                                stock.cost = (quote.c!! * 100).roundToInt() / 100.0F
                                stock.day = (quote.d!! * 100).roundToInt() / 100.0F
                                stock.dayPerc = (quote.dp!! * 100).roundToInt() / 100.0F
                                App.dbRepository.update(stock)
                            }, {}).let {}
                    }

                    TimeUnit.SECONDS.sleep(30)

                    checkMark(context)
                }
            }, {
                Log.d("ALARM", "${it.message}")
            }).let {}
    }

    private fun checkMark(context: Context){
        App.dbRepository.getMarkStocksSingle()
            .subscribe({
                Log.d("ALARM", "getMarkStocksSingle")
                if (it.isNotEmpty()){
                    for (stock in it){
                        if (!stock.isMarkReached){
                            if (stock.trendMark == "up" && stock.mark < stock.cost) stock.isMarkReached = true
                            if (stock.trendMark == "down" && stock.mark > stock.cost) stock.isMarkReached = true

                            //TODOsendNotification(context, stock.name, stock.mark.toString())
                            if (stock.isMarkReached && stock.isNotification){
                                sendNotification(context, stock.name, stock.mark.toString())
                            }
                        }

                        App.dbRepository.update(stock)
                    }
                }
            }, {
                Log.d("ALARM", "${it.message}")
            }).let {}
    }

    private fun sendNotification(context: Context, name: String, mark: String){
        Log.d("ALARM", "sendNotification")
        val notification = NotificationCompat.Builder(context, "111")
            .setSmallIcon(R.drawable.ic_logo_launcher)
            .setContentTitle("The stock has reached the mark!")
            .setContentText("$name reached the price of $mark \$")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("111", "1", NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(1, notification)
        }
    }
}