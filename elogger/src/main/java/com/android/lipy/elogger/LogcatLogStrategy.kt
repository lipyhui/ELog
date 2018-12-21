package com.android.lipy.elogger

import android.util.Log
import com.android.lipy.elogger.interfaces.LogStrategy

/**
 * LogCat implementation for [LogStrategy]
 *
 * This simply prints out all logs to Logcat by using standard [Log] class.
 */
class LogcatLogStrategy : LogStrategy {

    override fun log(priority: Int, tags: String?, message: String) {
        var tag = tags
        checkNotNull(message)

        if (tag == null) {
            tag = DEFAULT_TAG
        }

        Log.println(priority, tag, message)
    }

    companion object {

        internal const val DEFAULT_TAG = "NO_TAG"
    }
}
