package com.android.lipy.elog.interfaces

import com.android.lipy.elog.AndroidLogAdapter
import com.android.lipy.elog.DiskLogAdapter

/**
 * Provides a common interface to emits logs through. This is a required contract for ELog.
 *
 * @see AndroidLogAdapter
 *
 * @see DiskLogAdapter
 */
interface LogAdapter {

    /**
     * Used to determine whether log should be printed out or not.
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message
     *
     * @return is used to determine if log should printed.
     * If it is true, it will be printed, otherwise it'll be ignored.
     */
    fun isLoggable(priority: Int, tag: String?): Boolean

    /**
     * Each log will use this pipeline
     *
     * @param priority is the log level e.g. DEBUG, WARNING
     * @param tag is the given tag for the log message.
     * @param message is the given message for the log message.
     */
    fun log(priority: Int, tag: String?, message: String)
}