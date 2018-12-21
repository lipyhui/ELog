package com.android.lipy.elogger

import android.os.Environment
import android.os.HandlerThread
import com.android.lipy.elogger.interfaces.FormatStrategy
import com.android.lipy.elogger.interfaces.LogStrategy
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * CSV formatted file logging for Android.
 * Writes to CSV the following data:
 * epoch timestamp, ISO8601 timestamp (human-readable), log level, tag, log message.
 */
class CsvFormatStrategy private constructor(builder: Builder) : FormatStrategy {

    private val date: Date
    private val dateFormat: SimpleDateFormat
    private val logStrategy: LogStrategy
    private val tag: String?

    init {
        checkNotNull(builder)

        date = builder.date!!
        dateFormat = builder.dateFormat!!
        logStrategy = builder.logStrategy!!
        tag = builder.tag
    }

    override fun log(priority: Int, onceOnlyTag: String?, msg: String) {
        var message = msg
        checkNotNull(message)

        val tag = formatTag(onceOnlyTag)

        date.time = System.currentTimeMillis()

        val builder = StringBuilder()

        // machine-readable date/time
        builder.append(java.lang.Long.toString(date.time))

        // human-readable date/time
        builder.append(SEPARATOR)
        builder.append(dateFormat.format(date))

        //Thread
        builder.append(SPACE)
        builder.append(android.os.Process.myPid())
        builder.append(THREAD_SEPARATOR)
        builder.append(Thread.currentThread().name)
        //Process
        builder.append(SEPARATOR)
        builder.append(Utils.getProcessName())

        // level
        builder.append(SPACE)
        builder.append(Utils.logLevel(priority))
        // tag
        builder.append(SEPARATOR)
        builder.append(tag)

        // message
        if (message.contains(NEW_LINE!!)) {
            // a new line would break the CSV format, so we replace it here
            message = message.replace(NEW_LINE.toRegex(), NEW_LINE_REPLACEMENT)
        }
        builder.append(MSG_SEPARATOR)
        builder.append(message)

        // new line
        builder.append(NEW_LINE)

        logStrategy.log(priority, tag, builder.toString())
    }

    private fun formatTag(tag: String?): String? {
        return if (!tag.isNullOrEmpty() && !this.tag.equals(tag)) {
            this.tag + "-" + tag
        } else this.tag
    }

    class Builder internal constructor() {

        internal var date: Date? = null
        internal var dateFormat: SimpleDateFormat? = null
        internal var logStrategy: LogStrategy? = null
        internal var tag: String? = "E_LOGGER"

        fun date(`val`: Date?): Builder {
            date = `val`
            return this
        }

        fun dateFormat(`val`: SimpleDateFormat?): Builder {
            dateFormat = `val`
            return this
        }

        fun logStrategy(`val`: LogStrategy?): Builder {
            logStrategy = `val`
            return this
        }

        fun tag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        fun build(): CsvFormatStrategy {
            if (date == null) {
                date = Date()
            }
            if (dateFormat == null) {
                dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK)
            }
            if (logStrategy == null) {
                val diskPath = Environment.getExternalStorageDirectory().absolutePath
                val folder = diskPath + File.separatorChar + "eLogger"

                val ht = HandlerThread("AndroidFileELogger.$folder")
                ht.start()
                val handler = DiskLogStrategy.WriteHandler(ht.looper, folder, MAX_BYTES)
                logStrategy = DiskLogStrategy(handler)
            }
            return CsvFormatStrategy(this)
        }

        companion object {
            private const val MAX_BYTES = 500 * 1024 // 500K averages to a 4000 lines per file
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
