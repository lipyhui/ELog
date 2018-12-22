package com.example.logger

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.lipy.elogger.AndroidLogAdapter
import com.android.lipy.elogger.DiskLogAdapter
import com.android.lipy.elogger.Logger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()

        //init
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.addLogAdapter(DiskLogAdapter())

        Logger.hex(0x08)
        Logger.hex(0xE6.toByte())
        Logger.hex(0xF0.toByte())
        Logger.hex("type is", 0xF0.toByte())
        Logger.hex(byteArrayOf(0x14, 0x33, 0x02, 0x15, 0xf2.toByte(), 0x0e))
        Logger.hex("info is", byteArrayOf(0x14, 0x33, 0x02, 0x15, 0xf2.toByte(), 0x0e))
    }

    @SuppressLint("SetTextI18n")
    fun test() {
        testTv?.text = "this is a test !!!!!!!!!"
    }
}