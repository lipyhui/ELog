package com.android.lipy.elog.interfaces

import com.android.lipy.elog.strategy.CsvFormatStrategy
import com.android.lipy.elog.strategy.PrettyFormatStrategy

/**
 * Used to determine how messages should be printed or saved.
 *
 * @see PrettyFormatStrategy
 *
 * @see CsvFormatStrategy
 */
interface FormatStrategy {

    fun log(priority: Int, onceOnlyTag: String?, msg: String)
}
