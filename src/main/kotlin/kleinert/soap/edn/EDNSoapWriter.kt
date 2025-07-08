package kleinert.soap.edn

import kleinert.soap.data.PersistentList
import kleinert.soap.data.Ratio
import java.io.Flushable
import java.io.Writer
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

class EDNSoapWriter private constructor(private val options: EDNSoapOptions, private val writer: Appendable) {

    companion object {
        fun pprintToString(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.defaultOptions): String {
            val writer: StringBuilder = StringBuilder()
            EDNSoapWriter(options, writer).encode(obj)
            return writer.toString()
        }

        fun pprint(
            obj: Any?,
            options: EDNSoapOptions = EDNSoapOptions.defaultOptions,
            writer: Appendable
        ) { EDNSoapWriter(options, writer).encode(obj)
        if (writer is Flushable) writer.flush()}

        fun pprintln(
            obj: Any?,
            options: EDNSoapOptions = EDNSoapOptions.defaultOptions,
            writer: Appendable = StringBuilder()
        ) {
            EDNSoapWriter(options, writer).encode(obj)
            writer.append('\n')
            if (writer is Flushable) writer.flush()
        }

    }

    private fun tryEncoder(obj: Any): Boolean {
        val encoder = options.ednClassEncoders[obj.javaClass] ?: return false
        val (prefix, output) = encoder(obj as Any?) ?: return false
        writer.append('#').append(prefix).append(' ')
        encode(output)
        return true
    }

    fun encode(obj: Any?) {
        when (obj) {
            null -> writer.append("nil")
            true -> writer.append("true")
            false -> writer.append("false")
            is String -> writer.append(encodeString(obj))
            is Char -> writer.append(encodeChar(obj))
            is Byte, is Short, is Int, is Long, is Float, is Double, is Ratio -> writer.append(
                encodePredefinedNumberType(obj as Number)
            )

            is BigInteger, is BigDecimal -> writer.append(encodePredefinedNumberType(obj as Number))
            is Map.Entry<*, *> -> writer.append("${encode(obj.key)} ${encode(obj.value)}")
            is PersistentList<*> -> if (!tryEncoder(obj)) encodePersistentList(obj)
            is ByteArray -> if (!tryEncoder(obj)) encode(obj.toList())
            is ShortArray -> if (!tryEncoder(obj)) encode(obj.toList())
            is IntArray -> if (!tryEncoder(obj)) encode(obj.toList())
            is LongArray -> if (!tryEncoder(obj)) encode(obj.toList())
            is FloatArray -> if (!tryEncoder(obj)) encode(obj.toList())
            is DoubleArray -> if (!tryEncoder(obj)) encode(obj.toList())
            is Array<*> -> if (!tryEncoder(obj)) encode(obj.toList())
            is List<*> -> if (!tryEncoder(obj)) encodeList(obj)
            is Set<*> -> if (!tryEncoder(obj)) encodeSet(obj)
            is Map<*, *> -> if (!tryEncoder(obj)) encodeMap(obj)
            is Iterable<*> -> if (!tryEncoder(obj)) encodeOtherIterable(obj)
            is Sequence<*> -> if (!tryEncoder(obj)) encodeSequence(obj)
            is UUID -> writer.append("#uuid ").append(obj.toString())
            is Instant -> writer.append("#inst").append(obj.toString())
            else -> if (!tryEncoder(obj)) obj.toString()
        }
    }

    private fun encodePredefinedNumberType(obj: Number): String {
        val temp = StringBuilder()
        when (obj) {
            is Byte, is Short, is Int, is Long, is Float, is Double, is Ratio -> temp.append(obj)
            is BigInteger -> temp.append(obj).append('N')
            is BigDecimal -> temp.append(obj).append('M')
            else -> temp.append(obj)
        }
        if (options.allowNumericSuffixes) {
            when (obj) {
                is Byte -> temp.append("_i8")
                is Short -> temp.append("_i16")
                is Int -> temp.append("_i32")
            }
        }
        return temp.toString()
    }

    private fun encodeString(obj: String) = StringBuilder().append('"').append(obj).append('"').toString()

    private fun encodeChar(obj: Char) = StringBuilder().append('\\').append(obj).toString()

    private fun encodeSequence(obj: Sequence<*>) = obj.joinTo(
        writer,
        separator = options.decodingSequenceSeparator,
        limit = options.sequenceElementLimit,
        prefix = "(",
        postfix = ")",
        transform = { encode(it);"" },
    )

    private fun encodePersistentList(obj: PersistentList<*>) = obj.joinTo(
        writer,
        separator = options.decodingSequenceSeparator,
        prefix = "(",
        postfix = ")",
        transform = { encode(it);"" },
    )

    private fun encodeOtherIterable(obj: Iterable<*>) = obj.joinTo(
        writer,
        separator = options.decodingSequenceSeparator,
        prefix = "(",
        postfix = ")",
        transform = { encode(it);"" },
    )

    private fun encodeList(obj: List<*>) = obj.joinTo(
        writer,
        separator = options.decodingSequenceSeparator,
        prefix = "[",
        postfix = "]",
        transform = { encode(it);"" },
    )

    private fun encodeSet(obj: Set<*>) = obj.joinTo(
        writer,
        separator = options.decodingSequenceSeparator,
        prefix = "#{",
        postfix = "}",
        transform = { encode(it);"" },
    )

    private fun encodeMap(obj: Map<*, *>) = obj.map { it }.joinTo(
        writer,
        separator = options.decodingSequenceSeparator,
        prefix = "{",
        postfix = "}",
        transform = { encode(it);"" },
    )
}