package com.android.lipy.elog.adapter

import com.android.lipy.elog.strategy.CsvFormatStrategy
import com.android.lipy.elog.interfaces.FormatStrategy
import com.android.lipy.elog.interfaces.LogAdapter

/**
 * This is used to saves log messages to the disk.
 * By default it uses [CsvFormatStrategy] to translates text message into CSV format.
 */
class DiskLogAdapter : LogAdapter {

    private val formatStrategy: FormatStrategy

    constructor() {
        formatStrategy = CsvFormatStrategy.Builder().build()
    }

    constructor(formatStrategy: FormatStrategy) {
        this.formatStrategy = checkNotNull(formatStrategy)
    }

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return true
    }

    override fun log(priority: Int, tag: String?, message: String) {
        formatStrategy.log(priority, tag, message)
    }
}
