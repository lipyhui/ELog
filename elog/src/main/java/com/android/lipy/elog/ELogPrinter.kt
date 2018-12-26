package com.android.lipy.elog

import android.util.Log.*
import com.android.lipy.elog.interfaces.LogAdapter
import com.android.lipy.elog.interfaces.Printer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

internal class ELogPrinter : Printer {
    /**
     * Provides one-time used tag for the log message
     */
    private val localTag = ThreadLocal<String>()

    private val logAdapters = ArrayList<LogAdapter>()

    /**
     * @return the appropriate tag based on local or global
     */
    private val tag: String?
        get() {
            val tag = localTag.get()
            if (tag != null) {
                localTag.remove()
                return tag
            }

            return null
        }

    override fun t(tag: String?): Printer {
        if (tag != null) {
            localTag.set(tag)
        }
        return this
    }

    override fun d(message: String, vararg args: Any) {
        log(DEBUG, null, message, *args)
    }

    override fun d(obj: Any) {
        log(DEBUG, null, Utils.toString(obj))
    }

    override fun e(message: String, vararg args: Any) {
        e(null, message, *args)
    }

    override fun e(throwable: Throwable?, message: String, vararg args: Any) {
        log(ERROR, throwable, message, *args)
    }

    override fun w(message: String, vararg args: Any) {
        log(WARN, null, message, *args)
    }

    override fun i(message: String, vararg args: Any) {
        log(INFO, null, message, *args)
    }

    override fun v(message: String, vararg args: Any) {
        log(VERBOSE, null, message, *args)
    }

    override fun wtf(message: String, vararg args: Any) {
        log(ASSERT, null, message, *args)
    }

    override fun json(jsonStr: String?) {
        var json = jsonStr
        if (json.isNullOrEmpty()) {
            d("Empty/Null json content")
            return
        }
        try {
            json = json!!.trim { it <= ' ' }
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                val message = jsonObject.toString(JSON_INDENT)
                d(message)
                return
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                val message = jsonArray.toString(JSON_INDENT)
                d(message)
                return
            }
            e("Invalid Json")
        } catch (e: JSONException) {
            e("Invalid Json")
        }
    }

    override fun xml(xmlStr: String?) {
        if (xmlStr.isNullOrEmpty()) {
            d("Empty/Null xml content")
            return
        }
        try {
            val xmlInput = StreamSource(StringReader(xmlStr))
            val xmlOutput = StreamResult(StringWriter())
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            transformer.transform(xmlInput, xmlOutput)
            d(xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n"))
        } catch (e: TransformerException) {
            e("Invalid xml")
        }
    }

    override fun hex(byte: Byte?) {
        if (byte == null) {
            d("Null Byte")
        } else {
            log(DEBUG, null, Utils.byte2Hex(byte).toUpperCase())
        }
    }

    override fun hex(message: String?, byte: Byte?) {
        when {
            message.isNullOrEmpty() && byte == null -> {
                d("Null byte")
            }

            byte == null -> {
                d("$message Null byte content")
            }

            else -> {
                message?.let {
                    log(DEBUG, null, "$message ${Utils.byte2Hex(byte).toUpperCase()}")
                    return
                }

                log(DEBUG, null, Utils.byte2Hex(byte).toUpperCase())
            }
        }
    }

    override fun hex(bytes: ByteArray?) {
        bytes?.let {
            val strBuff = StringBuffer()
            strBuff.append("[")
            for (byte in bytes) {
                strBuff.append(Utils.byte2Hex(byte))
            }
            strBuff.setCharAt(strBuff.length - 1, ']')

            log(DEBUG, null, strBuff.toString().toUpperCase())
            return
        }

        d("Null ByteArray")
    }

    override fun hex(message: String?, bytes: ByteArray?) {
        val strBuff = StringBuffer()
        if (bytes == null) {
            strBuff.append("Null ByteArray")
        } else {
            strBuff.append("[")
            for (byte in bytes) {
                strBuff.append(Utils.byte2Hex(byte))
            }
            strBuff.setCharAt(strBuff.length - 1, ']')
        }

        message?.let {
            log(DEBUG, null, "$message ${strBuff.toString().toUpperCase()}")
            return
        }

        log(DEBUG, null, strBuff.toString().toUpperCase())
    }

    @Synchronized
    override fun log(priority: Int,
                     tag: String?,
                     msg: String?,
                     throwable: Throwable?) {
        var message = msg
        if (throwable != null && message != null) {
            message += " : " + Utils.getStackTraceString(throwable)
        }
        if (throwable != null && message == null) {
            message = Utils.getStackTraceString(throwable)
        }
        if (message.isNullOrEmpty()) {
            message = "Empty/NULL log message"
        }

        for (adapter in logAdapters) {
            if (adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, message!!)
            }
        }
    }

    override fun clearLogAdapters() {
        logAdapters.clear()
    }

    override fun addAdapter(adapter: LogAdapter) {
        logAdapters.add(checkNotNull(adapter))
    }

    override fun addAdapters(adapters: ArrayList<LogAdapter>) {
        logAdapters.addAll(adapters)
    }

    override fun getAdaptersSize(): Int {
        return logAdapters.size
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    @Synchronized
    private fun log(priority: Int,
                    throwable: Throwable?,
                    msg: String,
                    vararg args: Any) {
        checkNotNull(msg)

        val tag = tag
        val message = createMessage(msg, *args)
        log(priority, tag, message, throwable)
    }

    private fun createMessage(message: String, vararg args: Any): String {
        return if (args.isEmpty()) message else String.format(message, *args)
    }

    companion object {

        /**
         * It is used for json pretty print
         */
        private const val JSON_INDENT = 2
    }
}
