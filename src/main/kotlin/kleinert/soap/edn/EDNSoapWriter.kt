package kleinert.soap.edn

import kleinert.soap.data.*
import java.io.Flushable
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
        ) {
            EDNSoapWriter(options, writer).encode(obj)
            if (writer is Flushable) writer.flush()
        }

        fun pprintln(
            obj: Any?,
            options: EDNSoapOptions = EDNSoapOptions.defaultOptions,
            writer: Appendable
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
            is String -> encodeString(obj)
            is Char -> encodeChar(obj)
            is Byte, is Short, is Int, is Long, is Float, is Double, is Ratio -> encodePredefinedNumberType(obj as Number)
            is BigInteger, is BigDecimal -> encodePredefinedNumberType(obj as Number)

            is Map.Entry<*, *> -> {
                encode(obj.key)
                writer.append(' ')
                encode(obj.value)
            }

            is IObj<*> -> {
                writer.append('^')
                encode(obj.meta)
                writer.append(' ')
                encode(obj.obj)
            }

            is PersistentList<*> -> if (!tryEncoder(obj)) encodePersistentList(obj)

            // User-defined encoder or as vector
            is Pair<*, *> -> if (!tryEncoder(obj)) encodeList(listOf(obj.first, obj.second))

            is ByteArray -> if (!tryEncoder(obj)) encode(obj.toList()) // User-defined encoder or as vector
            is ShortArray -> if (!tryEncoder(obj)) encode(obj.toList()) // User-defined encoder or as vector
            is IntArray -> if (!tryEncoder(obj)) encode(obj.toList()) // User-defined encoder or as vector
            is LongArray -> if (!tryEncoder(obj)) encode(obj.toList()) // User-defined encoder or as vector
            is FloatArray -> if (!tryEncoder(obj)) encode(obj.toList()) // User-defined encoder or as vector
            is DoubleArray -> if (!tryEncoder(obj)) encode(obj.toList()) // User-defined encoder or as vector
            is Array<*> -> if (!tryEncoder(obj)) encode(obj.toList()) // User-defined encoder or as vector
            is List<*> -> if (!tryEncoder(obj)) encodeList(obj) // Vector

            is Set<*> -> if (!tryEncoder(obj)) encodeSet(obj)
            is Map<*, *> -> if (!tryEncoder(obj)) encodeMap(obj)

            is Iterable<*> -> if (!tryEncoder(obj)) encodeOtherIterable(obj)
            is Sequence<*> -> if (!tryEncoder(obj)) encodeSequence(obj)
            is UUID -> writer.append("#uuid ").append(obj.toString())
            is Instant -> writer.append("#inst").append(obj.toString())
            is Keyword -> if (!tryEncoder(obj)) writer.append(obj.toString())
            is Symbol -> if (!tryEncoder(obj)) writer.append(obj.toString())
            else -> if (!tryEncoder(obj)) writer.append(obj.toString())
        }
    }

    private fun encodePredefinedNumberType(obj: Number) {
        when (obj) {
            is Byte, is Short, is Int, is Long, is Float, is Double, is Ratio -> writer.append(obj.toString())
            is BigInteger -> writer.append(obj.toString()).append('N')
            is BigDecimal -> writer.append(obj.toString()).append('M')
            else -> writer.append(obj.toString())
        }
        if (options.allowNumericSuffixes) {
            when (obj) {
                is Byte -> writer.append("_i8")
                is Short -> writer.append("_i16")
                is Int -> writer.append("_i32")
            }
        }
    }

    private fun encodeString(obj: String): Appendable {
        writer.append('"')
        for (code in obj) {
            when (code) {
                '\t' -> writer.append("\\t")
                '\b' -> writer.append("\\b")
                '\n' -> writer.append("\\n")
                '\r' -> writer.append("\\r")
                '\"' -> writer.append("\\\"")
                '\\' -> writer.append("\\\\")
                else -> writer.append(code)
            }
        }
        writer.append('"')
        return writer
    }

    private fun encodeChar(obj: Char) = writer.append('\\').append(obj)

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