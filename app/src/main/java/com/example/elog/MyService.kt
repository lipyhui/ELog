package com.example.elog

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.android.lipy.elog.AndroidLogAdapter
import com.android.lipy.elog.DiskLogAdapter
import com.android.lipy.elog.ELog

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //init
        ELog.addLogAdapter(AndroidLogAdapter())
        ELog.addLogAdapter(DiskLogAdapter())

        ELog.e("err %s", "this is a test format string!")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
