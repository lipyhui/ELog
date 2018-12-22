package com.android.lipy.elog.interfaces

import com.android.lipy.elog.DiskLogStrategy
import com.android.lipy.elog.LogcatLogStrategy

/**
 * Determines destination target for the logs such as Disk, Logcat etc.
 *
 * @see LogcatLogStrategy
 *
 * @see DiskLogStrategy
 */
interface LogStrategy {

    /**
     * This is invoked by ELog each time a log message is processed.
     * Interpret this method as last destination of the log in whole pipeline.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message.
     * @param message is the given message for the log message.
     */
    fun log(priority: Int, tag: String?, message: String)
}
