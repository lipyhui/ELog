package com.android.lipy.elogger

import com.android.lipy.elogger.interfaces.FormatStrategy
import com.android.lipy.elogger.interfaces.LogAdapter

/**
 * Android terminal log output implementation for [LogAdapter].
 *
 * Prints output to LogCat with pretty borders.
 *
 * <pre>
 * ┌──────────────────────────
 * │ Method stack history
 * ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 * │ Log message
 * └──────────────────────────
 * </pre>
 */
class AndroidLogAdapter : LogAdapter {

    private val formatStrategy: FormatStrategy

    constructor() {
        this.formatStrategy = PrettyFormatStrategy.Builder().build()
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
