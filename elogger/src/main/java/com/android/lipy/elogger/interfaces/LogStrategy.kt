package com.android.lipy.elogger.interfaces

import com.android.lipy.elogger.DiskLogStrategy
import com.android.lipy.elogger.LogcatLogStrategy

/**
 * Determines destination target for the logs such as Disk, Logcat etc.
 *
 * @see LogcatLogStrategy
 *
 * @see DiskLogStrategy
 */
interface LogStrategy {

    /**
     * This is invoked by Logger each time a log message is processed.
     * Interpret this method as last destination of the log in whole pipeline.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message.
     * @param message is the given message for the log message.
     */
    fun log(priority: Int, tag: String?, message: String)
}
