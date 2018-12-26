package com.example.elog

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.lipy.elog.ELog
import com.android.lipy.elog.ELogConfigs

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //init
        val configs = ELogConfigs.Builder()
                .setTag("MyTestConfigs")
                .enableLogcat()
                .enableDiskLog()
                .setDiskTag("TestDiskTag")
                .setLogcatTag("TestLogcatTag")
                .setLogcatMethodCount(7)
                .setLogcatShowThreadInfo(false)
                .build()
        ELog.init(configs)
        ELog.e("info %s", "this is a test format string!")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
