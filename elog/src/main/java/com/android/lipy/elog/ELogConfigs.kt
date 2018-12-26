package com.android.lipy.elog

import com.android.lipy.elog.adapter.AndroidLogAdapter
import com.android.lipy.elog.adapter.DiskLogAdapter
import com.android.lipy.elog.interfaces.LogAdapter
import com.android.lipy.elog.interfaces.LogStrategy
import com.android.lipy.elog.interfaces.Printer
import com.android.lipy.elog.strategy.CsvFormatStrategy
import com.android.lipy.elog.strategy.PrettyFormatStrategy
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ELogConfigs private constructor(builder: Builder) {
    //custom printer
    var mPrinter: Printer

    //log adapters
    private val mLogAdapters: ArrayList<LogAdapter> = ArrayList()

    init {
        checkNotNull(builder)

        //printer
        mPrinter = if (builder.mPrinter == null) {
            ELogPrinter()
        } else {
            builder.mPrinter!!
        }

        //default logcat
        if (builder.mEnableLogcat) {
            val logcatFormatStrategy = PrettyFormatStrategy.Builder()
                    .tag(builder.mLogcatTag)
                    .methodCount(builder.mLogcatMethodCount)
                    .methodOffset(builder.mLogcatMethodOffset)
                    .showThreadInfo(builder.mLogcatShowThreadInfo)
                    .logStrategy(builder.mLogcatLogStrategy)
                    .build()

            //default logcat adapter
            mLogAdapters.add(AndroidLogAdapter(logcatFormatStrategy))
        }

        //default disk log
        if (builder.mEnableDiskLog) {
            val diskFormatStrategy = CsvFormatStrategy.Builder()
                    .tag(builder.mDiskTag)
                    .date(builder.mDiskDate)
                    .dateFormat(builder.mDiskDateFormat)
                    .logStrategy(builder.mDiskLogStrategy)
                    .build()

            //default disk adapter
            mLogAdapters.add(DiskLogAdapter(diskFormatStrategy))
        }

        //adapters
        if (mLogAdapters.size <= 0 && builder.mLogAdapters.size <= 0) {
            //default logcat adapter
            mLogAdapters.add(AndroidLogAdapter())
        } else {
            //custom log adapters
            mLogAdapters.addAll(builder.mLogAdapters)
        }

        //init printer
        mPrinter.clearLogAdapters()
        mPrinter.addAdapters(mLogAdapters)
    }

    class Builder constructor() {
        private var mTag: String? = DEFAULT_TAG

        //enable default logcat
        internal var mEnableLogcat: Boolean = true
        //enable default disk log
        internal var mEnableDiskLog: Boolean = false

        //custom printer
        internal var mPrinter: Printer? = null
        //custom log adapter
        val mLogAdapters: ArrayList<LogAdapter> = ArrayList()

        //logcat configs
        internal var mLogcatTag: String? = null
        internal var mLogcatMethodCount = DEFAULT_METHOD_COUNT
        internal var mLogcatMethodOffset = DEFAULT_METHOD_OFFSET
        internal var mLogcatShowThreadInfo = DEFAULT_IS_SHOW_THREAD_INFO
        internal var mLogcatLogStrategy: LogStrategy? = null

        //disk configs
        internal var mDiskTag: String? = null
        internal var mDiskDate: Date? = null
        internal var mDiskDateFormat: SimpleDateFormat? = null
        internal var mDiskLogStrategy: LogStrategy? = null

        fun setTag(tag: String?): Builder {
            mTag = tag
            return this
        }

        fun enableLogcat(): Builder {
            mEnableLogcat = true
            return this
        }

        fun enableDiskLog(): Builder {
            mEnableDiskLog = true
            return this
        }

        fun setPrinter(printer: Printer): Builder {
            mPrinter = printer
            return this
        }

        fun addLogAdapter(logAdapter: LogAdapter): Builder {
            mLogAdapters.add(logAdapter)
            return this
        }

        /****************************************************************
         *                   Default logcat configs
         ***************************************************************/
        fun setLogcatTag(logcatTag: String?): Builder {
            mLogcatTag = logcatTag
            return this
        }

        fun setLogcatMethodCount(logcatMethodCount: Int): Builder {
            mLogcatMethodCount = logcatMethodCount
            return this
        }

        fun setLogcatMethodOffset(logcatMethodOffset: Int): Builder {
            mLogcatMethodOffset = logcatMethodOffset
            return this
        }

        fun setLogcatShowThreadInfo(logcatShowThreadInfo: Boolean): Builder {
            mLogcatShowThreadInfo = logcatShowThreadInfo
            return this
        }

        fun setLogcatLogStrategy(logcatLogStrategy: LogStrategy?): Builder {
            mLogcatLogStrategy = logcatLogStrategy
            return this
        }

        /****************************************************************
         *                   Default disk log configs
         ***************************************************************/
        fun setDiskTag(diskTag: String?): Builder {
            mDiskTag = diskTag
            return this
        }

        fun setDiskDate(diskDate: Date?): Builder {
            mDiskDate = diskDate
            return this
        }

        fun setDiskDateFormat(diskDateFormat: SimpleDateFormat): Builder {
            mDiskDateFormat = diskDateFormat
            return this
        }

        fun setDiskLogStrategy(diskLogStrategy: LogStrategy): Builder {
            mDiskLogStrategy = diskLogStrategy
            return this
        }

        fun build(): ELogConfigs {
            if (mTag.isNullOrEmpty()) {
                mTag = DEFAULT_TAG
            }

            if (mLogcatTag.isNullOrEmpty()) {
                mLogcatTag = mTag
            }

            if (mDiskTag.isNullOrEmpty()) {
                mDiskTag = mTag
            }

            if (mLogcatMethodCount < 0) {
                mLogcatMethodCount = DEFAULT_METHOD_COUNT
            }

            if (mLogcatMethodOffset < 0) {
                mLogcatMethodOffset = DEFAULT_METHOD_OFFSET
            }

            return ELogConfigs(this)
        }
    }

    companion object {
        const val DEFAULT_TAG = "ELOG"

        //logcat
        const val DEFAULT_METHOD_COUNT = 2
        const val DEFAULT_METHOD_OFFSET = 0
        const val DEFAULT_IS_SHOW_THREAD_INFO = true

        //disk
        const val DEFAULT_DATA_FORMAT = "yyyy.MM.dd HH:mm:ss.SSS"
        const val DEFAULT_DIR = "ELog"
    }
}