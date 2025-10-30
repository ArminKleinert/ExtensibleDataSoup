package kleinert.edn.reader

import kleinert.edn.EDNSoupOptions
import kleinert.edn.data.*
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * TODO
 *
 * @author Armin Kleinert
 */
class EDNSoupReader private constructor(
    private val options: EDNSoupOptions = EDNSoupOptions.extendedOptions, private val cpi: CodePointIterator
) {
    companion object {
        private val NOTHING = object {}

        @Throws(EdnReaderException::class)
        internal fun read(cpi: CodePointIterator, options: EDNSoupOptions = EDNSoupOptions.defaultOptions): Any? =
            EDNSoupReader(options, cpi).readString()
    }

    init {
        ensureValidDecoderNames()
    }

    /*
     + Decoders (tags) must follow the same naming conventions as symbols AND must have a namespace part.
     */
    private fun ensureValidDecoderNames() {
        for (key in options.ednClassDecoders.keys) {
            val name = Symbol.parse(key)
            if (name == null) {
                val message = "Decoder name \"$key\" is not a valid symbol."
                throw EdnReaderException(cpi.lineIdx, cpi.textIndex, message)
            }
            if (!options.allowMoreEncoderDecoderNames && name.namespace == null) {
                val message = "Decoder without namespace: $name"
                throw EdnReaderException(cpi.lineIdx, cpi.textIndex, message)
            }
        }
    }

    private fun readString(): Any? {
//        if (!cpi.hasNext())
//            throw EdnReaderException(cpi.lineIdx, cpi.textIndex, "Empty input string.")

        val data = readForm(0)
        if ((data as Collection<*>).size != 1)
            throw EdnReaderException(
                cpi.lineIdx, cpi.textIndex, "The string should only contain one expression, but there are multiple."
            )
        return data.first()
    }

    private fun readNumber(): Number {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        try {
            return readNumberHelper(codePosIndex, linePos)
        } catch (ex: NumberFormatException) {
            throw EdnReaderException(codePosIndex, linePos, null, ex)
        }
    }

    private fun readNumberHelper(codePosIndex: Int, linePos: Int): Number {
        val token = readToken(::isNotBreakingSymbol)

        val floatyRegex = Regex("[+\\-]?[0-9]*\\.?[0-9]+([eE][+\\-][0-9]+)?M?")
        val intRegex = Regex("[+\\-]?(0[obx])?[0-9a-fA-F]+N?")
        val ratioRegex = Regex("[+\\-]?[0-9]+/[0-9]+?")

        val expandedIntRegex = Regex("[+\\-]?(0[obx])?[0-9a-fA-F]+(N|_i8|_i16|_i32|_i64|L)?")

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
                    'o' -> if (options.moreNumberPrefixes) 8 else throw EdnReaderException(
                        linePos, codePosIndex, "Invalid number prefix in number $token."
                    )

                    'b' -> if (options.moreNumberPrefixes) 2 else throw EdnReaderException(
                        linePos, codePosIndex, "Invalid number prefix in number $token."
                    )

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
        } else {
            throw EdnReaderException(linePos, codePosIndex, "Invalid number format: $token")
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

    private fun readComment() {
        cpi.skipLine()
    }

    private fun readEdnString(): String {
        val currentToken = StringBuilder()

        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex

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
                        'u'.code -> currentToken.append(readUnicodeChar(4, 4, 'u').toChar()) // UTF-16 code
                        'x'.code -> currentToken.appendCodePoint(readUnicodeChar(8, 8, 'x').code) // UTF-32 code
                        else -> {
                            val message = "Invalid escape sequence: \\${codePt2.toChar()} in string $currentToken"
                            throw EdnReaderException(cpi.lineIdx, cpi.textIndex, message)
                        }
                    }
                }

                else -> currentToken.appendCodePoint(codePoint)
            }
        }

        throw EdnReaderException(linePos, codePosIndex, "Unclosed String literal \"$currentToken\".")
    }

    private fun readUnicodeChar(minLength: Int, maxLength: Int, initChar: Char): Char32 {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        val token = cpi.takeCodePoints(StringBuilder(), maxLength, ::isHexNum)
        if (token.length < minLength || token.length > maxLength)
            throw EdnReaderException(linePos, codePosIndex, "Invalid unicode sequence \\$initChar$token")
        return readUnicodeChar(token, 16, initChar)
    }

    private fun readUnicodeChar(token: CharSequence, base: Int, initChar: Char): Char32 {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        try {
            var code = 0
            for (it in token) code = code * base + it.digitToInt(base)
            return Char32(code)
        } catch (nfe: NumberFormatException) {
            throw EdnReaderException(linePos, codePosIndex, nfe.message ?: "Invalid unicode sequence \\$initChar$token")
        }
    }

    private fun readList(level: Int): List<Any?> {
        val temp = readVector(level, ')'.code, false)
        return options.listToPersistentListConverter(temp)
    }

    private fun readVector(level: Int, separator: Int, doConvert: Boolean = true): List<*> =
        buildList {
            val linePos = cpi.lineIdx
            do {
                cpi.skipWhile(::isWhitespace)

                if (!cpi.hasNext())
                    throw EdnReaderException(
                        cpi.lineIdx, cpi.textIndex,
                        "Unclosed list started in line $linePos. Expected '${Char(separator)}', got EOF."
                    )

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

    private fun readMap(level: Int, separator: Int = '}'.code): Map<*, *> {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        val result = LinkedHashMap<Any?, Any?>()
        val lst = readVector(level, separator)
        var i = 0
        do {
            if (i >= lst.size)
                break
            val key = lst[i]
            if (result.contains(key))
                throw EdnReaderException(linePos, codePosIndex, "Illegal map. Duplicate key $key.")
            i++
            if (i >= lst.size)
                throw EdnReaderException(linePos, codePosIndex, "Odd number of elements in map. Last key was $key.")
            val value = lst[i]
            result[key] = value
            i++
        } while (true)
        return options.mapToPersistentMapConverter(result)
    }

    private fun readSet(level: Int, separator: Int = '}'.code): Set<*> {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        val result = LinkedHashSet<Any?>()
        val lst = readVector(level, separator)
        var i = 0
        do {
            if (i >= lst.size)
                break
            val key = lst[i]
            if (result.contains(key))
                throw EdnReaderException(linePos, codePosIndex, "Illegal set. Duplicate value $key.")
            result.add(key)
            i++
        } while (true)
        return options.setToPersistentSetConverter(result)
    }

    private fun readChar(): Char {
        val token = readToken(::isNotBreakingSymbol)
        return readDispatchUnicodeChar(token, false).toChar()
    }

    private fun readDispatchUnicodeChar(
        initialToken: CharSequence,
        isDispatch: Boolean = false
    ): Char32 {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex

        val token =
            if (initialToken.isEmpty() && cpi.hasNext()) cpi.takeCodePoints(StringBuilder(), 1, ::isValidCharSingle)
            else initialToken

        if (token.length == 1)
            return Char32(token[0].code)

        when (token) {
            "newline" -> return Char32('\n'.code)
            "space" -> return Char32(' '.code)
            "tab" -> return Char32('\t'.code)
            "backspace" -> return Char32('\b'.code)
            "formfeed" -> return Char32(12) // '\f'
            "return" -> return Char32('\r'.code)
        }

        val reducedToken = token.subSequence(1, token.length)

        val errorTokenText = if (isDispatch) "#\\$token" else "\\$token"
        when (token[0]) {
            'o' -> {
                if (reducedToken.length <= 1 || reducedToken.length > 3) {
                    val msg =
                        "Invalid length of unicode sequence ${reducedToken.length} in sequence $errorTokenText (should be 4)."
                    throw EdnReaderException(linePos, codePosIndex, msg)
                }
                return readUnicodeChar(reducedToken, 8, 'o')
            }

            'u' -> { // UTF-16 or UTF-32 code
                if (reducedToken.length == 4 || (isDispatch && reducedToken.length == 8))
                    return readUnicodeChar(reducedToken, 16, 'u')
                val msg =
                    "Invalid length of unicode sequence ${reducedToken.length} in char literal $errorTokenText (should be 4 or 8)."
                throw EdnReaderException(linePos, codePosIndex, msg)
            }

            'x' -> { // UTF-32 code
                if (!options.allowSchemeUTF32Codes)
                    throw EdnReaderException(linePos, codePosIndex, "Invalid char literal: $errorTokenText")
                if (reducedToken.length == 8)
                    return readUnicodeChar(reducedToken, 16, 'x')
                val msg =
                    "Invalid length of unicode sequence ${reducedToken.length} in char literal $errorTokenText (should be 8)."
                throw EdnReaderException(linePos, codePosIndex, msg)

            }

            else -> throw EdnReaderException(linePos, codePosIndex, "Invalid char literal $errorTokenText")
        }
    }

    private fun readDispatch(level: Int): Any? {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        val token = cpi.takeCodePoints(StringBuilder(), ::isNotBreakingSymbolOrDispatch)

        if (token.isEmpty()) { // Next symbol was a breaking symbol (whitespace, bracket, etc.) or EOF
            if (!cpi.hasNext())
                throw EdnReaderException(linePos, codePosIndex, "Invalid dispatch expression #$token.")

            when (val code = cpi.nextInt()) {
                '{'.code -> token.appendCodePoint(code)
                '#'.code -> token.appendCodePoint(code)
                else -> throw EdnReaderException(linePos, codePosIndex, "Invalid dispatch expression $token.")
            }
        }

        when (token[0]) {
            '\\' ->
                if (options.allowDispatchChars) {
                    val subToken = token.subSequence(1, token.length)
                    return readDispatchUnicodeChar(subToken, true)
                }

            '{' -> return readSet(level + 1)

            '#' -> {
                cpi.takeCodePoints(token, ::isNotBreakingSymbolOrDispatch)

                return when (val tokenAsString = "#$token") {
                    "##NaN" -> Double.NaN
                    "##-NaN" -> -Double.NaN
                    "##INF" -> Double.POSITIVE_INFINITY
                    "##-INF" -> Double.NEGATIVE_INFINITY
                    else -> throw EdnReaderException(linePos, codePosIndex, "Unknown symbolic value $tokenAsString")
                }
            }
        }

        val tokenAsString = token.toString()
        return readDecode(level + 1, tokenAsString)
    }

    private fun readDecode(level: Int, token: CharSequence): Any? {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        val form = readForm(level + 1, stopAfterOne = true)
        try {
            if (token == "uuid") {
                if (form !is CharSequence) {
                    val msg = "Dispatch decoder $token requires a string as input but got $form."
                    throw EdnReaderException(linePos, codePosIndex, msg)
                }
                return UUID.fromString(form.toString())
            } else if (token == "inst") {
                if (form !is CharSequence) {
                    val msg = "Dispatch decoder $token requires a string as input but got $form."
                    throw EdnReaderException(linePos, codePosIndex, msg)
                }
                return Instant.parse(form)
            } else {
                val decoder = options.ednClassDecoders[token]
                if (decoder != null) return decoder(form)
            }
        } catch (ex: EdnReaderException.EdnClassConversionError) {
            throw EdnReaderException.EdnClassConversionError(linePos, codePosIndex, ex.message ?: "", ex.cause)
        } catch (ex: IllegalArgumentException) {
            // For UUID.fromString
            throw EdnReaderException.EdnClassConversionError(linePos, codePosIndex, null, ex)
        } catch (ex: NumberFormatException) {
            // For UUID.fromString
            throw EdnReaderException.EdnClassConversionError(linePos, codePosIndex, null, ex)
        } catch (ex: DateTimeParseException) {
            // For Instant.parse.
            throw EdnReaderException.EdnClassConversionError(linePos, codePosIndex, null, ex)
        }
        throw EdnReaderException(linePos, codePosIndex, "Invalid dispatch expression #$token.")
    }

    private fun readOther(): Any? {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        val token = cpi.takeCodePoints(StringBuilder(), ::isValidSymbolChar).toString()

        if (token.length > 1)
            when (token[0]) {
                ':' -> return Keyword.parse(token, options.allowUTFSymbols)
                    ?: throw EdnReaderException(
                        linePos, codePosIndex, "Token starts with colon, but is not a valid keyword: $token"
                    )
            }

        if (token[0] == ':') throw EdnReaderException(linePos, codePosIndex, "Lonely colon.")

        return when (token) {
            "nil" -> null
            "true" -> true
            "false" -> false
            else -> Symbol.parse(token, options.allowUTFSymbols)
                ?: throw EdnReaderException(linePos, codePosIndex, "Invalid symbol: $token")
        }
    }

    private fun readForm(level: Int, stopAfterOne: Boolean = false): Any? {
        val res = mutableListOf<Any?>()
        var linePos: Int = cpi.lineIdx
        var codePosIndex: Int = cpi.textIndex
        do {
            cpi.skipWhile(::isWhitespace)

            if (!cpi.hasNext())
                break

            linePos = cpi.lineIdx
            codePosIndex = cpi.textIndex

            when (val codePoint = cpi.nextInt()) {
                ';'.code -> {
                    readComment()
                    continue
                }

                '"'.code -> res.add(readEdnString())
                '('.code -> res.add(readList(level + 1))
                '['.code -> res.add(readVector(level + 1, ']'.code))
                '{'.code -> res.add(readMap(level + 1, '}'.code))
                '\\'.code -> res.add(readChar())
                '#'.code -> {
                    if (cpi.hasNext() && cpi.peek() == '_'.code) {
                        cpi.nextInt()
                        do {
                            val temp = readForm(level + 1, true)
                        } while (temp == NOTHING)
                        if (stopAfterOne) return NOTHING
                    } else if (cpi.hasNext() && cpi.peek() == '^'.code) {
                        cpi.nextInt()
                        res.add(readMeta(level))
                    } else {
                        res.add(readDispatch(level + 1))
                    }
                }

                '0'.code, '1'.code, '2'.code, '3'.code, '4'.code, '5'.code, '6'.code, '7'.code, '8'.code, '9'.code -> {
                    cpi.unread(codePoint)
                    res.add(readNumber())
                }

                ')'.code, ']'.code, '}'.code ->
                    if (level == 0)
                        throw EdnReaderException(linePos, codePosIndex, "Unexpected character ${Char(codePoint)}.")
                    else {
                        cpi.unread(codePoint); return NOTHING
                    }

                '+'.code, '-'.code -> {
                    val isNumber = cpi.hasNext() && cpi.peek() in '0'.code..'9'.code
                    cpi.unread(codePoint)
                    res.add(if (isNumber) readNumber() else readOther())
                }

                '^'.code -> {
                    res.add(readMeta(level))
                }

                else -> {
                    cpi.unread(codePoint)
                    res.add(readOther())
                }
            }

            if (stopAfterOne && res.isNotEmpty()) {
                return res[0]
            }
        } while (true)

        if (res.size != 1)
            throw EdnReaderException(
                linePos, codePosIndex, "Reader requires exactly one expression, but got ${res.size}."
            )

        return res
    }

    private fun readMeta(level: Int): IObj<Any?> {
        val linePos = cpi.lineIdx
        val codePosIndex = cpi.textIndex
        val meta = when (val m = readForm(level, true)) {
            is String, is Symbol -> PersistentMap(mapOf(Keyword["tag"] to m))
            is Keyword -> PersistentMap(mapOf(m to true))
            !is Map<*, *> -> throw EdnReaderException(
                linePos, codePosIndex, "Metadata must be Symbol,Keyword,String or Map"
            )

            else -> m
        }

        val obj = readForm(level, true)
        if (obj === NOTHING)
            throw EdnReaderException(linePos, codePosIndex, "Required object for metadata, but got nothing.")

        return IObj(meta, obj)
    }

    private fun readToken(condition: (Int) -> Boolean): String {
        val currentToken = StringBuilder()
        cpi.takeCodePoints(currentToken) { condition(it) }
        return currentToken.toString()
    }

    private fun isWhitespace(code: Int): Boolean =
        if (code < Char.MIN_VALUE.code || code > Char.MAX_VALUE.code) false
        else Char(code).isWhitespace() || code == ','.code

    private fun isValidCharSingle(code: Int): Boolean =
        if (code < Char.MIN_VALUE.code || code > Char.MAX_VALUE.code) false
        else !Char(code).isWhitespace()

    private fun isValidSymbolChar(code: Int): Boolean = when (code) {
        ' '.code, '\t'.code, '\n'.code, '\r'.code, '['.code, ']'.code, '('.code, ')'.code, '{'.code, '}'.code,
        '"'.code -> false

        else -> true
    }

    private fun isNotBreakingSymbol(code: Int): Boolean = when (code) {
        ' '.code, '\t'.code, '\n'.code, '\r'.code, '['.code, ']'.code, '('.code, ')'.code, '{'.code, '}'.code,
        '\''.code, '"'.code, ';'.code, ','.code -> false

        else -> true
    }

    private fun isNotBreakingSymbolOrDispatch(code: Int): Boolean {
        return isNotBreakingSymbol(code) && code != '#'.code
    }

//    private fun isAlphaNum(code: Int): Boolean = when (code.toChar()) {
//        in 'a'..'z', in 'A'..'Z', in '0'..'9' -> true
//        else -> false
//    }

    private fun isHexNum(code: Int): Boolean = when (code) {
        in 'a'.code..'f'.code, in 'A'.code..'F'.code, in '0'.code..'9'.code -> true
        else -> false
    }


}
