package com.example.elog

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.android.lipy.elog.ELog
import com.android.lipy.elog.ELogConfigs
import java.io.File

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //init
        val configs = ELogConfigs.Builder()
                .setTag("MyTestConfigs")
                .enableLogcat()
                .enableDiskLog()
                .setDiskTag("TestDiskTagS")
                .setLogcatTag("TestLogcatTagS")
                .setLogcatMethodCount(7)
                .setLogcatShowThreadInfo(false)
                .setDiskPath(Environment.getExternalStorageDirectory().absolutePath + File.separatorChar + "service")
                .setLogcatDebugPriority(Log.WARN)
                .setDiskDebugPriority(Log.DEBUG)
                .build()
        ELog.init(configs)

        ELog.d("service info %s", "this is a debug format string!")
        ELog.e("service info %s", "this is a err format string!")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
