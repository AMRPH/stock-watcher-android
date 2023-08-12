package com.examlpe.qxstockwatch.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.examlpe.qxstockwatch.App
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class MyWorker(private val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    val TAG = "Worker"

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun doWork(): Result {
        return Result.success()
    }


    override fun onStopped() {
        super.onStopped()
        compositeDisposable.clear()
    }
}