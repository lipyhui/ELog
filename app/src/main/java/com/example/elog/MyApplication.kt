package com.example.elog

import android.app.Application
import com.android.lipy.elog.ELog
import com.android.lipy.elog.ELogConfigs

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        //init
        val configs = ELogConfigs.Builder()
                .setTag("MyTestConfigs")
                .enableLogcat()
                .enableDiskLog()
                .setDiskTag("TestDiskTag")
                .setLogcatTag("TestLogcatTag")
                .setLogcatMethodCount(7)
                .build()
        ELog.init(configs)
    }
}