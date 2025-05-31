import java.time.LocalDateTime

class EdnReaderException(text: String) : Exception(text)
class EdnWriterException(text: String) : Exception(text)
class EdnClassConversionError(text: String) : Exception(text)

data class EDNSoapOptions(
    val allowSchemeUTF32Codes: Boolean = false,
    val ednClassDecoders: Map<String, (Any?) -> Any?> = mapOf(),
    val ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?> = mapOf(),
    val allowTimeDispatch: Boolean = false,
) {
    companion object {
        private const val NULL_CHAR = '\u0000'

        val extendedOptions: EDNSoapOptions
            get() = extendedReaderOptions(mapOf())

        val defaultOptions: EDNSoapOptions
            get() = EDNSoapOptions(
                allowSchemeUTF32Codes = false,
                allowTimeDispatch = false,
                ednClassDecoders = mapOf(),
                ednClassEncoders = mapOf(),
            )

        fun extendedReaderOptions(ednClassDecoder: Map<String, (Any?) -> Any?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowTimeDispatch = true,
                ednClassDecoders = ednClassDecoder,
                ednClassEncoders = mapOf()
            )

        fun extendedWriterOptions(ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowTimeDispatch = true,
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
        val codePointIterator = CodePointIterator(s.codePoints())
        if (!codePointIterator.hasNext())
            throw EdnReaderException("Empty input string.")
        return readForm(codePointIterator)
    }

    private fun parseNumber(cpi: CodePointIterator): Number {
        var base = 10
        var dotted = false

        val token = readToken(cpi, ::isNotBreakingSymbol)
        val result = StringBuilder(token.length)
        var index = 0

        val first = token[0]
        if (first == '0') {
            index += 2
            base = when (token[1]) {
                'x' -> 16
                'o' -> 8
                'b' -> 2
                else -> {
                    index--
                    8
                }
            }
        }

        return 0
    }

    private fun parseComment(cpi: CodePointIterator) {
//        while (cpi.hasNext()) {
//            val codePoint = cpi.nextInt()
//            if (codePoint == '\n'.code)
//                break
//        }
        cpi.skipLine()
    }

    private fun parseString(cpi: CodePointIterator): String {
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
                        'u'.code -> currentToken.append(Char(parseUnicodeChar(cpi, 4, 4, 16, 'u'))) // UTF-16 code
                        'x'.code -> currentToken.appendCodePoint(parseUnicodeChar(cpi, 8, 8, 16, 'x')) // UTF-32 code
                        else ->
                            throw EdnReaderException("Invalid escape sequence: \\${codePt2.toChar()} in string $currentToken")
                    }
                }

                else -> currentToken.appendCodePoint(codePoint)
            }
        }

        throw EdnReaderException("Unclosed String literal \"$currentToken\" .")
    }

    private fun parseUnicodeChar(cpi: CodePointIterator, minLength: Int, maxLength: Int, base: Int, initChar: Char): Int {
        val token = cpi.takeCodePoints(StringBuilder(), maxLength, ::isHexNum)
        if (token.length < minLength || token.length > maxLength)
            throw EdnReaderException("Invalid unicode sequence \\$initChar$token")
        return parseUnicodeChar(token, base, initChar)
    }

    private fun parseUnicodeChar(token: CharSequence, base: Int, initChar: Char): Int {
        try {
            var code = 0
            for (it in token) code = code * base + it.digitToInt(base)
            return code
        } catch (nfe: NumberFormatException) {
            throw EdnReaderException(nfe.message ?: "Invalid unicode sequence \\$initChar$token")
        }
    }

    private fun parseList(cpi: CodePointIterator): Iterable<*> {
        return parseVector(cpi, ')'.code)
    }

    private fun parseVector(cpi: CodePointIterator, separator: Int = ']'.code): List<*> = buildList {
        do {
            if (!cpi.hasNext()) throw EdnReaderException("Unclosed list. Expected '${Char(separator)}', got EOF.")
            if (cpi.peek() == separator) break

            val elem = readForm(cpi)
            add(elem)
        } while(true)
    }

    private fun parseMap(cpi: CodePointIterator, separator: Int = '}'.code): Map<*, *> = buildMap {
        do {
            if (!cpi.hasNext()) throw EdnReaderException("Unclosed list. Expected '${Char(separator)}', got EOF.")
            if (cpi.peek() == separator) break

            val key = readForm(cpi)
            if (contains(key)) throw EdnReaderException("Illegal map. Duplicate key $key.")
            val value = readForm(cpi)
            put(key, value)
        } while(true)
    }

    private fun parseSet(cpi: CodePointIterator, separator: Int = '}'.code): Set<*> = buildSet {
        do {
            if (!cpi.hasNext()) throw EdnReaderException("Unclosed set. Expected '${Char(separator)}', got EOF.")
            if (cpi.peek() == separator) break

            val elem = readForm(cpi)
            if (contains(elem)) throw EdnReaderException("Illegal set. Duplicate element $elem.")
            add(elem)
        } while(true)
    }

    private fun parseChar(cpi: CodePointIterator): Char {
        val token = readToken(cpi, ::isNotBreakingSymbol)
        return Char(parseDispatchUnicodeChar(token, false))
    }

    private fun parseDispatchUnicodeChar(token: CharSequence, isDispatch: Boolean = false): Int {
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

        val reducedToken = token.subSequence(1, token.length)
        val errorTokenText = if (isDispatch) "#\\$token" else "\\$token"
        when (token[0]) {
            'o' -> {
                if (reducedToken.length <= 1 || reducedToken.length > 3) {
                    val msg =
                        "Invalid length of unicode sequence ${reducedToken.length} in sequence $errorTokenText (should be 4)"
                    throw EdnReaderException(msg)
                }
                return parseUnicodeChar(reducedToken, 16, 'o')
            }

            'u' -> { // UTF-16 or UTF-32 code
                if (reducedToken.length == 4 || (isDispatch && reducedToken.length == 8))
                    return parseUnicodeChar(reducedToken, 16, 'u')
                val msg =
                    "Invalid length of unicode sequence ${reducedToken.length} in char literal $errorTokenText (should be 4 or 8)"
                throw EdnReaderException(msg)
            }

            'x' -> { // UTF-32 code
                if (!options.allowSchemeUTF32Codes)
                    throw EdnReaderException("Invalid char literal: $errorTokenText")
                if (reducedToken.length == 8)
                    return parseUnicodeChar(reducedToken, 16, 'x')
                val msg =
                    "Invalid length of unicode sequence ${reducedToken.length} in char literal $errorTokenText (should be 8)"
                throw EdnReaderException(msg)

            }

            else -> throw EdnReaderException("Invalid char literal $errorTokenText")
        }
    }

    private fun parseDispatch(cpi: CodePointIterator): Any? {
        val token = cpi.takeCodePoints(StringBuilder(), ::isNotBreakingSymbol)

        if (token.isEmpty()) { // Next symbol was a breaking symbol (whitespace, bracket, etc.) or EOF
            if (!cpi.hasNext())
                throw EdnReaderException("Invalid dispatch expression #$token.")

            when (val code = cpi.nextInt()) {
                '{'.code -> token.appendCodePoint(code)
                '#'.code -> token.appendCodePoint(code)
            }
        }

        when (token[0]) {
            '\\' ->
                if (options.allowSchemeUTF32Codes)
                    return StringBuilder()
                        .appendCodePoint(parseDispatchUnicodeChar(token.subSequence(1, token.length), true))
                        .toString()

            '{' -> return parseSet(cpi)

            '_' -> {
                readForm(cpi)
                return null
            }
        }

        if (token[0] == '#') {
            when (token.toString()) {
                "##NaN" -> Double.NaN
                "##INF" -> Double.POSITIVE_INFINITY
                "##-INF" -> Double.NEGATIVE_INFINITY
                "##time" -> LocalDateTime.now()
            }
        }

        throw EdnReaderException("Invalid dispatch expression #$token.")
    }

    private fun readForm(codePointIterator: CodePointIterator): Any? {
        do {
            codePointIterator.skipWhile(::isWhitespace)

            when (val codePoint = codePointIterator.nextInt()) {
                ';'.code -> parseComment(codePointIterator)
                '"'.code -> return parseString(codePointIterator)
                '('.code -> return parseList(codePointIterator)
                '['.code -> return parseVector(codePointIterator)
                '{'.code -> return parseMap(codePointIterator)
                '\\'.code -> return parseChar(codePointIterator)
                '#'.code -> return parseDispatch(codePointIterator)
                '0'.code, '1'.code, '2'.code, '3'.code, '4'.code, '5'.code, '6'.code, '7'.code, '8'.code, '9'.code ->
                    return parseNumber(codePointIterator.unread(codePoint))

                ')'.code, ']'.code, '}'.code ->
                    throw EdnReaderException("Unexpected character ${Char(codePoint)}.")

                else -> return readForm(codePointIterator.unread(codePoint))
            }
        } while (true)
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

    private fun isWhitespace(code: Int): Boolean =
        if (code < Char.MIN_VALUE.code || code > Char.MAX_VALUE.code) false
        else Char(code).isWhitespace()

    private fun isNotBreakingSymbol(sym: Int): Boolean = when (sym.toChar()) {
        ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}', '#', '\'', '"', ';' -> false
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
