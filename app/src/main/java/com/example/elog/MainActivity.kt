package com.example.elog

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.lipy.elog.AndroidLogAdapter
import com.android.lipy.elog.DiskLogAdapter
import com.android.lipy.elog.ELog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()

        //init
        ELog.addLogAdapter(AndroidLogAdapter())
        ELog.addLogAdapter(DiskLogAdapter())

        ELog.hex(0x08)
        ELog.hex(0xE6.toByte())
        ELog.hex(0xF0.toByte())
        ELog.hex("type is", 0xF0.toByte())
        ELog.hex(byteArrayOf(0x14, 0x33, 0x02, 0x15, 0xf2.toByte(), 0x0e))
        ELog.hex("info is", byteArrayOf(0x14, 0x33, 0x02, 0x15, 0xf2.toByte(), 0x0e))
    }

    @SuppressLint("SetTextI18n")
    fun test() {
        testTv?.text = "this is a test !!!!!!!!!"
    }
}