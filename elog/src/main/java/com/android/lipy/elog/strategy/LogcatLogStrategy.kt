package com.android.lipy.elog.strategy

import android.util.Log
import com.android.lipy.elog.LoggerPrinter.Companion.DEFAULT_TAG
import com.android.lipy.elog.interfaces.LogStrategy

/**
 * LogCat implementation for [LogStrategy]
 *
 * This simply prints out all logs to Logcat by using standard [Log] class.
 */
internal class LogcatLogStrategy : LogStrategy {

    override fun log(priority: Int, tag: String?, message: String) {
        var onceOnlyTag = tag
        checkNotNull(message)

        if (onceOnlyTag == null) {
            onceOnlyTag = DEFAULT_TAG
        }

        Log.println(priority, onceOnlyTag, message)
    }
}
