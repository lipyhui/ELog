package com.example.elog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.lipy.elog.ELog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test()

//        ELog.Builder()

        //init

        ELog.t("TEST_TAG").hex(0x08)
        ELog.hex("bytes is", byteArrayOf(0x14, 0x33, 0x02, 0x15, 0xf2.toByte(), 0x0e))
        ELog.v("%d, %s, %6f : %3f", 1, "test", 5F, 10F)

        startService(Intent(this, MyService::class.java))
    }

    @SuppressLint("SetTextI18n")
    fun test() {
        testTv?.text = "this is a test !!!!!!!!!"
    }
}