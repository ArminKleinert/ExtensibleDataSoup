import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.collections.RandomAccess
import kotlin.text.Regex


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

class EDNSoapReader private constructor(private val tokens: Iterator<String>, private val options: EDNSoapOptions) {
    companion object {
        private const val NULL_CHAR = '\u0000'

        @Throws(EdnReaderException::class)
        fun readString(input: String, options: EDNSoapOptions = EDNSoapOptions.extendedOptions): Any? {
            val tokens = tokenize(input, options)
            return EDNSoapReader(tokens, options).readForm(false)
        }

        @Throws(EdnReaderException::class)
        fun readFile(input: File, options: EDNSoapOptions = EDNSoapOptions.extendedOptions): Any? {
            val tokens = tokenize(input.readText(), options)
            return EDNSoapReader(tokens, options).readForm(false)
        }

        fun keyword(s: String) = Keyword.keyword(s)
        fun symbol(s: String) = Symbol.symbol(s)

        private fun tokenize(s: String, options: EDNSoapOptions): Iterator<String> {
            val tokens = mutableListOf<String>()
            val currentToken = StringBuilder()
            var isInString = false
            val chars = s.iterator()
            var isStringEscaped = false
            var lineCommented = false

            while (chars.hasNext()) {
                val ch = chars.next()

                if (isInString) {
                    currentToken.append(ch)
                    if (ch == '\\') {
                        isStringEscaped = true
                        continue
                    }
                    if (ch != '"')
                        continue
                    if (isStringEscaped) {
                        isStringEscaped = false
                        continue
                    }
                    val t = currentToken.toString().replace("\\n", "\n").replace("\\t", "\t")
                    tokens.add(t)
                    currentToken.clear()
                    isInString = false
                    continue
                }

                val currentAsString = currentToken.toString()
                if (currentAsString == "{" || currentAsString == "[" || currentAsString == "(") {
                    tokens.add(currentAsString)
                    currentToken.clear()
                    currentToken.append(ch)
                    continue
                }

                if (lineCommented) {
                    if (ch == '\n') {
                        lineCommented = false
                    }
                    continue
                }

                when (ch) {
                    '"' -> {
                        tokens.add(currentToken.toString())
                        isInString = true
                        currentToken.clear()
                        currentToken.append(ch)
                    }

                    ';' -> {
                        lineCommented = true
                    }

                    ' ', '\n', '\t', ',' -> {
                        tokens.add(currentToken.toString())
                        tokens.add(ch.toString())
                        currentToken.clear()
                    }

                    '(', '[' -> {
                        val t = currentToken.toString()
                        if (options.useCollectionPrefixes && t == "#a") {
                            tokens.add("$t$ch") // Extended EDN only
                        } else {
                            tokens.add(currentToken.toString())
                            tokens.add(ch.toString())
                        }
                        currentToken.clear()
                    }

                    '{' -> {
                        val t = currentToken.toString()
                        if (t == "#") {
                            tokens.add("#{")
                            currentToken.clear()
                        } else if (options.useCollectionPrefixes && (t == "#a" || t == "##a")) {
                            tokens.add("$t{")
                            currentToken.clear()
                        } else {
                            tokens.add(currentToken.toString())
                            tokens.add(ch.toString())
                            currentToken.clear()
                        }
                    }

                    ')', ']', '}' -> {
                        tokens.add(currentToken.toString())
                        currentToken.clear()
                        tokens.add(ch.toString())
                    }

                    else -> {
                        currentToken.append(ch)
                    }
                }
            }

            if (currentToken.isNotEmpty())
                tokens.add(currentToken.toString())

            return tokens.filter { it.isNotBlank() }.iterator()
        }
    }

    private val numberRegex = Regex("^[-+]?[0-9]*(\\.)?[0-9]+\$")

    private var current = advance()

    private fun next(): String? {
        val result = current
        current = advance()
        return result
    }

    private fun peek(): String? = current
    private fun advance(): String? = if (tokens.hasNext()) tokens.next() else null

    @Throws(EdnReaderException::class)
    private fun readForm(discard: Boolean): Any? {
        val p = peek()
        if (options.useCollectionPrefixes && p is String && p.first() == '#') {
            val l = p.last()
            // Code for prefixed collections.
            if (l == '(' || l == '[' || l == '{')
                return readSpecialForm(p,discard)

            // Special forms for direct conversion #...
            if (p.length > 1 && p.drop(1).all { it.isLetterOrDigit() }) {
                val typeName = p.substring(1)
                require(options.ednClassDecoders.containsKey(typeName))
                next()
                val form = readForm(discard)
                return options.ednClassDecoders[typeName]!!(form)
            }
        }
        return when (p) {
            null -> null
            "#_" -> readForm(true)
            "(" -> readList(discard)
            ")" -> throw EdnReaderException("expected form, got ')'")
            "[" -> readVector(discard)
            "]" -> throw EdnReaderException("expected form, got ']'")
            "#{" -> readSet(discard,false)
            "{" -> readMap(discard,false)
            "}" -> throw EdnReaderException("expected form, got '}'")
            else -> readAtom(discard)
        }
    }


    /**
     * Possible prefixes:
     * For lists, vectors, and maps:
     * - #a : Load list as lazy sequence, vector as array, or Load maps as ordered maps
     * For sets (prefix with double #):
     * - ##a : Load as ordered set
     */
    @Throws(EdnReaderException::class, EdnClassConversionError::class)
    private fun readSpecialForm(p: String, discard: Boolean): Any {
        require(options.useCollectionPrefixes)
        return when (p) {
            "#a(" -> readList(discard).asSequence() // Array
            "#a[" -> readTypedArray(discard) // Array
            "##a{" -> readSet(discard, true)
            "#a{" -> readMap(discard, true)
            else -> throw EdnReaderException("Invalid special form prefix $p")
        }
    }

    @Throws(EdnReaderException::class, EdnClassConversionError::class)
    private fun readList(discard: Boolean): Iterable<Any?> = readSequence(discard,")")

    @Throws(EdnReaderException::class, EdnClassConversionError::class)
    private fun readVector(discard: Boolean): List<Any?> = readSequence(discard,"]")

    @Throws(EdnReaderException::class, EdnClassConversionError::class)
    private fun readSequence(discard: Boolean,seqTerminator: String): List<Any?> = buildList {
        next()
        do {
            val form = when (peek()) {
                null -> throw EdnReaderException("Expected '$seqTerminator', got EOF")

                seqTerminator -> {
                    next(); break
                }

                else -> readForm(discard)
            }
            add(form)
        } while (form != null)
    }


    @Throws(EdnReaderException::class, EdnClassConversionError::class)
    private fun readSet(discard: Boolean, forceOrdered: Boolean = options.useCompacts): Set<Any?> {
        next()
        val memory = mutableSetOf<Any?>()
        val useOrdered = (forceOrdered || options.useCompacts)
        val l = buildList {
            do {
                val form = when (peek()) {
                    null -> throw EdnReaderException("Expected '}', got EOF")
                    "}" -> {
                        next(); break
                    }

                    else -> readForm(discard)
                }
                if (!memory.add(form)) throw EdnReaderException("Duplicate value $form in Set.")
                if (useOrdered) add(form)
            } while (true)
        }

        if (forceOrdered || options.useCompacts)
            return SeqSet(l)

        return memory
    }

    @Throws(EdnReaderException::class, EdnClassConversionError::class)
    private fun readTypedArray(discard: Boolean): Any {
        next()
        val temp = mutableListOf<Any?>()
        do {
            val form = when (peek()) {
                null -> throw EdnReaderException("Expected ']', got EOF")
                "]" -> {
                    next(); break
                }

                else -> readForm(discard)
            }
            temp.add(form)
        } while (true)

        // Try to compress primitive array types. (byte, short, int, long)
        if (temp.all { it is Byte }) {
            return ByteArray(temp.size) { temp[it] as Byte }
        } else if (temp.all { it is Short }) {
            return ShortArray(temp.size) { temp[it] as Short }
        } else if (temp.all { it is Int }) {
            return IntArray(temp.size) { temp[it] as Int }
        } else if (temp.all { it is Long }) {
            return LongArray(temp.size) { temp[it] as Long }
        }

        // Try to compress primitive array types. (byte, short, int, long)
        if (temp.all { it is Float }) {
            return FloatArray(temp.size) { temp[it] as Float }
        } else if (temp.all { it is Double }) {
            return DoubleArray(temp.size) { temp[it] as Double }
        }

        if (temp.all { it is Keyword }) {
            return FloatArray(temp.size) { temp[it] as Float }
        } else if (temp.all { it is Symbol }) {
            return DoubleArray(temp.size) { temp[it] as Double }
        } else if (temp.all { it is String }) {
            return DoubleArray(temp.size) { temp[it] as Double }
        }

        // Try to compress primitive array types. (byte, short, int, long)
        if (temp.all { it is Boolean }) {
            return BooleanArray(temp.size) { temp[it] as Boolean }
        } else if (temp.all { it is Char }) {
            return CharArray(temp.size) { temp[it] as Char }
        }

        if (temp.all { it is List<*> }) {
            return Array(temp.size) { temp[it] as List<*> }
        } else if (temp.all { it is Set<*> }) {
            return Array(temp.size) { temp[it] as Set<*> }
        } else if (temp.all { it is Map<*, *> }) {
            return Array(temp.size) { temp[it] as Map<*, *> }
        } else if (temp.all { it is Iterable<*> }) {
            return Array(temp.size) { temp[it] as Iterable<*> }
        }

        return temp.toTypedArray()
    }

    @Throws(EdnReaderException::class, EdnClassConversionError::class)
    private fun readMap(discard: Boolean, forceOrdered: Boolean = options.useCompacts): Map<Any?, Any?> {
        next()
        val useOrdered = forceOrdered || options.useCompacts
        val temp = mutableListOf<Pair<Any?, Any?>>()
        val memo = mutableMapOf<Any?, Any?>()
        do {
            var value: Any?;
            val key = when (peek()) {
                null -> throw EdnReaderException("Expected '}', got EOF")
                "}" -> {
                    next(); break
                }

                else -> {
                    val key = readForm(discard)
                    value = readForm(discard)
                    key
                }
            }
            if (memo.containsKey(key)) throw EdnReaderException("Duplicate key $key.")
            memo[key] = value
            temp.add(key to value)
        } while (true)

        return if (useOrdered) SeqMap(temp) else memo
    }

    private fun tryConvertNumber(
        text: String,
    ): Number? {
        var startIndex = 0
        var endIndex = text.lastIndex
        var dotIndex = -1
        var base = -1 // If -1, then no changes have been made and 10 or the default from options can be used.

        if (text[0] == '+' || text[0] == '-')
            startIndex++

        if (options.allowNumberPrefixes && startIndex < endIndex && text[startIndex] == '0') {
            base = 8
            if (text.startsWith("0x", startIndex)) {
                base = 16; startIndex += 2
            } else if (text.startsWith("0b", startIndex)) {
                base = 2; startIndex += 2
            }
        }

        val anyIntegralPostfix = (options.intPostfix.let { it != NULL_CHAR && text.endsWith(it) })
                || (options.bytePostfix.let { it != NULL_CHAR && text.endsWith(it) })
                || (options.shortPostfix.let { it != NULL_CHAR && text.endsWith(it) })
                || (options.longPostfix.let { it != NULL_CHAR && text.endsWith(it) })
        val anyFloatPostfix = !anyIntegralPostfix
                && ((options.floatPostfix.let { it != NULL_CHAR && text.endsWith(it) })
                || (options.doublePostfix.let { it != NULL_CHAR && text.endsWith(it) }))
        val anyBigNumPostfix = (options.bigIntPostfix.let { it != NULL_CHAR && text.endsWith(it) })
                || (options.bigDecimalPostfix.let { it != NULL_CHAR && text.endsWith(it) })

        if (anyIntegralPostfix || anyFloatPostfix || anyBigNumPostfix) endIndex--

        println(
            "$text ${text.substring(startIndex, endIndex+1)} $startIndex $endIndex $dotIndex $base $anyIntegralPostfix $anyFloatPostfix"
        )

        if (startIndex > endIndex) return null

        for (i in startIndex..endIndex) {
            if (text[i] == '.' && dotIndex == -1) dotIndex = i
            else if (text[i] < '0' || text[i] > '9') return null
        }


        if (dotIndex != -1 && (dotIndex == endIndex || base != -1 || anyIntegralPostfix)) return null

        if (dotIndex != -1 || anyFloatPostfix || (options.bigIntPostfix.let { it != NULL_CHAR && text.endsWith(it) })) {
            val num: Number = text.substring(startIndex, endIndex + 1).toBigDecimal()
            if (options.doublePostfix.let { it != NULL_CHAR && text.endsWith(it) }) return num.toDouble()
            if (options.bigDecimalPostfix.let { it != NULL_CHAR && text.endsWith(it) }) return num
            return num.toDouble() // Default
        } else {
            val base1 = if (base != -1) base else options.defaultIntegralNumberBase
            val num: BigInteger = BigInteger(text.substring(startIndex, endIndex + 1), base1)

            if (options.intPostfix.let { it != NULL_CHAR && text.endsWith(it) }) return num.toInt()
            if (options.longPostfix.let { it != NULL_CHAR && text.endsWith(it) }) return num.toLong()
            if (options.bigIntPostfix.let { it != NULL_CHAR && text.endsWith(it) }) return num

            if (!options.automaticallyExtendNumbers) return num.toInt() // Default to int

            if (!options.longIsLowerExtendBound && num.toInt() != 0) return num.toInt()
            if (num.toLong() != 0L) return num.toLong()

            return num
        }

    }

    @Throws(EdnReaderException::class)
    private fun readAtom(discard: Boolean): Any? {
        val next = next() ?: throw EdnReaderException("Unexpected null token")

        val numOrNull = tryConvertNumber(next)

        if (numOrNull != null)
            return numOrNull

        return when {
            next.isEmpty() ->
                throw EdnReaderException("Unrecognized token: $next")

            next.startsWith(":") && next.length > 1 -> {
                keyword(next.substring(1))
            }

            next.startsWith('"') && next.endsWith('"') -> {
                next.substring(1, next.length - 1)
            }

            next == "nil" || (options.allowNilAsNull && next == "null") -> {
                null
            }

            next == "true" || (options.allowSchemeBoolLiterals && next == "#t") -> {
                true
            }

            next == "false" || (options.allowSchemeBoolLiterals && next == "#f") -> {
                false
            }

            next.startsWith('"') && !next.endsWith('"') -> {
                throw EdnReaderException("expected '\"', got EOF (token was '$next')")
            }

            next[0] == '\\' && next.length > 1 -> makeChar(next)

            !next.contains('"') -> {
                symbol(next)
            }

            else -> {
                throw EdnReaderException("Unrecognized token: $next")
            }
        }
    }

    private fun makeChar(next: String): Char {
        if (next.length == 2) {
            val c = next[1]
            if (!c.isISOControl() && !c.isWhitespace())
                return c
            throw EdnReaderException("Could not parse char literal $next")
        }

        val rest = next.substring(1)

        when (rest) {
            "space" -> return ' '
            "newline" -> return '\n'
            "return" -> return Char(0x0d)
            "tab" -> return '\t'
            "formfeed" -> return Char(0x0c)
            "backspace" -> return Char(0x08)
        }
        if (options.allowSchemeCharLiterals) {
            when (rest) {
                "nl", "lf" -> return '\n'
                "cr" -> return Char(0x0d)
                "ht" -> return '\t'
                "page" -> return Char(0x0c)
                "alarm" -> return Char(0x07)
                "escape", "esc" -> return Char(0x1b)
                "delete", "del" -> return Char(0x7f)
                "null" -> return Char(0x00)
            }
        }

        if (rest[0] == 'u' || (options.allowSchemeCharLiterals && rest[0] == 'x')) {
            if (rest.length > 5)
                throw EdnReaderException("Invalid unicode char literal $next")
            try {
                val n = rest.substring(1).toInt(16)
                return Char(n)
            } catch (e: NumberFormatException) {
                throw EdnReaderException("Invalid unicode char literal $next")
            }
        }

        throw EdnReaderException("Could not parse char literal $next")
    }
}

class EDNSoapWriter(private val options: EDNSoapOptions) {
    init {
        val encoders = options.ednClassEncoders

        fun requireEncoderUndef(cl: Class<*>) =
            require(!encoders.containsKey(cl)) { "Encoder map must not include entry for class $cl" }

        requireEncoderUndef(String::class.java)
        requireEncoderUndef(Char::class.java)
        requireEncoderUndef(Map::class.java)
        requireEncoderUndef(MutableMap::class.java)
        requireEncoderUndef(Set::class.java)
        requireEncoderUndef(MutableSet::class.java)
        requireEncoderUndef(List::class.java)
        requireEncoderUndef(MutableList::class.java)
        requireEncoderUndef(Symbol::class.java)
        requireEncoderUndef(Keyword::class.java)

        requireEncoderUndef(Byte::class.java)
        requireEncoderUndef(Short::class.java)
        requireEncoderUndef(Int::class.java)
        requireEncoderUndef(Long::class.java)
        requireEncoderUndef(BigInteger::class.java)
        requireEncoderUndef(Float::class.java)
        requireEncoderUndef(Double::class.java)
        requireEncoderUndef(BigDecimal::class.java)

        requireEncoderUndef(java.lang.Byte::class.java)
        requireEncoderUndef(java.lang.Short::class.java)
        requireEncoderUndef(java.lang.Integer::class.java)
        requireEncoderUndef(java.lang.Long::class.java)
        requireEncoderUndef(java.lang.Float::class.java)
        requireEncoderUndef(java.lang.Double::class.java)

        requireEncoderUndef(Boolean::class.java)
        requireEncoderUndef(java.lang.Boolean::class.java)
    }

    fun encode(obj: Any?, ignoreEncoderEntries: List<Class<*>> = listOf()): String {
        if (obj != null && options.ednClassEncoders.containsKey(obj.javaClass) && (obj.javaClass as Class<*>) !in ignoreEncoderEntries) {
            val encoderResult = options.ednClassEncoders[obj.javaClass]!!(obj)
            println("Encoder result: $encoderResult")
            if (encoderResult != null) {
                val prefix = if (encoderResult.first.isEmpty()) "" else "#'${encoderResult.first} "
                val data = if (encoderResult.second == null)
                    encode(null)
                else
                    encode(encoderResult.second, ignoreEncoderEntries + encoderResult.second!!.javaClass)
                if (data.isNotEmpty()) return prefix + data
            }
        }

        return if (obj is Map<*, *>) {
            buildString {
                append("{")
                append(obj.entries.joinToString(" ") { (k, v) -> encode(k) + " " + encode(v) })
                append("}")
            }
        } else if (obj is Set<*>) {
            buildString {
                append("#{")
                append(obj.joinToString(" ") { v -> encode(v) })
                append("}")
            }
        } else if (obj is List<*>) {
            buildString {
                val mid = obj.joinToString(" ") { v -> encode(v) }
                if (obj is RandomAccess) append("[").append(mid).append("]")
                else append("(").append(mid).append(")")
            }
        } else if (obj is Symbol) {
            obj.toString()
        } else if (obj is Keyword) {
            obj.toString()
        } else if (obj is Number) {
            buildString {
                append(obj.toString())
                when (obj) {
                    is Long -> append("L")
                    is BigInteger -> append("m")
                    is BigDecimal -> append("m")
                }
            }
        } else if (obj == null) {
            "nil"
        } else if (obj is String) {
            buildString(obj.length) {
                for (chr in obj) {
                    when (chr) {
                        '\t' -> append("\\t")
                        '\b' -> append("\\b")
                        '\n' -> append("\\n")
                        '\r' -> append("\\r")
                        '\'' -> append("\\'")
                        '\"' -> append("\\\"")
                        '\\' -> append("\\\\")
                        else -> append(chr)
                    }
                }
            }
        } else if (obj is Char) {
            if (obj.isLetterOrDigit())
                "\\u${obj}"
            else
                "\\u${obj.code.toString(16)}"
        } else if (obj is Boolean) {
            obj.toString()
        } else {
            throw EdnWriterException("Unsupported object type ${obj.javaClass} of object $obj.")
        }
    }


}
