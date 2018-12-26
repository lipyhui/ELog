package com.android.lipy.elog.adapter

import android.util.Log.VERBOSE
import com.android.lipy.elog.ELogConfigs
import com.android.lipy.elog.interfaces.FormatStrategy
import com.android.lipy.elog.interfaces.LogAdapter
import com.android.lipy.elog.strategy.CsvFormatStrategy

/**
 * This is used to saves log messages to the disk.
 * By default it uses [CsvFormatStrategy] to translates text message into CSV format.
 */
internal class DiskLogAdapter : LogAdapter {

    private val mFormatStrategy: FormatStrategy
    private var mPriority: Int = ELogConfigs.DEFAULT_DEBUG_PRIORITY

    constructor() {
        mFormatStrategy = CsvFormatStrategy.Builder().build()
    }

    constructor(formatStrategy: FormatStrategy) {
        mFormatStrategy = checkNotNull(formatStrategy)
    }

    constructor(formatStrategy: FormatStrategy, priority: Int) {
        mFormatStrategy = checkNotNull(formatStrategy)
        mPriority = if (priority < VERBOSE) {
            VERBOSE
        } else {
            priority
        }
    }

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return priority >= mPriority
    }

    override fun log(priority: Int, tag: String?, message: String) {
        mFormatStrategy.log(priority, tag, message)
    }
}
