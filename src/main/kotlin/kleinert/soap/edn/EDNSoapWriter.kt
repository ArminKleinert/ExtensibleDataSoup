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
            EDNSoapWriter(options, writer).encode(obj, writer)
            return writer.toString()
        }

        fun pprint(
            obj: Any?,
            options: EDNSoapOptions = EDNSoapOptions.defaultOptions,
            writer: Appendable = System.out.writer()
        ) {
            EDNSoapWriter(options, writer).encode(obj, writer)
            if (writer is Flushable) writer.flush()
        }

        fun pprintln(
            obj: Any?,
            options: EDNSoapOptions = EDNSoapOptions.defaultOptions,
            writer: Appendable = System.out.writer()
        ) {
            EDNSoapWriter(options, writer).encode(obj, writer)
            writer.append('\n')
            if (writer is Flushable) writer.flush()
        }
    }

    private fun tryEncoder(obj: Any, writer: Appendable, indent: Int): Any? {
        var encoder: ((Any) -> Pair<String?, Any?>?)? = null
        for ((jClass, enc) in options.ednClassEncoders) {
            if (jClass.isInstance(obj)) {
                encoder = enc
                break
            }
        }
        if (encoder == null)
            return null
        val (prefix, output) = encoder(obj) ?: return null
        if (!prefix.isNullOrBlank()) writer.append('#').append(prefix).append(' ')
        encode(output, writer, indent)
        return true
    }

    fun encode(
        obj: Any?,
        writer: Appendable,
        indent: Int = 0
    ) {
        when (obj) {
            null -> writer.append(encodeNull())
            true -> writer.append(encodeBool(true))
            false -> writer.append(encodeBool(false))

            is String -> writer.append(encodeString(obj))
            is Keyword -> tryEncoder(obj, writer, indent) ?: writer.append(encodeKeyword(obj))
            is Symbol -> tryEncoder(obj, writer, indent) ?: writer.append(encodeSymbol(obj))

            is PersistentList<*> ->
                tryEncoder(obj, writer, indent) ?: encodePersistentList(obj, writer, indent) // List, not vector

            is ByteArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is ShortArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is IntArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is LongArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is FloatArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is DoubleArray -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is Array<*> -> tryEncoder(obj, writer, indent) ?: encode(obj.toList(), writer, indent) // as vector
            is List<*> -> tryEncoder(obj, writer, indent) ?: encodeVector(obj, writer, indent) // Vector

            is Set<*> -> tryEncoder(obj, writer, indent) ?: encodeSet(obj, writer, indent)
            is Map<*, *> -> tryEncoder(obj, writer, indent) ?: encodeMap(obj, writer, indent)

            is Iterable<*> -> tryEncoder(obj, writer, indent) ?: encodeOtherIterable(obj, writer, indent)
            is Sequence<*> -> tryEncoder(obj, writer, indent) ?: encodeSequence(obj, writer, indent)

            is Char -> writer.append(encodeChar(obj))
            is Char32 -> writer.append(encodeChar32(obj))
            is Byte, is Short, is Int, is Long, is Ratio -> writer.append(encodePredefinedNumberType(obj as Number))
            is Float -> writer.append(encodeFloat(obj))
            is Double -> writer.append(encodeDouble(obj))
            is Complex -> writer.append(encodeComplex(obj))
            is BigInteger, is BigDecimal -> writer.append(encodePredefinedNumberType(obj as Number))

            is IObj<*> -> encodeIObj(obj, writer, indent)

            is UUID -> writer.append(encodeUuid(obj))
            is Instant -> writer.append(encodeInstant(obj))

            else -> tryEncoder(obj, writer, indent) ?: writer.append(obj.toString())
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodePersistentList(obj: PersistentList<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj, "(", ")", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeVector(obj: List<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj, "[", "]", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeSet(obj: Set<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj.toList(), "#{", "}", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeSequence(obj: Sequence<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj.toList(), "(", ")", writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeOtherIterable(obj: Iterable<*>, writer: Appendable, indent: Int) {
        formatCollectionTo(obj.toList(), "(", ")", writer, indent)
    }

    private fun formatCollectionTo(
        elements: List<*>, open: String, close: String, writer: Appendable, indent: Int, isMap: Boolean = false
    ) {
        // Try inline first (dry-run)
        val tmp = StringBuilder()
        tmp.append(open)
        if (isMap) {
            for ((i, entry) in elements.withIndex()) {
                val e = entry as Map.Entry<*, *>
                encode(e.key, tmp, indent + 2)
                tmp.append(' ')
                encode(e.value, tmp, indent + 2)
                if (i != elements.lastIndex) tmp.append(options.encodingSequenceSeparator)
            }
        } else {
            for ((i, e) in elements.withIndex()) {
                encode(e, tmp, indent + 2)
                if (i != elements.lastIndex) tmp.append(options.encodingSequenceSeparator)
            }
        }
        tmp.append(close)

        if (tmp.length + indent <= options.encoderMaxColumn) {
            writer.append(tmp)
            return
        }

        // Multi-line formatting
        writer.append(open).append("\n")
        val childIndent = indent + 2
        val pad = " ".repeat(childIndent)
        if (isMap) {
            for ((i, entry) in elements.withIndex()) {
                val e = entry as Map.Entry<*, *>
                writer.append(pad)
                encode(e.key, writer, childIndent)
                writer.append(' ')
                encode(e.value, writer, childIndent)
                if (i != elements.lastIndex) writer.append(options.encodingSequenceSeparator).append("\n")
            }
        } else {
            for ((i, e) in elements.withIndex()) {
                writer.append(pad)
                encode(e, writer, childIndent)
                if (i != elements.lastIndex) writer.append(options.encodingSequenceSeparator).append("\n")
            }
        }
        writer.append("\n").append(" ".repeat(indent)).append(close)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeMap(obj: Map<*, *>, writer: Appendable, indent: Int) =
        formatCollectionTo(obj.entries.toList(), "{", "}", writer, indent, true)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeNull(): CharSequence = "nil"

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeBool(b: Boolean): CharSequence = if (b) "true" else "false"


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


    private fun encodeIObj(obj: IObj<*>, writer: Appendable, indent: Int) {
        writer.append('^')
        encode(obj.meta, writer, indent)
        writer.append(' ')
        encode(obj.obj, writer, indent)
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeKeyword(obj: Keyword): CharSequence = obj.toString()

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeSymbol(obj: Symbol): CharSequence = obj.toString()

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeUuid(obj: UUID): CharSequence = "#uuid \"$obj\""

    @Suppress("NOTHING_TO_INLINE")
    private inline fun encodeInstant(obj: Instant): CharSequence = "#inst \"$obj\""

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
}