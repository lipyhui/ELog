package com.android.lipy.elog

import android.os.Process
import android.util.Log
import android.util.Log.*
import java.io.*
import java.net.UnknownHostException
import java.util.*
import kotlin.experimental.and


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

    fun toString(obj: Any?): String {
        if (obj == null) {
            return "null"
        }
        if (!obj.javaClass.isArray) {
            return obj.toString()
        }
        if (obj is BooleanArray) {
            return Arrays.toString(obj as BooleanArray?)
        }
        if (obj is ByteArray) {
            return Arrays.toString(obj as ByteArray?)
        }
        if (obj is CharArray) {
            return Arrays.toString(obj as CharArray?)
        }
        if (obj is ShortArray) {
            return Arrays.toString(obj as ShortArray?)
        }
        if (obj is IntArray) {
            return Arrays.toString(obj as IntArray?)
        }
        if (obj is LongArray) {
            return Arrays.toString(obj as LongArray?)
        }
        if (obj is FloatArray) {
            return Arrays.toString(obj as FloatArray?)
        }
        if (obj is DoubleArray) {
            return Arrays.toString(obj as DoubleArray?)
        }
        return if (obj is Array<*>) {
            Arrays.deepToString(obj as Array<*>?)
        } else "Couldn't find a correct type for the object"
    }

    fun byte2Hex(byte: Byte): String {
        return "${((byte.toInt() shr 4) and 0x0F).toString(16)}${(byte and 0x0F).toString(16)} "
    }
}// Hidden constructor.
