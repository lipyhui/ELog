package com.android.lipy.elogger

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.util.Log
import android.util.Log.*
import java.io.*
import java.net.UnknownHostException
import java.util.*


/**
 * Provides convenient methods to some common operations
 */
internal object Utils {

    /**
     * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
     * in unit tests.
     *
     * @return Stack trace in form of String
     */
    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    fun logLevel(value: Int): String {
        Log.ASSERT
        return when (value) {
            VERBOSE -> "V"
            DEBUG -> "D"
            INFO -> "I"
            WARN -> "W"
            ERROR -> "E"
            ASSERT -> "A"
            else -> "UNKNOWN"
        }
    }

    /**
     * 获取当前进程的名称，一般就是包名
     *
     * @return 返回进程的名字
     */
    fun getProcessName(): String {
        return try {
            val file = File("/proc/" + Process.myPid() + "/" + "cmdline")
            val mBufferedReader = BufferedReader(FileReader(file))
            val processName = mBufferedReader.readLine().replace(Regex("[^A-Za-z0-9._]"), "")
            mBufferedReader.close()
            processName
        } catch (e: Exception) {
            e.printStackTrace()
            "null".trim()
        }
    }

    fun getAppNameByPID(context: Context, pid: Int): String {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }


    fun toString(`object`: Any?): String {
        if (`object` == null) {
            return "null"
        }
        if (!`object`.javaClass.isArray) {
            return `object`.toString()
        }
        if (`object` is BooleanArray) {
            return Arrays.toString(`object` as BooleanArray?)
        }
        if (`object` is ByteArray) {
            return Arrays.toString(`object` as ByteArray?)
        }
        if (`object` is CharArray) {
            return Arrays.toString(`object` as CharArray?)
        }
        if (`object` is ShortArray) {
            return Arrays.toString(`object` as ShortArray?)
        }
        if (`object` is IntArray) {
            return Arrays.toString(`object` as IntArray?)
        }
        if (`object` is LongArray) {
            return Arrays.toString(`object` as LongArray?)
        }
        if (`object` is FloatArray) {
            return Arrays.toString(`object` as FloatArray?)
        }
        if (`object` is DoubleArray) {
            return Arrays.toString(`object` as DoubleArray?)
        }
        return if (`object` is Array<*>) {
            Arrays.deepToString(`object` as Array<*>?)
        } else "Couldn't find a correct type for the object"
    }
}// Hidden constructor.
