package com.android.lipy.elog.interfaces

/**
 * A proxy interface to enable additional operations.
 * Contains all possible Log message usages.
 */
interface Printer {

    fun addAdapter(adapter: LogAdapter)

    fun t(tag: String?): Printer

    fun d(message: String, vararg args: Any)

    fun d(obj: Any)

    fun e(message: String, vararg args: Any)

    fun e(throwable: Throwable?, message: String, vararg args: Any)

    fun w(message: String, vararg args: Any)

    fun i(message: String, vararg args: Any)

    fun v(message: String, vararg args: Any)

    fun wtf(message: String, vararg args: Any)

    /**
     * Formats the given json content and print it
     */
    fun json(jsonStr: String?)

    /**
     * Formats the given xml content and print it
     */
    fun xml(xmlStr: String?)

    /**
     * byte to hex and print it
     */
    fun hex(message: String?, byte: Byte)

    /**
     * byte array to hex and print it
     */
    fun hex(message: String?, bytes: ByteArray)

    fun log(priority: Int, tag: String, msg: String?, throwable: Throwable?)

    fun clearLogAdapters()
}
