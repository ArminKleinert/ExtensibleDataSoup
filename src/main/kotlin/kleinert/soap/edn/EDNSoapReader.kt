package kleinert.soap.edn

import kleinert.soap.data.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.LinkedHashMap

class EDNSoapReader private constructor(
    private val options: EDNSoapOptions = EDNSoapOptions.extendedOptions,
    private val cpi: CodePointIterator
) {
    companion object {
        private val NOTHING = object {}

        @Throws(EdnReaderException::class)
        internal fun read(cpi: CodePointIterator, options: EDNSoapOptions = EDNSoapOptions.defaultOptions): Any? =
            EDNSoapReader(options, cpi).readString()
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

    private fun ednReaderException(text: String, ex: Exception? = null) =
        EdnReaderException(text, ex, cpi.lineIdx, cpi.textIndex)


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

    private fun readString(): Any? {
        if (!cpi.hasNext())
            throw ednReaderException("Empty input string.")

        val data = readForm(0)
        if ((data as Collection<*>).size != 1)
            throw ednReaderException("The string should only contain one expression, but there are multiple.")
        return data.first()
    }

    private fun parseNumber(): Number {
        try {
            return parseNumberHelper()
        } catch (ex: NumberFormatException) {
            // For UUID.fromString
            throw ednReaderException(ex.toString(), ex)
        }
    }

    private fun parseNumberHelper(negate: Boolean = false): Number {
        val token = readToken(::isNotBreakingSymbol)

        val floatyRegex = Regex("[+\\-]?[0-9]*\\.?[0-9]+([eE][+\\-][0-9]+)?M?")
        val intRegex = Regex("[+\\-]?(0[obx])?[0-9a-fA-F]+N?")
        val ratioRegex = Regex("[+\\-]?[0-9]+/[0-9]+?")

        val expandedIntRegex = Regex("[+\\-]?(0[obx])?[0-9a-fA-F]+(N|_i8|_i16|_i32|_i64|L)?")
        val complexRegex = Regex("([+\\-]?\\d+(.\\d)?+i)|([+\\-]?\\d+(.\\d+)?[+\\-](\\d+(.\\d+)?)?i)")

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
            return try {
                Complex.valueOf(token)
            } catch (nfe: NumberFormatException) {
                throw ednReaderException(nfe.toString(), nfe)
            }
        } else {
            throw ednReaderException("Invalid number format: $token")
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

    private fun parseComment() {
        cpi.skipLine()
    }

    private fun parseString(): String {
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
                        'u'.code -> currentToken.append(Char(parseUnicodeChar(4, 4, 'u'))) // UTF-16 code
                        'x'.code -> currentToken.appendCodePoint(parseUnicodeChar(8, 8, 'x')) // UTF-32 code
                        else ->
                            throw ednReaderException("Invalid escape sequence: \\${codePt2.toChar()} in string $currentToken")
                    }
                }

                else -> currentToken.appendCodePoint(codePoint)
            }
        }

        throw ednReaderException("Unclosed String literal \"$currentToken\" .")
    }

    private fun parseUnicodeChar(
        minLength: Int,
        maxLength: Int,
        initChar: Char
    ): Int {
        val token = cpi.takeCodePoints(StringBuilder(), maxLength, ::isHexNum)
        if (token.length < minLength || token.length > maxLength)
            throw ednReaderException("Invalid unicode sequence \\$initChar$token")
        return parseUnicodeChar(token, 16, initChar)
    }

    private fun parseUnicodeChar(token: CharSequence, base: Int, initChar: Char): Int {
        try {
            var code = 0
            for (it in token) code = code * base + it.digitToInt(base)
            return code
        } catch (nfe: NumberFormatException) {
            throw ednReaderException(nfe.message ?: "Invalid unicode sequence \\$initChar$token")
        }
    }

    private fun parseList(level: Int): List<Any?> {
        val temp = parseVector(level, ')'.code, false)
        return options.listToPersistentListConverter(temp)
    }

    private fun parseVector(level: Int, separator: Int, doConvert: Boolean = true): List<*> =
        buildList {
            do {
                cpi.skipWhile(::isWhitespace)

                if (!cpi.hasNext())
                    throw ednReaderException("Unclosed list. Expected '${Char(separator)}', got EOF.")

                if (cpi.peek() == separator) {
                    cpi.nextInt()
                    break
                }

                val elem = readForm(level + 1, true)

                if (elem != NOTHING) add(elem)
            } while (true)
        }.toList().let {
            if (doConvert) options.listToPersistentVectorConverter(it) else it
        }

    private fun parseMap(level: Int, separator: Int = '}'.code): Map<*, *> {
        val result = LinkedHashMap<Any?, Any?>()
        val lst = parseVector(level, separator)
        var i = 0
        do {
            if (i >= lst.size)
                break
            val key = lst[i]
            if (result.contains(key))
                throw ednReaderException("Illegal map. Duplicate key $key.")
            i++
            if (i >= lst.size)
                throw ednReaderException("Odd number of elements in map. Last key was $key.")
            val value = lst[i]
            result[key] = value
            i++
        } while (true)
        return options.mapToPersistentMapConverter(result)
    }

    private fun parseSet(level: Int, separator: Int = '}'.code): Set<*> {
        val result = LinkedHashSet<Any?>()
        val lst = parseVector(level, separator)
        var i = 0
        do {
            if (i >= lst.size)
                break
            val key = lst[i]
            if (result.contains(key))
                throw ednReaderException("Illegal set. Duplicate value $key.")
            result.add(key)
            i++
        } while (true)
        return options.setToPersistentSetConverter(result)
    }

    private fun parseChar(): Char {
        val token = readToken(::isNotBreakingSymbol)
        return Char(parseDispatchUnicodeChar(token, false))
    }

    private fun parseDispatchUnicodeChar(
        initialToken: CharSequence,
        isDispatch: Boolean = false
    ): Int {
        val token =
            if (initialToken.isEmpty() && cpi.hasNext()) cpi.takeCodePoints(StringBuilder(), 1, ::isValidCharSingle)
            else initialToken

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
                    throw ednReaderException(msg)
                }
                return parseUnicodeChar(reducedToken, 8, 'o')
            }

            'u' -> { // UTF-16 or UTF-32 code
                if (reducedToken.length == 4 || (isDispatch && reducedToken.length == 8))
                    return parseUnicodeChar(reducedToken, 16, 'u')
                val msg =
                    "Invalid length of unicode sequence ${reducedToken.length} in char literal $errorTokenText (should be 4 or 8)"
                throw ednReaderException(msg)
            }

            'x' -> { // UTF-32 code
                if (!options.allowSchemeUTF32Codes)
                    throw ednReaderException("Invalid char literal: $errorTokenText")
                if (reducedToken.length == 8)
                    return parseUnicodeChar(reducedToken, 16, 'x')
                val msg =
                    "Invalid length of unicode sequence ${reducedToken.length} in char literal $errorTokenText (should be 8)"
                throw ednReaderException(msg)

            }

            else -> throw ednReaderException("Invalid char literal $errorTokenText")
        }
    }

    private fun parseDispatch(level: Int): Any? {
        val token = cpi.takeCodePoints(StringBuilder(), ::isNotBreakingSymbolOrDispatch)

        if (token.isEmpty()) { // Next symbol was a breaking symbol (whitespace, bracket, etc.) or EOF
            if (!cpi.hasNext())
                throw ednReaderException("Invalid dispatch expression #$token.")

            when (val code = cpi.nextInt()) {
                '{'.code -> token.appendCodePoint(code)
                '#'.code -> token.appendCodePoint(code)
                else -> {
                    throw ednReaderException("Invalid dispatch expression $token.")
                }
            }
        }

        when (token[0]) {
            '\\' ->
                if (options.allowDispatchChars) {
                    val subToken = token.subSequence(1, token.length)
                    val uniChar = parseDispatchUnicodeChar(subToken, true)
                    return StringBuilder()
                        .appendCodePoint(uniChar)
                        .toString()
                }

            '{' -> return parseSet(level + 1)

            '#' -> {
                cpi.takeCodePoints(token, ::isNotBreakingSymbolOrDispatch)

                return when (val tokenAsString = "#$token") {
                    "##NaN" -> Double.NaN
                    "##-NaN" -> -Double.NaN
                    "##INF" -> Double.POSITIVE_INFINITY
                    "##-INF" -> Double.NEGATIVE_INFINITY
                    "##time" ->
                        if (options.allowTimeDispatch) LocalDateTime.now()
                        else throw ednReaderException("Unknown symbolic value $tokenAsString")

                    else -> throw ednReaderException("Unknown symbolic value $tokenAsString")
                }
            }
        }

        val tokenAsString = token.toString()

        if (tokenAsString == "uuid" || tokenAsString == "inst")
            return parseDecode(level + 1, tokenAsString, null)

        val decoder = options.ednClassDecoders[tokenAsString]
        if (decoder != null) {
            return parseDecode(level + 1, tokenAsString, decoder)
        }

        throw ednReaderException("Invalid dispatch expression #$tokenAsString.")
    }

    private fun parseDecode(level: Int, token: CharSequence, decoder: ((Any?) -> Any?)?): Any? {
        val form = readForm(level + 1, stopAfterOne = true)

        if (token == "uuid" || token == "inst") {
            try {
                if (form !is CharSequence)
                    throw ednReaderException("Dispatch decoder $token requires a string as input but got $form.")
                return when (token) {
                    "uuid" -> UUID.fromString(form.toString())
                    "inst" -> Instant.parse(form)
                    else -> throw NotImplementedError("Not implemented.")
                }
            } catch (ex: IllegalArgumentException) {
                // For UUID.fromString
                throw ednReaderException("Dispatch parsing of operation $token failed.", ex)
            } catch (ex: NumberFormatException) {
                // For UUID.fromString
                throw ednReaderException("Dispatch parsing of operation $token failed.", ex)
            } catch (ex: DateTimeParseException) {
                // For Instant.parse.
                throw ednReaderException("Dispatch parsing of operation $token failed.", ex)
            }
        }

        return decoder!!(form)
    }

    private fun parseOther(level: Int): Any? {
        val token = cpi.takeCodePoints(StringBuilder(), ::isValidSymbolChar).toString()

        if (token.length > 1)
            when (token[0]) {
                ':' -> return Keyword.parse(token, options.allowUTFSymbols)
                    ?: throw ednReaderException("Token starts with colon, but is not a valid keyword: $token")
            }

        if (token[0] == ':') throw ednReaderException("Lonely colon.")

        return when (token) {
            "nil" -> null
            "true" -> true
            "false" -> false
            else -> Symbol.parse(token, options.allowUTFSymbols)
                ?: throw ednReaderException("Invalid symbol: $token")
        }
    }

    private fun readForm(level: Int, stopAfterOne: Boolean = false): Any? {
        val res = mutableListOf<Any?>()
        do {
            cpi.skipWhile(::isWhitespace)

            if (!cpi.hasNext()) {
                //throw EdnReaderException.EdnEmptyInputException("Expected anything but got EOF.")
                break
            }

            when (val codePoint = cpi.nextInt()) {
                ';'.code -> {
                    parseComment()
                    continue
                }

                '"'.code -> res.add(parseString())
                '('.code -> res.add(parseList(level + 1))
                '['.code -> res.add(parseVector(level + 1, ']'.code))
                '{'.code -> res.add(parseMap(level + 1, '}'.code))
                '\\'.code -> res.add(parseChar())
                '#'.code -> {
                    if (cpi.hasNext() && cpi.peek() == '_'.code) {
                        cpi.nextInt()
                        do {
                            val temp = readForm(level + 1, true)
                        } while (temp == NOTHING)
                        //res.add(readForm(cpi, level))
                        if (stopAfterOne) return NOTHING
                    } else {
                        res.add(parseDispatch(level + 1))
                    }
                }

                '0'.code, '1'.code, '2'.code, '3'.code, '4'.code, '5'.code, '6'.code, '7'.code, '8'.code, '9'.code -> {
                    cpi.unread(codePoint)
                    res.add(parseNumber())
                }

                ')'.code, ']'.code, '}'.code ->
                    if (level == 0) throw ednReaderException("Unexpected character ${Char(codePoint)}.")
                    else {
                        cpi.unread(codePoint); return NOTHING
                    }

                '+'.code -> {
                    val isNumber = cpi.hasNext() && cpi.peek() in '0'.code..'9'.code
                    cpi.unread(codePoint)
                    res.add(if (isNumber) parseNumber() else parseOther(level + 1))
                }

                '-'.code -> {
                    val isNumber = cpi.hasNext() && cpi.peek() in '0'.code..'9'.code
                    cpi.unread(codePoint)
                    res.add(if (isNumber) parseNumber() else parseOther(level + 1))
                }

                else -> {
                    cpi.unread(codePoint)
                    res.add(parseOther(level + 1))
                }
            }

            if (stopAfterOne && res.isNotEmpty()) {
                return res[0]
            }
        } while (true)

        if (res.size != 1) {
            throw ednReaderException("Reader requires exactly one expression, but got ${res.size}.")
        }
        return res
    }

    private fun readToken(condition: (Int) -> Boolean): String {
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
