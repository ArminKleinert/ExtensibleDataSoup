import java.util.*


class EdnReaderException(text: String) : Exception(text)
class EdnWriterException(text: String) : Exception(text)
class EdnClassConversionError(text: String) : Exception(text)

data class EDNSoapOptions(
    val useCompacts: Boolean = false,
    val useCollectionPrefixes: Boolean = false,
    val allowSchemeBoolLiterals: Boolean = false,
    val allowSchemeCharLiterals: Boolean = false,
    val allowNilAsNull: Boolean = false,
    val defaultIntegralNumberBase: Int = 10,
    val allowNumberUnderscores: Boolean = false,
    val allowNumberPrefixes: Boolean = false,

    val bytePostfix: Char = 'B',
    val shortPostfix: Char = 'S',
    val intPostfix: Char = NULL_CHAR,
    val longPostfix: Char = 'L',
    val floatPostfix: Char = 'F',
    val doublePostfix: Char = 'D',
    val bigIntPostfix: Char = 'N',
    val bigDecimalPostfix: Char = 'M',

    val automaticallyExtendNumbers: Boolean = false,
    val longIsLowerExtendBound: Boolean = false,
    val ednClassDecoders: Map<String, (Any?) -> Any?> = mapOf(),
    val ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?> = mapOf(),
) {
    companion object {
        private const val NULL_CHAR = '\u0000'

        val extendedOptions: EDNSoapOptions
            get() = extendedReaderOptions(mapOf())

        val defaultOptions: EDNSoapOptions
            get() = EDNSoapOptions(
                useCompacts = false,
                useCollectionPrefixes = false,
                allowSchemeBoolLiterals = false,
                allowSchemeCharLiterals = false,
                allowNilAsNull = false,

                allowNumberUnderscores = false,
                allowNumberPrefixes = false,
                intPostfix = NULL_CHAR,
                longPostfix = NULL_CHAR,
                doublePostfix = NULL_CHAR,
                bigIntPostfix = 'N',
                bigDecimalPostfix = 'M',
                automaticallyExtendNumbers = false,
                longIsLowerExtendBound = true,

                ednClassDecoders = mapOf(),
                ednClassEncoders = mapOf()
            )

        fun extendedReaderOptions(ednClassDecoder: Map<String, (Any?) -> Any?>) =
            EDNSoapOptions(
                useCompacts = true,
                useCollectionPrefixes = true,
                allowSchemeBoolLiterals = true,
                allowNilAsNull = true,

                allowNumberUnderscores = true,
                allowNumberPrefixes = true,

                intPostfix = 'i',
                longPostfix = NULL_CHAR,
                doublePostfix = 'D',
                bigIntPostfix = 'N',
                bigDecimalPostfix = 'M',
                automaticallyExtendNumbers = true,
                longIsLowerExtendBound = true,

                ednClassDecoders = ednClassDecoder,
                ednClassEncoders = mapOf()
            )

        fun extendedWriterOptions(ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?>) =
            EDNSoapOptions(
                useCompacts = true,
                useCollectionPrefixes = true,
                allowSchemeBoolLiterals = true,
                allowNilAsNull = true,
                ednClassDecoders = mapOf(),
                ednClassEncoders = ednClassEncoders
            )
    }
}

class EDNSoapReader private constructor(options: EDNSoapOptions = EDNSoapOptions.defaultOptions) {

    companion object {
        private const val NULL_CHAR = '\u0000'

        @Throws(EdnReaderException::class)
        fun readString(input: String, options: EDNSoapOptions = EDNSoapOptions.extendedOptions): Any? {
            return EDNSoapReader(options).readString(input)
        }
    }


//        private fun tokenizeString(chars: IntIterator, currentToken: String, options: EDNSoapOptions): String {
//            var isStringEscaped = false
//            val currentToken = StringBuilder()
//
//            while (chars.hasNext()) {
//                val ch = chars.next()
//                if (ch == '"'.code) return currentToken.toString()
//            }
//
//            throw EdnReaderException("Unclosed String $currentToken")
//        }

    fun readString(s: String): Any? {
        val currentToken = StringBuilder()
        val codePointIterator = s.codePoints().iterator()

        if (!codePointIterator.hasNext())
            throw EdnReaderException("Empty input string.")

        when (val codePoint = codePointIterator.nextInt()) {
            '"'.code -> return parseString(codePointIterator)
            '('.code -> return parseList(codePointIterator)
            '['.code -> return parseVector(codePointIterator)
            '{'.code -> return parseMap(codePointIterator)
            '\\'.code -> return parseChar(codePointIterator)
            '#'.code -> return parseDispatch(codePointIterator)
            else -> return readForm(codePointIterator, codePoint)
        }
    }

    fun parseString(cpi: PrimitiveIterator.OfInt): String {
        val currentToken = StringBuilder()

        while (cpi.hasNext()) {
            when (val codePoint = cpi.nextInt()) {
                '"'.code ->
                    return currentToken.toString()

                '\\'.code -> {
                    if (!cpi.hasNext()) break
                    when (val codePt2 = cpi.nextInt()) {
                        't'.code -> currentToken.append('\t')
                        'b'.code -> currentToken.append('\b')
                        'n'.code -> currentToken.append('\n')
                        'r'.code -> currentToken.append('\r')
                        '"'.code -> currentToken.append('\"')
                        '\\'.code -> currentToken.append("\\\\")
                        'u'.code -> currentToken.append(Char(parseUnicodeChar(cpi, 4, 16, 'u'))) // UTF-16 code
                        'o'.code -> currentToken.append(Char(parseUnicodeChar(cpi, 3, 8, 'o')))
                        'x'.code -> currentToken.appendCodePoint(parseUnicodeChar(cpi, 8, 16, 'o')) // UTF-32
                        else -> throw EdnReaderException("Illegal escape sequence: \\$codePt2 in string $currentToken")
                    }
                }

                else -> currentToken.appendCodePoint(codePoint)
            }
        }

        throw EdnReaderException("Unclosed String literal \"$currentToken\" .")
    }

    fun parseUnicodeChar(cpi: PrimitiveIterator.OfInt, length: Int, base: Int, initChar: Char): Int {
        var code = 0
        for (i in 1..length) {
            if (!cpi.hasNext())
                throw EdnReaderException(
                    "Invalid length of unicode sequence $i in sequence \\$initChar${code.toString(16)}"
                )
            val c = cpi.nextInt().toChar()
            if (c !in ('0'..'9') && c !in ('a'..'z') && c !in ('A'..'Z'))
                throw EdnReaderException("Invalid unicode codepoint $c in sequence \\$initChar${code.toString(16)}")
            code = code * base + c.digitToInt(16)
        }
        return code
    }

    fun parseList(cpi: PrimitiveIterator.OfInt): Iterable<*> {
        TODO()
    }

    fun parseVector(cpi: PrimitiveIterator.OfInt): List<*> {
        TODO()
    }

    fun parseMap(cpi: PrimitiveIterator.OfInt): Map<*, *> {
        TODO()
    }

    fun parseChar(cpi: PrimitiveIterator.OfInt): Char {
        TODO()
    }

    fun parseDispatch(cpi: PrimitiveIterator.OfInt): Any? {
        TODO()
    }

    private fun readForm(codePointIterator: PrimitiveIterator.OfInt, initCodePoint: Int): Any? {
        TODO()
    }


}
