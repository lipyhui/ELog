package com.android.lipy.elog

import android.app.Application
import android.os.Environment
import android.util.Log
import android.util.Log.VERBOSE
import com.android.lipy.elog.adapter.AndroidLogAdapter
import com.android.lipy.elog.adapter.DiskLogAdapter
import com.android.lipy.elog.interfaces.LogAdapter
import com.android.lipy.elog.interfaces.LogStrategy
import com.android.lipy.elog.interfaces.Printer
import com.android.lipy.elog.strategy.CsvFormatStrategy
import com.android.lipy.elog.strategy.DiskLogStrategy
import com.android.lipy.elog.strategy.PrettyFormatStrategy
import com.android.lipy.elog.strategy.LogcatLogStrategy
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 *  //init
 *
 * <h3>Customize</h3>
 *  <h4>If you want to customize on application at [Application];
 *  If you want to customize on Thread at [Thread]; </h4>
 * <pre>
 *  //configs
 *  val configs = ELogConfigs.Builder()
 *  .enableLogcat()                                         //Enable android logcat.Default [DEFAULT_ENABLE_LOGCAT]
 *  .enableDiskLog()                                        //Enable save log to disk.Default [DEFAULT_ENABLE_DISK_LOG]
 *  .setPrinter(CustomPrinter)                              //Setting up custom Printer. Default [ELogPrinter]
 *  .addLogAdapter(CustomLogAdapters)                       //Add custom LogAdapters. Default [AndroidLogAdapter] or [DiskLogAdapter]
 *  .setTag("MyTestConfigs")                                //Set default TAG, Use tag if you don't have set diskTag or logcatTag. Default [DEFAULT_TAG]
 *  //logcat configs
 *  .setLogcatTag("TestLogcatTag")                          //Set logcatTag. Default [DEFAULT_TAG]
 *  .setLogcatMethodCount(7)                                //Set the method count of logcat displays,. Default [DEFAULT_METHOD_COUNT]
 *  .setLogcatMethodOffset(2)                               //Set the method offset of logcat displays. Default [DEFAULT_METHOD_OFFSET]
 *  .setLogcatShowThreadInfo(false)                         //Set whether to show thread info. Default [DEFAULT_IS_SHOW_THREAD_INFO]
 *  .setLogcatLogStrategy(CustomLogStrategy)                //Setting up custom LogStrategy. Default [LogcatLogStrategy]
 *  //disk configs
 *  .setDiskTag("TestDiskTag")                              //Set diskTag. Default [DEFAULT_TAG]
 *  .setDiskDate(Date(2018, 1, 1, 24, 58))                  //Set diskTag. Default current system time
 *  .setDiskDateFormat(SimpleDateFormat("MM.dd HH:mm"))     //Set diskTag. Default [DEFAULT_DATA_FORMAT]
 *  .setDiskLogStrategy(CustomLogStrategy)                  //Setting up custom LogStrategy. Default [DiskLogStrategy]
 *  .setDiskPath(CustomDiskPath)                            //Setting up custom disk path, example "CustomDiskPath/ELog/logs_*.csv". Default "ExternalStorageDirectory/ELog/logs_*.csv"
 *  .build()
 *
 *  //init
 *  ELog.init(configs)
 *  </pre>
 *
 * @property mPrinter Printer
 * @property mLogAdapters ArrayList<LogAdapter>
 * @constructor
 */
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
            mLogAdapters.add(AndroidLogAdapter(logcatFormatStrategy, builder.mLogcatDebugPriority))
        }

        //default disk log
        if (builder.mEnableDiskLog) {
            val diskFormatStrategy = CsvFormatStrategy.Builder()
                    .tag(builder.mDiskTag)
                    .date(builder.mDiskDate)
                    .dateFormat(builder.mDiskDateFormat)
                    .logStrategy(builder.mDiskLogStrategy)
                    .diskPath(builder.mDiskPath)
                    .build()

            //default disk adapter
            mLogAdapters.add(DiskLogAdapter(diskFormatStrategy, builder.mDiskDebugPriority))
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
        internal var mEnableLogcat: Boolean = DEFAULT_ENABLE_LOGCAT
        //enable default disk log
        internal var mEnableDiskLog: Boolean = DEFAULT_ENABLE_DISK_LOG

        //custom printer
        internal var mPrinter: Printer? = null
        //custom log adapter
        val mLogAdapters: ArrayList<LogAdapter> = ArrayList()

        //logcat configs
        internal var mLogcatTag: String? = null
        internal var mLogcatDebugPriority = DEFAULT_DEBUG_PRIORITY
        internal var mLogcatMethodCount = DEFAULT_METHOD_COUNT
        internal var mLogcatMethodOffset = DEFAULT_METHOD_OFFSET
        internal var mLogcatShowThreadInfo = DEFAULT_IS_SHOW_THREAD_INFO
        internal var mLogcatLogStrategy: LogStrategy? = null

        //disk configs
        internal var mDiskTag: String? = null
        internal var mDiskDebugPriority = DEFAULT_DEBUG_PRIORITY
        internal var mDiskDate: Date? = null
        internal var mDiskDateFormat: SimpleDateFormat? = null
        internal var mDiskLogStrategy: LogStrategy? = null
        internal var mDiskPath: String? = null

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

        fun setLogcatDebugPriority(priority: Int): Builder {
            mLogcatDebugPriority = priority
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

        fun setDiskDebugPriority(priority: Int): Builder {
            mDiskDebugPriority = priority
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

        fun setDiskPath(diskPath: String?): Builder {
            mDiskPath = diskPath
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

            if (mLogcatDebugPriority < VERBOSE) {
                mLogcatDebugPriority = VERBOSE
            }

            if (mDiskDebugPriority < VERBOSE) {
                mDiskDebugPriority = VERBOSE
            }

            if (mLogcatMethodCount < 0) {
                mLogcatMethodCount = DEFAULT_METHOD_COUNT
            }

            if (mLogcatMethodOffset < 0) {
                mLogcatMethodOffset = DEFAULT_METHOD_OFFSET
            }

            if (mDiskPath.isNullOrEmpty()) {
                mDiskPath = Environment.getExternalStorageDirectory().absolutePath
            }

            return ELogConfigs(this)
        }
    }

    companion object {
        internal const val DEFAULT_TAG = "ELOG"

        //debug priority
        internal const val DEFAULT_DEBUG_PRIORITY = VERBOSE

        //log switch
        internal const val DEFAULT_ENABLE_LOGCAT = true
        internal const val DEFAULT_ENABLE_DISK_LOG = false

        //logcat
        internal const val DEFAULT_METHOD_COUNT = 2
        internal const val DEFAULT_METHOD_OFFSET = 0
        internal const val DEFAULT_IS_SHOW_THREAD_INFO = true

        //disk
        internal const val DEFAULT_DATA_FORMAT = "yyyy.MM.dd HH:mm:ss.SSS"
        internal const val DEFAULT_DIR = "ELog"

        //stop debug log
        const val DEBUG_STOP = Int.MAX_VALUE
    }
}