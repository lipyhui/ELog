package com.android.lipy.elog

import com.android.lipy.elog.ELog.init
import com.android.lipy.elog.adapter.AndroidLogAdapter
import com.android.lipy.elog.interfaces.FormatStrategy
import com.android.lipy.elog.interfaces.LogAdapter
import com.android.lipy.elog.interfaces.LogStrategy
import com.android.lipy.elog.interfaces.Printer
import com.android.lipy.elog.strategy.CsvFormatStrategy
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * ┌────────────────────────────────────────────
 * │ ELOG
 * ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 * │ Standard logging mechanism
 * ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 * │ But more pretty, simple and powerful
 * └────────────────────────────────────────────
 * </pre>
 *
 * <h3>How to use it</h3>
 * Initialize it first
 * <pre>
 * ELog.addLogAdapter(new AndroidLogAdapter());
 * </pre>
 *
 * And use the appropriate static ELog methods.
 *
 * <pre>
 * ELog.d("debug");
 * ELog.e("error");
 * ELog.w("warning");
 * ELog.v("verbose");
 * ELog.i("information");
 * ELog.wtf("What a Terrible Failure");
 * </pre>
 *
 * <h3>String format arguments are supported</h3>
 * <pre>
 * ELog.d("hello %s", "world");
 * </pre>
 *
 * <h3>Collections are support ed(only available for debug logs)</h3>
 * <pre>
 * ELog.d(MAP);
 * ELog.d(SET);
 * ELog.d(LIST);
 * ELog.d(ARRAY);
 * </pre>
 *
 * <h3>Json and Xml support (output will be in debug level)</h3>
 * <pre>
 * ELog.json(JSON_CONTENT);
 * ELog.xml(XML_CONTENT);
 * </pre>
 *
 * <h3>Byte and ByteArray to hex support (output will be in debug level)</h3>
 * <pre>
 * ELog.hex(Byte_CONTENT);
 * ELog.hex(ByteArray_CONTENT);
 * ELog.hex(STRING_CONTENT, Byte_CONTENT);
 * ELog.hex(STRING_CONTENT, ByteArray_CONTENT);
 * </pre>
 *
 * <h3>Customize ELog</h3>
 * Based on your needs, you can change the following settings:
 *
 * Different [LogAdapter]
 * Different [FormatStrategy]
 * Different [LogStrategy]
 *
 *
 * @see LogAdapter
 *
 * @see FormatStrategy
 *
 * @see LogStrategy
 */
object ELog {
    private var printer: Printer? = null

    /**
     * init printer
     *
     * @param configs Detailed configuration see [ELogConfigs]
     */
    fun init(configs: ELogConfigs) {
        printer = configs.mPrinter

        if (configs.mPrinter.getAdaptersSize() <= 0) {
            printer?.addAdapter(AndroidLogAdapter())
        }
    }

    fun clearLogAdapters() {
        //clear log adapters
        printer?.clearLogAdapters()

        //add default android log adapter
        printer?.addAdapter(AndroidLogAdapter())
    }

    /**
     * Given tag will be used as tag only once for this method call regardless of the tag that's been
     * set during initialization. After this invocation, the general tag that's been set will
     * be used for the subsequent log calls
     */
    fun t(tag: String): Printer {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        return printer!!.t(tag)
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    fun log(priority: Int, tag: String, message: String, throwable: Throwable) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.log(priority, tag, message, throwable)
    }

    fun d(message: String, vararg args: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.d(message, *args)
    }

    fun d(obj: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.d(obj)
    }

    fun e(message: String, vararg args: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.e(null, message, *args)
    }

    fun e(throwable: Throwable, message: String, vararg args: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.e(throwable, message, *args)
    }

    fun i(message: String, vararg args: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.i(message, *args)
    }

    fun v(message: String, vararg args: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.v(message, *args)
    }

    fun w(message: String, vararg args: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.w(message, *args)
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    fun wtf(message: String, vararg args: Any) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.wtf(message, *args)
    }

    /**
     * Formats the given json content and print it
     */
    fun json(json: String?) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.json(json)
    }

    /**
     * Formats the given xml content and print it
     */
    fun xml(xml: String?) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.xml(xml)
    }

    /**
     * byte to hex and print it
     */
    fun hex(byte: Byte) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.hex(null, byte)
    }

    /**
     * byte to hex and print it
     */
    fun hex(message: String, byte: Byte) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.hex(message, byte)
    }

    /**
     * byte array to hex and print it
     */
    fun hex(bytes: ByteArray) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.hex(null, bytes)
    }

    /**
     * byte array to hex and print it
     */
    fun hex(message: String, bytes: ByteArray) {
        if (printer == null || printer?.getAdaptersSize()!! <= 0) {
            init(ELogConfigs.Builder().build())
        }

        printer?.hex(message, bytes)
    }

}//no instance
