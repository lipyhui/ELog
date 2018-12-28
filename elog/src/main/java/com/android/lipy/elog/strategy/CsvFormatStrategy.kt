package com.android.lipy.elog.strategy

import android.os.Environment
import android.os.HandlerThread
import com.android.lipy.elog.ELogConfigs.Companion.BYTES_KB
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_DATA_FORMAT
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_DIR
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_FILE_COUNT_MAX
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_FILE_SIZE_KB
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_SHOW_THREAD_INFO
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_SHOW_TIME_MS
import com.android.lipy.elog.ELogConfigs.Companion.DEFAULT_TAG
import com.android.lipy.elog.Utils
import com.android.lipy.elog.interfaces.FormatStrategy
import com.android.lipy.elog.interfaces.LogStrategy
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * CSV formatted file logging for Android.
 * Writes to CSV the following data:
 * epoch timestamp, ISO8601 timestamp (human-readable), log level, tag, log message.
 */
internal class CsvFormatStrategy private constructor(builder: Builder) : FormatStrategy {

    private val tag: String?
    private val showTimeMs: Boolean
    private val showThreadInfo: Boolean
    private val date: Date
    private val dateFormat: SimpleDateFormat
    private val logStrategy: LogStrategy

    init {
        checkNotNull(builder)

        tag = builder.tag
        showTimeMs = builder.showTimeMs
        showThreadInfo = builder.showThreadInfo
        date = builder.date!!
        dateFormat = builder.dateFormat!!
        logStrategy = builder.logStrategy!!
    }

    override fun log(priority: Int, onceOnlyTag: String?, msg: String) {
        var message = msg
        checkNotNull(message)

        val tag = formatTag(onceOnlyTag)

        date.time = System.currentTimeMillis()

        val builder = StringBuilder()

        if (showTimeMs) {
            // machine-readable date/time
            builder.append(date.time)
            builder.append(SEPARATOR)
        }
        // human-readable date/time
        builder.append(dateFormat.format(date))
        builder.append(SPACE)

        if (showThreadInfo) {
            //Thread
            builder.append(android.os.Process.myPid())
            builder.append(THREAD_SEPARATOR)
            builder.append(Thread.currentThread().name)
            builder.append(SEPARATOR)
            //Process
            builder.append(Utils.getProcessName())
            builder.append(SPACE)
        }

        // level
        builder.append(Utils.logLevel(priority))
        builder.append(SEPARATOR)
        // tag
        builder.append(tag)
        builder.append(MSG_SEPARATOR)

        // message
        if (message.contains(NEW_LINE!!)) {
            // a new line would break the CSV format, so we replace it here
            message = message.replace(NEW_LINE.toRegex(), NEW_LINE_REPLACEMENT)
        }
        builder.append(message)

        // new line
        builder.append(NEW_LINE)

        logStrategy.log(priority, tag, builder.toString())
    }

    private fun formatTag(tag: String?): String? {
        val commonTag = if (this.tag.isNullOrEmpty()) {
            DEFAULT_TAG
        } else this.tag

        return if (!tag.isNullOrEmpty() && !commonTag.equals(tag)) {
            "$commonTag-$tag"
        } else commonTag
    }

    class Builder internal constructor() {

        internal var tag: String? = DEFAULT_TAG
        internal var showTimeMs: Boolean = DEFAULT_SHOW_TIME_MS
        internal var showThreadInfo: Boolean = DEFAULT_SHOW_THREAD_INFO
        internal var date: Date? = null
        internal var dateFormat: SimpleDateFormat? = null
        internal var logStrategy: LogStrategy? = null

        private var path: String? = null
        private var fileSize: Int = DEFAULT_FILE_SIZE_KB * BYTES_KB
        private var fileCountMax: Int = DEFAULT_FILE_COUNT_MAX

        fun tag(value: String?): Builder {
            tag = value
            return this
        }

        fun showTimeMs(value: Boolean): Builder {
            showTimeMs = value
            return this
        }

        fun showThreadInfo(value: Boolean): Builder {
            showThreadInfo = value
            return this
        }

        fun date(value: Date?): Builder {
            date = value
            return this
        }

        fun dateFormat(value: SimpleDateFormat?): Builder {
            dateFormat = value
            return this
        }

        fun logStrategy(value: LogStrategy?): Builder {
            logStrategy = value
            return this
        }

        fun path(value: String?): Builder {
            path = value
            return this
        }

        fun fileSize(value: Int): Builder {
            fileSize = value
            return this
        }

        fun fileCountMax(value: Int): Builder {
            fileCountMax = value
            return this
        }

        fun build(): CsvFormatStrategy {
            if (date == null) {
                date = Date()
            }
            if (dateFormat == null) {
                dateFormat = SimpleDateFormat(DEFAULT_DATA_FORMAT, Locale.UK)
            }
            if (logStrategy == null) {
                if (path.isNullOrEmpty()) {
                    path = Environment.getExternalStorageDirectory().absolutePath
                }
                val folder = path + File.separatorChar + DEFAULT_DIR

                val ht = HandlerThread("AndroidFileELog.$folder")
                ht.start()

                if (fileSize < BYTES_KB) {
                    fileSize = DEFAULT_FILE_SIZE_KB * BYTES_KB
                }

                if (fileCountMax <= 0) {
                    fileCountMax = DEFAULT_FILE_COUNT_MAX
                }

                val handler = DiskLogStrategy.WriteHandler(ht.looper, folder, fileSize, fileCountMax)
                logStrategy = DiskLogStrategy(handler)
            }
            return CsvFormatStrategy(this)
        }
    }

    companion object {
        private val NEW_LINE = System.getProperty("line.separator")
        private const val NEW_LINE_REPLACEMENT = " <br> "
        private const val SEPARATOR = "/"
        private const val THREAD_SEPARATOR = "-"
        private const val MSG_SEPARATOR = ": "
        private const val SPACE = " "
    }
}
