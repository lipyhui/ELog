package com.android.lipy.elog.adapter

import android.util.Log.VERBOSE
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_DEBUG_PRIORITY
import com.android.lipy.elog.interfaces.FormatStrategy
import com.android.lipy.elog.interfaces.LogAdapter
import com.android.lipy.elog.strategy.PrettyFormatStrategy

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
internal class AndroidLogAdapter : LogAdapter {

    private val mFormatStrategy: FormatStrategy
    private var mPriority: Int = DEFAULT_DEBUG_PRIORITY

    constructor() {
        mFormatStrategy = PrettyFormatStrategy.Builder().build()
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
