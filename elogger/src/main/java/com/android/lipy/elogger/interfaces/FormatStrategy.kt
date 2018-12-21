package com.android.lipy.elogger.interfaces

import com.android.lipy.elogger.CsvFormatStrategy
import com.android.lipy.elogger.PrettyFormatStrategy

/**
 * Used to determine how messages should be printed or saved.
 *
 * @see PrettyFormatStrategy
 *
 * @see CsvFormatStrategy
 */
interface FormatStrategy {

    fun log(priority: Int, tag: String?, message: String)
}
