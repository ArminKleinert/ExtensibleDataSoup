package kleinert.soap

import kleinert.soap.data.*
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.LinkedHashMap

class EDNSoapReader private constructor(private val options: EDNSoapOptions = EDNSoapOptions.extendedOptions) {
    companion object {
        private const val NULL_CHAR = '\u0000'

        private val NOTHING = object {}

        @Throws(EdnReaderException::class)
        fun readString(input: String, options: EDNSoapOptions = EDNSoapOptions.defaultOptions): Any? {
            return EDNSoapReader(options).readString(input)
        }
    }

    init {
        ensureValidDecoderNames()
    }

    /*
     + Decoders (tags) must follow the same naming conventions as symbols AND must have a prefix part.
     */
    private fun ensureValidDecoderNames() {
        if (!options.allowMoreEncoderDecoderNames) {
            for (key in options.ednClassDecoders.keys) {
                val name = Symbol.parse(key)
                if (name?.prefix == null) throw EdnReaderException("Invalid decoder name: \"$key\" (parsed as $name)")
            }
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
        val data = readForm(codePointIterator, 0)
        if ((data as Collection<*>).size != 1)
            throw EdnReaderException("The string should only contain one expression, but there are multiple.")
        return data.first()
    }

    private fun parseNumber(cpi: CodePointIterator): Number {
        try {
            return parseNumberHelper(cpi)
        } catch (ex: NumberFormatException) {
            // For UUID.fromString
            throw EdnReaderException(ex.toString(), ex)
        }
    }

    private fun parseNumberHelper(cpi: CodePointIterator, negate: Boolean = false): Number {
        fun negateIfNegative(number: BigDecimal) = if (negate) number.negate() else number
        fun negateIfNegative(number: BigInteger) = if (negate) number.negate() else number
        fun negateIfNegative(number: Long) = if (negate) -number else number
        fun negateIfNegative(number: Double) = if (negate) -number else number
        fun negateIfNegative(number: Ratio) = if (negate) number.negate() else number
        fun negateIfNegative(number: Complex) = if (negate) number.negate() else number

        val token = readToken(cpi, ::isNotBreakingSymbol)

        val floatyRegex = Regex("[+\\-]?[0-9]*\\.?[0-9]+([eE][+\\-][0-9]+)?M?")
        val intRegex = Regex("[+\\-]?(0[obx])?[0-9a-fA-F]+N?")
        val ratioRegex = Regex("[+\\-]?[0-9]+/[0-9]+?")

        val expandedIntRegex = Regex("[+\\-]?(0[obx])?[0-9a-fA-F]+(N|_i8|_i16|_i32|_i64)?")
        val complexRegex = Regex("[+\\-]?([0-9]*\\.?[0-9]+[+\\-]?)?([0-9]*\\.?[0-9]+)?i")

        val tokenLen = token.length
        var base = 10
        var startIndex = 0
        var sign: Long = 1

        if ((options.allowNumericSuffixes && expandedIntRegex.matches(token)) || intRegex.matches(token)) {
            if (token[0] == '+') {
                startIndex++
                sign = 1
            } else if (token[0] == '-') {
                startIndex++
                sign = -1
            }

            val first = token[startIndex]
            if (first == '0' && token.length - startIndex > 1) {
                startIndex += 2
                base = when (token[startIndex - 1]) {
                    'x' -> 16
                    'o' -> 8
                    'b' -> 2
                    else -> {
                        startIndex -= 2 // Reset start index
                        8
                    }
                }
            }
        } else if (floatyRegex.matches(token)) {
            startIndex = 0
            if (token.endsWith('M')) return token.substring(startIndex, tokenLen - 1).toBigDecimal()
            return sign * (token.toDouble())
        } else if (ratioRegex.matches(token)) {
            return Ratio.valueOf(token)
        } else if (options.allowComplexNumberLiterals && complexRegex.matches(token)) {
            return Complex.valueOf(token)
        } else {
            throw EdnReaderException("Invalid number format: $token")
        }

        return when {
            token.endsWith('M') -> sign.toBigDecimal()
                .multiply(token.substring(startIndex, tokenLen - 1).toBigDecimal())

            token.endsWith("N") -> sign.toBigInteger()
                .multiply(token.substring(startIndex, tokenLen - 1).toBigInteger(base))

            !options.allowNumericSuffixes -> sign * (token.substring(startIndex, tokenLen - 0).toLong(base))
            token.endsWith("_i8") -> (sign * token.substring(startIndex, tokenLen - 3).toLong(base)).toByte()
            token.endsWith("_i16") -> (sign * token.substring(startIndex, tokenLen - 4).toLong(base)).toShort()
            token.endsWith("_i32") -> (sign * token.substring(startIndex, tokenLen - 4).toLong(base)).toInt()
            token.endsWith("_i64") -> (sign * token.substring(startIndex, tokenLen - 4).toLong(base))
            token.endsWith("L") -> (sign * token.substring(startIndex, tokenLen - 1).toLong(base))
            else -> sign * (token.substring(startIndex, tokenLen - 0).toLong(base))
        }
    }

    private fun parseComment(cpi: CodePointIterator, errorIfEof: Boolean = false) {
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
                        '\\'.code -> currentToken.append("\\")
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

    private fun parseUnicodeChar(
        cpi: CodePointIterator,
        minLength: Int,
        maxLength: Int,
        base: Int,
        initChar: Char
    ): Int {
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

    private fun parseList(cpi: CodePointIterator, level: Int): List<Any?> {
        val temp = parseVector(cpi, level, ')'.code)
        return options.listToPersistentListConverter(temp)
    }

    private fun parseVector(cpi: CodePointIterator, level: Int, separator: Int): List<*> = buildList {
        do {
            cpi.skipWhile(::isWhitespace)

            if (!cpi.hasNext())
                throw EdnReaderException("Unclosed list. Expected '${Char(separator)}', got EOF.")

            if (cpi.peek() == separator) {
                cpi.nextInt()
                break
            }

            val elem = readForm(cpi, level + 1, true)

            if (elem != NOTHING) add(elem)
        } while (true)
    }.toList().let {
        options.listToPersistentVectorConverter(it)
    }

    private fun parseMap(cpi: CodePointIterator, level: Int, separator: Int = '}'.code): Map<*, *> {
        val result = LinkedHashMap<Any?, Any?>()
        val lst = parseVector(cpi, level, separator)
        var i = 0
        do {
            if (i >= lst.size)
                break
            val key = lst[i]
            if (result.contains(key))
                throw EdnReaderException("Illegal map. Duplicate key $key.")
            i++
            if (i >= lst.size)
                throw EdnReaderException("Odd number of elements in map. Last key was $key.")
            val value = lst[i]
            result[key] = value
            i++
        } while (true)
        return PersistentMap(result)
    }

    private fun parseSet(cpi: CodePointIterator, level: Int, separator: Int = '}'.code): Set<*> {
        val result = LinkedHashSet<Any?>()
        val lst = parseVector(cpi, level, separator)
        var i = 0
        do {
            if (i >= lst.size)
                break
            val key = lst[i]
            if (result.contains(key))
                throw EdnReaderException("Illegal set. Duplicate value $key.")
            result.add(key)
            i++
        } while (true)
        return PersistentSet(result)
    }

    private fun parseChar(cpi: CodePointIterator): Char {
        val token = readToken(cpi, ::isNotBreakingSymbol)
        return Char(parseDispatchUnicodeChar(cpi, token, false))
    }

    private fun parseDispatchUnicodeChar(
        cpi: CodePointIterator,
        token: CharSequence,
        isDispatch: Boolean = false
    ): Int {
        val token = if (token.isEmpty() && cpi.hasNext())
            cpi.takeCodePoints(StringBuilder(), 1, ::isValidCharSingle)
        else
            token

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
                return parseUnicodeChar(reducedToken, 8, 'o')
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

    private fun parseDispatch(cpi: CodePointIterator, level: Int): Any? {
        val token = cpi.takeCodePoints(StringBuilder(), ::isNotBreakingSymbolOrDispatch)

        if (token.isEmpty()) { // Next symbol was a breaking symbol (whitespace, bracket, etc.) or EOF
            if (!cpi.hasNext())
                throw EdnReaderException("Invalid dispatch expression #$token.")

            when (val code = cpi.nextInt()) {
                '{'.code -> token.appendCodePoint(code)
                '#'.code -> token.appendCodePoint(code)
                else -> {
                    throw EdnReaderException("Invalid dispatch expression $token.")
                }
            }
        }

        when (token[0]) {
            '\\' ->
                if (options.allowDispatchChars) {
                    val subToken = token.subSequence(1, token.length)
                    val uniChar = parseDispatchUnicodeChar(cpi, subToken, true)
                    return StringBuilder()
                        .appendCodePoint(uniChar)
                        .toString()
                }

            '{' -> return parseSet(cpi, level + 1)

            '#' -> {
                cpi.takeCodePoints(token, ::isNotBreakingSymbolOrDispatch)

                val tokenAsString = "#$token"

                return when (tokenAsString) {
                    "##NaN" -> Double.NaN
                    "##-NaN" -> -Double.NaN
                    "##INF" -> Double.POSITIVE_INFINITY
                    "##-INF" -> Double.NEGATIVE_INFINITY
                    "##time" ->
                        if (options.allowTimeDispatch) LocalDateTime.now()
                        else throw EdnReaderException("Unknown symbolic value $tokenAsString")

                    else -> throw EdnReaderException("Unknown symbolic value $tokenAsString")
                }
            }
        }

        val tokenAsString = token.toString()

        if (tokenAsString == "uuid" || tokenAsString == "inst")
            return parseDecode(cpi, level + 1, tokenAsString, null)

        val decoder = options.ednClassDecoders[tokenAsString]
        if (decoder != null) {
            return parseDecode(cpi, level + 1, tokenAsString, decoder)
        }

        throw EdnReaderException("Invalid dispatch expression #$tokenAsString.")
    }

    private fun parseDecode(cpi: CodePointIterator, level: Int, token: CharSequence, decoder: ((Any?) -> Any?)?): Any? {
        val form = readForm(cpi, level + 1, stopAfterOne = true)

        if (token == "uuid" || token == "inst") {
            try {
                if (form !is CharSequence)
                    throw EdnReaderException("Dispatch decoder $token requires a string as input but got $form.")
                return when (token) {
                    "uuid" -> UUID.fromString(form.toString())
                    "inst" -> Instant.parse(form)
                    else -> throw NotImplementedError("Not implemented.")
                }
            } catch (ex: IllegalArgumentException) {
                // For UUID.fromString
                throw EdnReaderException("Dispatch parsing of operation $token failed.", ex)
            } catch (ex: NumberFormatException) {
                // For UUID.fromString
                throw EdnReaderException("Dispatch parsing of operation $token failed.", ex)
            } catch (ex: DateTimeParseException) {
                // For Instant.parse.
                throw EdnReaderException("Dispatch parsing of operation $token failed.", ex)
            }
        }

        return decoder!!(form)
    }

    private fun parseOther(cpi: CodePointIterator, level: Int): Any? {
        val token = cpi.takeCodePoints(StringBuilder(), ::isValidSymbolChar).toString()

        if (token.length > 1)
            when (token[0]) {
                ':' -> return Keyword.parse(token, options.allowUTFSymbols)
                    ?: throw EdnReaderException("Token starts with colon, but is not a valid keyword: $token")
            }

        if (token[0] == ':') throw EdnReaderException("Lonely colon.")

        return when (token) {
            "nil" -> null
            "true" -> true
            "false" -> false
            else -> Symbol.parse(token, options.allowUTFSymbols)
                ?: throw EdnReaderException("Invalid symbol: $token")
        }
    }

    private fun readForm(cpi: CodePointIterator, level: Int, stopAfterOne: Boolean = false): Any? {
        val res = mutableListOf<Any?>()
        do {
            cpi.skipWhile(::isWhitespace)

            if (!cpi.hasNext()) {
                //throw EdnReaderException.EdnEmptyInputException("Expected anything but got EOF.")
                break
            }

            when (val codePoint = cpi.nextInt()) {
                ';'.code -> {
                    parseComment(cpi)
                    continue
                }

                '"'.code -> res.add(parseString(cpi))
                '('.code -> res.add(parseList(cpi, level + 1))
                '['.code -> res.add(parseVector(cpi, level + 1, ']'.code))
                '{'.code -> res.add(parseMap(cpi, level + 1, '}'.code))
                '\\'.code -> res.add(parseChar(cpi))
                '#'.code -> {
                    if (cpi.hasNext() && cpi.peek() == '_'.code) {
                        cpi.nextInt()
                        do {
                            val temp = readForm(cpi, level + 1, true)
                        } while (temp == NOTHING)
                        //res.add(readForm(cpi, level))
                        if (stopAfterOne) return NOTHING
                    } else {
                        res.add(parseDispatch(cpi, level + 1))
                    }
                }

                '0'.code, '1'.code, '2'.code, '3'.code, '4'.code, '5'.code, '6'.code, '7'.code, '8'.code, '9'.code ->
                    res.add(parseNumber(cpi.unread(codePoint)))

                ')'.code, ']'.code, '}'.code ->
                    throw EdnReaderException("Unexpected character ${Char(codePoint)}.")

                '+'.code -> {
                    res.add(
                        if (cpi.hasNext() && cpi.peek() in '0'.code..'9'.code) parseNumber(cpi.unread(codePoint))
                        else parseOther(cpi.unread(codePoint), level + 1)
                    )
                }

                '-'.code -> {
                    res.add(
                        if (cpi.hasNext() && cpi.peek() in '0'.code..'9'.code) parseNumber(cpi.unread(codePoint))
                        else parseOther(cpi.unread(codePoint), level + 1)
                    )
                }

                else -> res.add(parseOther(cpi.unread(codePoint), level + 1))
            }

            if (stopAfterOne && res.isNotEmpty()) {
                return res[0]
            }
        } while (true)

        if (res.size != 1) {
            throw EdnReaderException("Reader requires exactly one expression, but got ${res.size}.")
        }
        return res
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
        else Char(code).isWhitespace() || code == ','.code

    private fun isValidCharSingle(code: Int): Boolean =
        if (code < Char.MIN_VALUE.code || code > Char.MAX_VALUE.code) false
        else !Char(code).isWhitespace()

    private fun isValidSymbolChar(code: Int): Boolean = when (code.toChar()) {
        ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}', '"' -> false
        else -> true
    }

    private fun isNotBreakingSymbol(code: Int): Boolean = when (code.toChar()) {
        ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}', '\'', '"', ';', ',' -> false
        else -> true
    }

    private fun isNotBreakingSymbolOrDispatch(code: Int): Boolean {
        return isNotBreakingSymbol(code) && code != '#'.code
    }

    private fun isAlphaNum(code: Int): Boolean = when (code.toChar()) {
        in 'a'..'z', in 'A'..'Z', in '0'..'9' -> true
        else -> false
    }

    private fun isHexNum(code: Int): Boolean = when (code.toChar()) {
        in 'a'..'f', in 'A'..'F', in '0'..'9' -> true
        else -> false
    }


}
