package kleinert.soap.edn

import kleinert.soap.data.*
import java.io.Flushable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

/**
 * TODO
 *
 * @author Armin Kleinert
 */
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
        var encoder: ((Any) -> Pair<String, Any?>?)? = null
        for ((jClass, enc) in options.ednClassEncoders) {
            if (jClass.isInstance(obj)) {
                encoder = enc
                break
            }
        }
        if (encoder == null)
            return false
        val (prefix, output) = encoder(obj) ?: return false
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
            is Char32 -> encodeChar32(obj)
            is Byte, is Short, is Int, is Long, is Ratio -> encodePredefinedNumberType(obj as Number)
            is Float -> encodeFloat(obj)
            is Double -> encodeDouble(obj)
            is Complex -> encodeComplex(obj)
            is BigInteger, is BigDecimal -> encodePredefinedNumberType(obj as Number)

            is IObj<*> -> {
                writer.append('^')
                encode(obj.meta)
                writer.append(' ')
                encode(obj.obj)
            }

            is PersistentList<*> -> if (!tryEncoder(obj)) encodePersistentList(obj)

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
            is UUID -> writer.append("#uuid \"").append(obj.toString()).append('"')

            is Instant -> writer.append("#inst \"").append(obj.toString()).append('"')
            is Keyword -> if (!tryEncoder(obj)) writer.append(obj.toString())
            is Symbol -> if (!tryEncoder(obj)) writer.append(obj.toString())
            else -> if (!tryEncoder(obj)) writer.append(obj.toString())
        }
    }

    private fun encodePredefinedNumberType(obj: Number) {
        when (obj) {
            is Byte, is Short, is Int, is Long, is Ratio -> writer.append(obj.toString())
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

    private fun encodeFloat(obj: Float) {
        if (obj.isNaN()) writer.append("##NaN")
        else if (obj == Float.POSITIVE_INFINITY) writer.append("##INF")
        else if (obj == Float.NEGATIVE_INFINITY) writer.append("##-INF")
        else writer.append(obj.toString())
    }

    private fun encodeDouble(obj: Double) {
        if (obj.isNaN()) writer.append("##NaN")
        else if (obj == Double.POSITIVE_INFINITY) writer.append("##INF")
        else if (obj == Double.NEGATIVE_INFINITY) writer.append("##-INF")
        else writer.append(obj.toString())
    }

    private fun encodeComplex(obj: Complex) {
        if (options.allowComplexNumberLiterals) {
            // Maybe some special handling?
            writer.append(obj.toString())
        } else {
            // Maybe some special handling?
            writer.append(obj.toString())
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

    private fun encodeChar(obj: Char) = when (obj) {
        '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '\\', '^', '_', '`',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '|', '~', '§', '°', '´', '€' -> writer.append('\\').append(obj)

        '\n' -> writer.append("\\newline")
        ' ' -> writer.append("\\space")
        '\t' -> writer.append("\\tab")
        '\b' -> writer.append("\\backspace")
        12.toChar() -> writer.append("\\formfeed")
        '\r' -> writer.append("\\return")

        else -> writer.append(String.format("\\u%04x", obj.code))
    }

    private fun encodeChar32(obj: Char32) {
        if (!options.allowDispatchChars) {
            writer.append('"').append(obj.toString()).append('"')
            return
        }

        when (obj.code) {
            '!'.code, '"'.code, '#'.code, '$'.code, '%'.code, '&'.code, '\''.code, '('.code, ')'.code, '*'.code, '+'.code,
            ','.code, '-'.code, '.'.code, '/'.code,
            '0'.code, '1'.code, '2'.code, '3'.code, '4'.code, '5'.code, '6'.code, '7'.code, '8'.code, '9'.code,
            ':'.code, ';'.code, '<'.code, '='.code, '>'.code, '?'.code, '@'.code,
            'A'.code, 'B'.code, 'C'.code, 'D'.code, 'E'.code, 'F'.code, 'G'.code, 'H'.code, 'I'.code, 'J'.code, 'K'.code,
            'L'.code, 'M'.code, 'N'.code, 'O'.code, 'P'.code, 'Q'.code, 'R'.code, 'S'.code, 'T'.code, 'U'.code, 'V'.code,
            'W'.code, 'X'.code, 'Y'.code, 'Z'.code,
            '\\'.code, '^'.code, '_'.code, '`'.code,
            'a'.code, 'b'.code, 'c'.code, 'd'.code, 'e'.code, 'f'.code, 'g'.code, 'h'.code, 'i'.code, 'j'.code, 'k'.code,
            'l'.code, 'm'.code, 'n'.code, 'o'.code, 'p'.code, 'q'.code, 'r'.code, 's'.code, 't'.code, 'u'.code, 'v'.code,
            'w'.code, 'x'.code, 'y'.code, 'z'.code,
            '|'.code, '~'.code, '§'.code, '°'.code, '´'.code, '€'.code
            -> writer.append("#\\").append(obj.toString())

            '\n'.code -> writer.append("#\\newline")
            ' '.code -> writer.append("#\\space")
            '\t'.code -> writer.append("#\\tab")
            '\b'.code -> writer.append("#\\backspace")
            12 -> writer.append("#\\formfeed")
            '\r'.code -> writer.append("#\\return")

            else -> writer.append(String.format("#\\u%08x", obj.code))
        }
    }

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
        transform = {
            encode(it.key)
            writer.append(' ')
            encode(it.value)
            ""
        },
    )
}