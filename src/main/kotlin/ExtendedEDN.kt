import java.util.*


class EdnReaderException(text: String) : Exception(text)
class EdnWriterException(text: String) : Exception(text)
class EdnClassConversionError(text: String) : Exception(text)

data class EDNSoapOptions(
    val allowSchemeUTF32Codes: Boolean = false,
    val ednClassDecoders: Map<String, (Any?) -> Any?> = mapOf(),
    val ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?> = mapOf(),
) {
    companion object {
        private const val NULL_CHAR = '\u0000'

        val extendedOptions: EDNSoapOptions
            get() = extendedReaderOptions(mapOf())

        val defaultOptions: EDNSoapOptions
            get() = EDNSoapOptions(
                allowSchemeUTF32Codes = false,
                ednClassDecoders = mapOf(),
                ednClassEncoders = mapOf()
            )

        fun extendedReaderOptions(ednClassDecoder: Map<String, (Any?) -> Any?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                ednClassDecoders = ednClassDecoder,
                ednClassEncoders = mapOf()
            )

        fun extendedWriterOptions(ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                ednClassDecoders = mapOf(),
                ednClassEncoders = ednClassEncoders
            )
    }
}

class EDNSoapReader private constructor(private val options: EDNSoapOptions = EDNSoapOptions.extendedOptions) {

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
        val codePointIterator = CodePointIterator(s.codePoints())

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

    fun parseString(cpi: CodePointIterator): String {
        val currentToken = StringBuilder()

        while (cpi.hasNext()) {
            when (val codePoint = cpi.nextInt()) {
                '"'.code ->
                    return currentToken.toString()

                '\\'.code -> {
                    if (!cpi.hasNext()) break
                    val codePt2 = cpi.nextInt()
                    when (codePt2) {
                        't'.code -> currentToken.append('\t')
                        'b'.code -> currentToken.append('\b')
                        'n'.code -> currentToken.append('\n')
                        'r'.code -> currentToken.append('\r')
                        '"'.code -> currentToken.append('\"')
                        '\\'.code -> currentToken.append("\\\\")
                        'u'.code -> currentToken.append(Char(parseUnicodeChar(cpi, 4, 4, 16, 'u'))) // UTF-16 code
                        'o'.code -> currentToken.append(Char(parseUnicodeChar(cpi, 1, 3, 8, 'o')))
                        'x'.code -> {
                            if (options.allowSchemeUTF32Codes)
                                currentToken.appendCodePoint(parseUnicodeChar(cpi, 8, 8, 16, 'x')) // UTF-32
                            else
                                throw EdnReaderException("Invalid escape sequence: \\${codePt2.toChar()} in string $currentToken")
                        }

                        else ->
                            throw EdnReaderException("Invalid escape sequence: \\${codePt2.toChar()} in string $currentToken")
                    }
                }

                else -> currentToken.appendCodePoint(codePoint)
            }
        }

        throw EdnReaderException("Unclosed String literal \"$currentToken\" .")
    }

    fun parseUnicodeChar(cpi: CodePointIterator, minLength: Int, maxLength: Int, base: Int, initChar: Char): Int {
        var code = 0
        for (i in 1..maxLength) {
            if (!cpi.hasNext()) {
                val msg =
                    "Invalid length of unicode sequence $i in sequence \\$initChar${code.toString(base)} (should be at least $minLength)"
                throw EdnReaderException(msg)
            }
            val c = cpi.nextInt()
            if (!isAlphaNum(c)) {
                val msg = "Invalid unicode codepoint ${c.toChar()} in sequence \\$initChar${code.toString(base)}"
                throw EdnReaderException(msg)
            }
            code = code * base + c.toChar().digitToInt(base)
        }
        return code
    }

    fun parseUnicodeChar(token: CharSequence, base: Int, initChar: Char): Int {
        try {
            var code = 0
            for (it in token) code = code * base + it.digitToInt(base)
            return code
        } catch (nfe: NumberFormatException) {
            throw EdnReaderException(nfe.message ?: "Invalid unicode sequence \\$initChar$token")
        }
    }

    fun parseList(cpi: CodePointIterator): Iterable<*> {
        TODO()
    }

    fun parseVector(cpi: CodePointIterator): List<*> {
        TODO()
    }

    fun parseMap(cpi: CodePointIterator): Map<*, *> {
        TODO()
    }

    fun parseSet(cpi: CodePointIterator): Map<*, *> {
        TODO()
    }

    fun parseChar(cpi: CodePointIterator): Char {
        val token = readToken(cpi, ::isNotBreakingSymbol)
        return Char(parseDispatchUnicodeChar(token, false))
    }

    fun parseDispatchUnicodeChar(token: CharSequence, isDispatch: Boolean = false): Int {
        if (token.length == 1)
            return token[0].code

        when (token) {
            "newline" -> return '\n'.code
            "space" -> return ' '.code
            "tab" -> return '\t'.code
            "backspace" -> return '\b'.code
            "formfeed" -> return 12 // '\f'
            "return" -> return '\r'.code
        }

        val reducedLengthToken = token.subSequence(1, token.length)
        val errorTokenText = if (isDispatch) "#\\$token" else "\\$token"
        when (token[0]) {
            'o' -> {
                if (reducedLengthToken.length <= 1 || reducedLengthToken.length > 3) {
                    val msg =
                        "Invalid length of unicode sequence ${reducedLengthToken.length} in sequence $errorTokenText (should be 4)"
                    throw EdnReaderException(msg)
                }
                return parseUnicodeChar(reducedLengthToken, 16, 'o')
            }

            'u' -> { // UTF-16 or UTF-32 code
                if (reducedLengthToken.length == 4 || (isDispatch && reducedLengthToken.length == 8))
                    return parseUnicodeChar(reducedLengthToken, 16, 'u')
                val msg =
                    "Invalid length of unicode sequence ${reducedLengthToken.length} in char literal $errorTokenText (should be 4 or 8)"
                throw EdnReaderException(msg)
            }

            'x' -> { // UTF-32 code
                if (!options.allowSchemeUTF32Codes)
                    throw EdnReaderException("Invalid char literal: $errorTokenText")
                if (reducedLengthToken.length == 8)
                    return parseUnicodeChar(reducedLengthToken, 16, 'x')
                val msg =
                    "Invalid length of unicode sequence ${reducedLengthToken.length} in char literal $errorTokenText (should be 8)"
                throw EdnReaderException(msg)

            }

            else -> throw EdnReaderException("Invalid char literal $errorTokenText")
        }
    }

    fun parseDispatch(cpi: CodePointIterator): Any {
        val token = cpi.takeCodePoints(StringBuilder(), ::isNotBreakingSymbol)

        if (token.isEmpty()) { // Next symbol was a breaking symbol (whitespace, bracket, etc.) or EOF
            if (!cpi.hasNext())
                throw EdnReaderException("Invalid dispatch expression #$token.")

            when (val code = cpi.nextInt()) {
                '{'.code -> token.appendCodePoint(code)
            }
        }

        when (token[0]) {
            '\\' ->
                if (options.allowSchemeUTF32Codes)
                    return StringBuilder()
                        .appendCodePoint(parseDispatchUnicodeChar(token.subSequence(1, token.length), true))
                        .toString()
            '{' -> parseSet(cpi)
        }

        throw EdnReaderException("Invalid dispatch expression #$token.")
    }

    private fun readForm(codePointIterator: CodePointIterator, initCodePoint: Int): Any? {
        TODO()
    }

    private fun readToken(cpi: CodePointIterator, condition: (Int) -> Boolean): String {
        val currentToken = StringBuilder()

//        while (cpi.hasNext()) {
//            val codepoint = cpi.nextInt()
//            if (isBreakingSymbol(codepoint)) {
//                cpi.unread(codepoint)
//                return currentToken.toString()
//            }
//        }
        cpi.takeCodePoints(currentToken) { condition(it) }

        return currentToken.toString()
    }

    private fun isNotBreakingSymbol(sym: Int): Boolean = when (sym.toChar()) {
        ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}', '#', '\'', '"' -> false
        else -> true
    }

    private fun isAlphaNum(sym: Int): Boolean = when (sym.toChar()) {
        in 'a'..'z', in 'A'..'Z', in '0'..'9' -> true
        else -> false
    }

    private fun isHexNum(sym: Int): Boolean = when (sym.toChar()) {
        in 'a'..'f', in 'A'..'F', in '0'..'9' -> true
        else -> false
    }
}
