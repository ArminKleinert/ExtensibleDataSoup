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
            writer.append(EDNSoapWriter(options, writer).encode(obj))
            return writer.toString()
        }

        fun pprint(
            obj: Any?,
            options: EDNSoapOptions = EDNSoapOptions.defaultOptions,
            writer: Appendable
        ) {
            writer.append(EDNSoapWriter(options, writer).encode(obj))
            if (writer is Flushable) writer.flush()
        }

        fun pprintln(
            obj: Any?,
            options: EDNSoapOptions = EDNSoapOptions.defaultOptions,
            writer: Appendable
        ) {
            writer.append(EDNSoapWriter(options, writer).encode(obj))
            writer.append('\n')
            if (writer is Flushable) writer.flush()
        }
    }

    private val indent = StringBuilder()
    private var level = 0
    private var column = 0

    private fun breakByLength(columnAdvance: Int): Boolean {
        var didBreak = false
        if (column + columnAdvance > options.encoderMaxColumn) {
            doBreak()
            didBreak = true
        }
        column += columnAdvance
        return didBreak
    }

    private fun doBreak() {
        indent.clear()
        for (i in 1..<level) indent.append(options.encoderLineIndent)
        options.encoderLineIndent.repeat(8)
        column = 0
        writer.append('\n').append(indent)
    }

    private fun tryEncoder(obj: Any): CharSequence? {
        var encoder: ((Any) -> Pair<String, Any?>?)? = null
        for ((jClass, enc) in options.ednClassEncoders) {
            if (jClass.isInstance(obj)) {
                encoder = enc
                break
            }
        }
        if (encoder == null)
            return null
        val (prefix, output) = encoder(obj) ?: return null
        writer.append('#').append(prefix).append(' ')
        return encode(output)
    }

    fun encode(obj: Any?): CharSequence {
        return when (obj) {
            null -> encodeNull()
            true -> encodeBool(true)
            false -> encodeBool(false)

            is String -> encodeString(obj)
            is Keyword -> tryEncoder(obj) ?: encodeKeyword(obj)
            is Symbol -> tryEncoder(obj) ?: encodeSymbol(obj)

            is PersistentList<*> -> tryEncoder(obj) ?: encodePersistentList(obj) // List, not vector

            is ByteArray -> tryEncoder(obj) ?: encode(obj.toList()) // User-defined encoder or as vector
            is ShortArray -> tryEncoder(obj) ?: encode(obj.toList()) // User-defined encoder or as vector
            is IntArray -> tryEncoder(obj) ?: encode(obj.toList()) // User-defined encoder or as vector
            is LongArray -> tryEncoder(obj) ?: encode(obj.toList()) // User-defined encoder or as vector
            is FloatArray -> tryEncoder(obj) ?: encode(obj.toList()) // User-defined encoder or as vector
            is DoubleArray -> tryEncoder(obj) ?: encode(obj.toList()) // User-defined encoder or as vector
            is Array<*> -> tryEncoder(obj) ?: encode(obj.toList()) // User-defined encoder or as vector
            is List<*> -> tryEncoder(obj) ?: encodeVector(obj) // Vector

            is Set<*> -> tryEncoder(obj) ?: encodeSet(obj)
            is Map<*, *> -> tryEncoder(obj) ?: encodeMap(obj)

            is Iterable<*> -> tryEncoder(obj) ?: encodeOtherIterable(obj)
            is Sequence<*> -> tryEncoder(obj) ?: encodeSequence(obj)

            is Char -> encodeChar(obj)
            is Char32 -> encodeChar32(obj)
            is Byte, is Short, is Int, is Long, is Ratio -> encodePredefinedNumberType(obj as Number)
            is Float -> encodeFloat(obj)
            is Double -> encodeDouble(obj)
            is Complex -> encodeComplex(obj)
            is BigInteger, is BigDecimal -> encodePredefinedNumberType(obj as Number)

            is IObj<*> -> encodeIObj(obj)

            is UUID -> encodeUuid(obj)
            is Instant -> encodeInstant(obj)

            else -> tryEncoder(obj) ?: obj.toString()
        }
    }


    private fun encodeIObj(obj: IObj<*>): CharSequence {
        writer.append('^')
        writer.append(encode(obj.meta))
        writer.append(' ')
        return encode(obj.obj)
    }

    private fun encodeKeyword(obj: Keyword): CharSequence = obj.toString()

    private fun encodeSymbol(obj: Symbol): CharSequence = obj.toString()

    private fun encodeUuid(obj: UUID): CharSequence = "#uuid \"$obj\""

    private fun encodeInstant(obj: Instant): CharSequence = "#inst \"$obj\""

    private fun encodeNull(): CharSequence = "nil"

    private fun encodeBool(b: Boolean): CharSequence = if (b) "true" else "false"

    private fun encodePredefinedNumberType(obj: Number): CharSequence {
        val writer = StringBuilder()
        when (obj) {
            is Byte, is Short, is Int, is Long, is Ratio -> writer.append(obj.toString())
            is BigInteger -> return "${obj}N"
            is BigDecimal -> return "${obj}M"
            else -> writer.append(obj.toString())
        }
        if (options.allowNumericSuffixes) {
            when (obj) {
                is Byte -> writer.append("_i8")
                is Short -> writer.append("_i16")
                is Int -> writer.append("_i32")
            }
        }
        return writer
    }

    private fun encodeFloat(obj: Float): CharSequence =
        if (obj.isNaN()) "##NaN"
        else if (obj == Float.POSITIVE_INFINITY) "##INF"
        else if (obj == Float.NEGATIVE_INFINITY) "##-INF"
        else obj.toString()

    private fun encodeDouble(obj: Double): CharSequence =
        if (obj.isNaN()) "##NaN"
        else if (obj == Double.POSITIVE_INFINITY) "##INF"
        else if (obj == Double.NEGATIVE_INFINITY) "##-INF"
        else obj.toString()

    private fun encodeComplex(obj: Complex): CharSequence {
        if (options.allowComplexNumberLiterals) {
            // Maybe some special handling?
            return obj.toString()
        }

        // Maybe some special handling?
        return obj.toString()
    }

    private fun encodeString(obj: String): CharSequence {
        val writer = StringBuilder()
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

    private fun encodeChar(obj: Char): CharSequence = when (obj) {
        '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        '\\', '^', '_', '`',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '|', '~', '§', '°', '´', '€' -> "\\$obj"

        '\n' -> "\\newline"
        ' ' -> "\\space"
        '\t' -> "\\tab"
        '\b' -> "\\backspace"
        12.toChar() -> "\\formfeed"
        '\r' -> "\\return"

        else -> String.format("\\u%04x", obj.code)
    }

    private fun encodeChar32(obj: Char32): CharSequence {
        if (!options.allowDispatchChars) {
            return "\"$obj\""
        }

        return when (obj.code) {
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
            -> "#\\$obj"

            '\n'.code -> "#\\newline"
            ' '.code -> "#\\space"
            '\t'.code -> "#\\tab"
            '\b'.code -> "#\\backspace"
            12 -> "#\\formfeed"
            '\r'.code -> "#\\return"

            else -> String.format("#\\u%08x", obj.code)
        }
    }

    private fun encodeSequence(obj: Sequence<*>): CharSequence {
        return seqEncoderHelper(obj.iterator(), "(", ")", options.encoderSequenceElementLimit)
    }

    private fun encodePersistentList(obj: PersistentList<*>): CharSequence {
        return seqEncoderHelper(obj.iterator(), "(", ")", options.encoderCollectionElementLimit)
    }

    private fun encodeOtherIterable(obj: Iterable<*>): CharSequence {
        return seqEncoderHelper(obj.iterator(), "(", ")", options.encoderCollectionElementLimit)
    }

    private fun encodeVector(obj: List<*>): CharSequence {
        return seqEncoderHelper(obj.iterator(), "[", "]", options.encoderCollectionElementLimit)
    }

    private fun encodeSet(obj: Set<*>): CharSequence {
        return seqEncoderHelper(obj.iterator(), "#{", "}", options.encoderCollectionElementLimit)
    }

    private fun encodeMap(obj: Map<*, *>): CharSequence {
        val iter = obj.iterator()
        val opener = "{"
        val closer = "}"
        val limit = options.encoderCollectionElementLimit
        val didBreak = breakByLength(opener.length)
        writer.append(opener)
        level++
        var counter = 0
        while (iter.hasNext()) {
            if (counter > limit) {
                writer.append("...")
                break
            }
            counter++
            val elem = iter.next()
            writer.append(encode(elem.key))
            writer.append(' ')
            val str = encode(elem.value)
            writer.append(str)
            breakByLength(str.length)
            if (iter.hasNext()) writer.append(options.encodingSequenceSeparator)
        }
        if (didBreak)doBreak()
        writer.append(closer)
        level--
        return ""
    }

    private fun seqEncoderHelper(
        iter: Iterator<*>,
        opener: CharSequence,
        closer: CharSequence,
        limit: Int
    ): CharSequence {
        val didBreak = breakByLength(opener.length)
        writer.append(opener)
        level++
        var counter = 0
        while (iter.hasNext()) {
            if (counter > limit) {
                writer.append("...")
                break
            }
            counter++
            val elem = iter.next()
            val str = encode(elem)
            breakByLength(str.length)
            writer.append(str)
            if (iter.hasNext()) writer.append(options.encodingSequenceSeparator)
        }
        if (didBreak)doBreak()
        writer.append(closer)
        level--
        return ""
    }
}
