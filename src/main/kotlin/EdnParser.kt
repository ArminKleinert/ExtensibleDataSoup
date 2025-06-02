//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Pattern

object EdnReader {
    private val macros: Array<AFn?> = arrayOfNulls(128)
    private val dispatchMacros: Array<AFn?> = arrayOfNulls(128)
    private val symbolPat: Pattern = Pattern.compile("[:]?([\\D&&[^/]].*/)?(/|[\\D&&[^/]][^/]*)")
    private val intPat: Pattern =
        Pattern.compile("([-+]?)(?:(0)|([1-9][0-9]*)|0[xX]([0-9A-Fa-f]+)|0([0-7]+)|([1-9][0-9]?)[rR]([0-9A-Za-z]+)|0[0-9]+)(N)?")
    private val ratioPat: Pattern = Pattern.compile("([-+]?[0-9]+)/([0-9]+)")
    private val floatPat: Pattern = Pattern.compile("([-+]?[0-9]+(\\.[0-9]*)?([eE][-+]?[0-9]+)?)(M)?")
    private val EOF: Keyword = Keyword.keyword(null as String?, "eof")

    val TAG_KEY = Keyword.keyword(null as String?, "tag")

    private fun nonConstituent(ch: Int): Boolean = ch == '@'.code || ch == '`'.code || ch == '~'.code

    fun readString(s: String, opts: Map<*, *> = mapOf<Any?, Any?>()): Any? {
        val r = PushbackReader(StringReader(s))
        return read(r, opts)
    }

    private fun isWhitespace(ch: Int): Boolean = Character.isWhitespace(ch) || ch == 44

    private fun unread(r: PushbackReader, ch: Int) {
        if (ch != -1) {
            try {
                r.unread(ch)
            } catch (var3: IOException) {
                throw RuntimeException(var3)
            }
        }
    }

    private fun read1(r: Reader): Int {
        return try {
            r.read()
        } catch (var2: IOException) {
            throw RuntimeException(var2)
        }
    }

    private fun read(r: PushbackReader, opts: Map<*, *>): Any? =
        read(r, !opts.containsKey(EOF), opts[EOF], false, opts)

    private fun read(r: PushbackReader, eofIsError: Boolean, eofValue: Any?, isRecursive: Boolean, opts: Any?): Any? {
        try {
            while (true) {
                var ch: Int
                ch = read1(r)
                while (isWhitespace(ch)) {
                    ch = read1(r)
                }
                if (ch == -1) {
                    if (eofIsError) {
                        throw EdnParserException(msg="EOF while reading")
                    }
                    return eofValue
                }
                if (Character.isDigit(ch)) {
                    return readNumber(r, ch.toChar())
                }
                val macroFn: AFn? = getMacro(ch)
                if (macroFn != null) {
                    val ret = macroFn.invoke(r, ch.toChar(), opts)
                    if (ret === r) {
                        continue
                    }
                    return ret
                }
                if (ch == '+'.code || ch == '-'.code) {
                    val ch2 = read1(r)
                    if (Character.isDigit(ch2)) {
                        unread(r, ch2)
                        return readNumber(r, ch.toChar())
                    }
                    unread(r, ch2)
                }
                val token = readToken(r, ch.toChar(), true)
                return interpretToken(token)
            }
        } catch (var9: Exception) {
            if (!isRecursive && r is LineNumberingPushbackReader) {
                val rdr: LineNumberingPushbackReader = r
                throw ReaderException(rdr.lineNumber, rdr.columnNumber, var9)
            } else {
                throw RuntimeException(var9)
            }
        }
    }

    private fun readToken(r: PushbackReader, initch: Char, leadConstituent: Boolean): String {
        val sb = StringBuilder()
        if (leadConstituent && nonConstituent(initch.code)) {
            throw EdnParserException(msg="Invalid leading character: $initch")
        } else {
            sb.append(initch)
            while (true) {
                val ch = read1(r)
                if (ch == -1 || isWhitespace(ch) || isTerminatingMacro(ch)) {
                    unread(r, ch)
                    return sb.toString()
                }
                if (nonConstituent(ch)) {
                    throw EdnParserException(msg="Invalid constituent character: " + ch.toChar())
                }
                sb.append(ch.toChar())
            }
        }
    }

    private fun readNumber(r: PushbackReader, initch: Char): Any {
        val sb = StringBuilder()
        sb.append(initch)
        while (true) {
            val ch = read1(r)
            if (ch == -1 || isWhitespace(ch) || isMacro(ch)) {
                unread(r, ch)
                val s = sb.toString()
                val n = matchNumber(s)
                return n ?: throw NumberFormatException("Invalid number: $s")
            }
            sb.append(ch.toChar())
        }
    }

    private fun readUnicodeChar(token: String, offset: Int, length: Int, base: Int): Int {
        return if (token.length != offset + length) {
            throw IllegalArgumentException("Invalid unicode character: \\$token")
        } else {
            var uc = 0
            for (i in offset..<offset + length) {
                val d = token[i].digitToIntOrNull(base) ?: -1
                require(d != -1) { "Invalid digit: " + token[i] }
                uc = uc * base + d
            }
            uc.toChar().code
        }
    }

    private fun readUnicodeChar(r: PushbackReader, initch: Int, base: Int, length: Int, exact: Boolean): Int {
        var uc = initch.toChar().digitToIntOrNull(base) ?: -1
        return if (uc == -1) {
            throw IllegalArgumentException("Invalid digit: " + initch.toChar())
        } else {
            var i: Int = 1
            while (i < length) {
                val ch = read1(r)
                if (ch == -1 || isWhitespace(ch) || isMacro(ch)) {
                    unread(r, ch)
                    break
                }
                val d = ch.toChar().digitToIntOrNull(base) ?: -1
                require(d != -1) { "Invalid digit: " + ch.toChar() }
                uc = uc * base + d
                ++i
            }
            if (i != length && exact) {
                throw IllegalArgumentException("Invalid character length: $i, should be: $length")
            } else {
                uc
            }
        }
    }

    private fun interpretToken(s: String): Any? = when (s) {
        "nil" -> null
        "true" -> true
        "false" -> false
        else -> {
            var ret: Any? = null
            ret = matchSymbol(s)
            ret ?: throw EdnParserException(msg="Invalid token: $s")
        }
    }

    private fun matchSymbol(s: String): Any? {
        val m = symbolPat.matcher(s)
        return if (!m.matches()) {
            null
        } else {
            val gc = m.groupCount()
            val ns = m.group(1)
            val name = m.group(2)
            if ((ns == null || !ns.endsWith(":/")) && !name.endsWith(":") && s.indexOf("::", 1) == -1) {
                if (s.startsWith("::")) {
                    null
                } else {
                    val isKeyword = s[0] == ':'
                    val sym: Symbol = Symbol.symbol(s.substring(if (isKeyword) 1 else 0))!!
                    if (isKeyword) Keyword.keyword(sym) else sym
                }
            } else {
                null
            }
        }
    }

    private fun matchNumber(s: String): Any? {
        var m = intPat.matcher(s)
        return if (m.matches()) {
            if (m.group(2) != null) {
                if (m.group(8) != null) BigInteger.ZERO else Numbers.num(0L)
            } else {
                val negate = m.group(1) == "-"
                var radix = 10
                var n: String?
                if (m.group(3).also { n = it } != null) {
                    radix = 10
                } else if (m.group(4).also { n = it } != null) {
                    radix = 16
                } else if (m.group(5).also { n = it } != null) {
                    radix = 8
                } else if (m.group(7).also { n = it } != null) {
                    radix = m.group(6).toInt()
                }
                if (n == null) {
                    null
                } else {
                    var bn = BigInteger(n, radix)
                    if (negate) {
                        bn = bn.negate()
                    }
                    if (m.group(8) != null) {
                        bn
                    } else {
                        if (bn.bitLength() < 64) Numbers.num(bn.toLong()) else bn
                    }
                }
            }
        } else {
            m = floatPat.matcher(s)
            if (m.matches()) {
                if (m.group(4) != null) BigDecimal(m.group(1)) else s.toDouble()
            } else {
                m = ratioPat.matcher(s)
                if (m.matches()) {
                    var numerator = m.group(1)
                    if (numerator.startsWith("+")) numerator = numerator.substring(1)
                    Ratio.of(BigInteger(numerator), BigInteger(m.group(2)))
                } else {
                    null
                }
            }
        }
    }

    private fun getMacro(ch: Int): AFn? {
        return if (ch < macros.size) macros[ch] else null
    }

    private fun isMacro(ch: Int): Boolean {
        return ch < macros.size && macros[ch] != null
    }

    private fun isTerminatingMacro(ch: Int): Boolean {
        return ch != 35 && ch != 39 && isMacro(ch)
    }

    private fun readDelimitedList(delim: Char, r: PushbackReader, isRecursive: Boolean, opts: Any?): List<*> {
        val firstline = if (r is LineNumberingPushbackReader) r.lineNumber else -1
        val a = ArrayList<Any?>()
        while (true) {
            var ch: Int
            ch = read1(r)
            while (isWhitespace(ch)) {
                ch = read1(r)
            }
            if (ch == -1) {
                if (firstline < 0) {
                    throw EdnParserException(msg="EOF while reading")
                }
                throw EdnParserException(msg="EOF while reading, starting at line $firstline")
            }
            if (ch == delim.code) {
                return a
            }
            val macroFn: AFn? = getMacro(ch)
            var mret: Any?
            if (macroFn != null) {
                mret = macroFn.invoke(r, ch.toChar(), opts)
                if (mret !== r) {
                    a.add(mret)
                }
            } else {
                unread(r, ch)
                mret = read(r, true, null as Any?, isRecursive, opts)
                if (mret !== r) {
                    a.add(mret)
                }
            }
        }
    }

    object TaggedReader : AFn {
        override operator fun invoke(reader: Any?, firstChar: Any?, opts: Any?): Any? {
            val r = reader as PushbackReader
            val name = read(r, true, null as Any?, false, opts)
            return if (name !is Symbol) {
                throw RuntimeException("Reader tag must be a symbol")
            } else {
                val sym: Symbol = name
                readTagged(r, sym, opts as Map<*, *>)
            }
        }

        private fun readTagged(reader: PushbackReader, tag: Symbol?, opts: Map<*, *>): Any? {
            val o = read(reader, true, null as Any?, true, opts)
            val readersKeyword: Keyword = Keyword.keyword(null as String?, "readers")
            val readers = opts[readersKeyword] as Map<*, *>
            val dataReader: AFn? = readers[tag] as AFn?
            return if (dataReader == null) {
                val defaultKeyword: Keyword = Keyword.keyword(null as String?, "default")
                val defaultReader: AFn? = opts[defaultKeyword] as AFn?
                if (defaultReader != null) {
                    defaultReader.invoke(tag, o, null)
                } else {
                    throw RuntimeException("No reader function for tag " + tag.toString())
                }
            } else {
                dataReader.invoke(o, null, null)
            }
        }
    }

    private object SymbolicValueReader : AFn {
        val specials: Map<*, *> = mapOf(
            Symbol.symbol("Inf") to Double.POSITIVE_INFINITY,
            Symbol.symbol("-Inf") to Double.NEGATIVE_INFINITY,
            Symbol.symbol("NaN") to Double.NaN
        )

        override operator fun invoke(reader: Any?, quote: Any?, opts: Any?): Any? {
            val r = reader as PushbackReader
            val o = read(r, true, null as Any?, true, opts)
            return if (o !is Symbol) {
                throw EdnParserException(msg="Invalid token: ##$o")
            } else if (!specials.containsKey(o)) {
                throw EdnParserException(msg="Unknown symbolic value: ##$o")
            } else {
                specials[o]
            }
        }
    }

    private val UnreadableReader : AFn = { _: Any?, _: Any?, _: Any?->
        throw EdnParserException(msg="Unreadable form")
    }

    private val UnmatchedDelimiterReader:AFn = { _: Any?, rightHandDelimiter: Any?, _: Any?->
        throw EdnParserException(msg="Unmatched delimiter: $rightHandDelimiter")
    }

    private val SetReader = { reader: Any?, _: Any?, opts: Any?->
        val r = reader as PushbackReader
        readDelimitedList('}', r, true, opts).toSet()
    }

    private val MapReader = { reader: Any?, _: Any?, opts: Any? ->
        val r = reader as PushbackReader
        val a = readDelimitedList('}', r, true, opts).toTypedArray()
        if (a.size and 1 == 1) {
            throw EdnParserException(msg="Map literal must contain an even number of forms")
        } else {
            RT.map(a)
        }
    }

    private val VectorReader = { reader: Any?, _: Any?, opts: Any?->
        val r = reader as PushbackReader
        readDelimitedList(']', r, true, opts)
    }

    private val ListReader = { reader: Any?, _: Any?, opts: Any?->
        val r = reader as PushbackReader
        var line: Int = 1
        var column: Int = 1
        if (r is LineNumberingPushbackReader) {
            line = r.lineNumber
            column = r.columnNumber - 1
        }
        val list = readDelimitedList(')', r, true, opts)
        if (list.isEmpty()) {
            listOf<Any?>()
        } else {
            list
            //list as IObj<*>
        }
    }

    private object CharacterReader : AFn {
        override operator fun invoke(reader: Any?, backslash: Any?, opts: Any?): Any? {
            val r = reader as PushbackReader
            val ch = read1(r)
            if (ch == -1) throw EdnParserException(msg="EOF while reading character")

            val token = readToken(r, ch.toChar(), false)
            if (token.length == 1) return token[0]
            when (token) {
                "newline" -> return '\n'
                "space" -> return ' '
                "tab" -> return '\t'
                "backspace" -> return '\b'
                "formfeed" -> return 12.toChar()
                "return" -> return '\r'
            }

            if (token.startsWith("u")) {
                val c = readUnicodeChar(token, 1, 4, 16).toChar()
                if (c in '\ud800'..'\udfff') {
                    throw EdnParserException(msg="Invalid character constant: \\u$c")
                } else {
                    return c
                }
            }

            if (token.startsWith("o")) {
                val len = token.length - 1
                if (len > 3) {
                    throw EdnParserException(msg="Invalid octal escape sequence length: $len")
                } else {
                    val uc = readUnicodeChar(token, 1, len, 8)
                    if (uc > 255) {
                        throw EdnParserException(msg="Octal escape sequence must be in range [0, 377].")
                    } else {
                        return uc.toChar()
                    }
                }
            }

            throw EdnParserException(msg="Unsupported character: \\$token")
        }
    }

    private object MetaReader : AFn {
        override operator fun invoke(reader: Any?, caret: Any?, opts: Any?): Any? {
            throw UnsupportedOperationException("Reading Metadata is not supported.")
//            val r = reader as PushbackReader
//            var line = -1
//            var column = -1
//            if (r is LineNumberingPushbackReader) {
//                line = r.lineNumber
//                column = r.columnNumber - 1
//            }
//            var meta = read(r, true, null as Any?, true, opts)
//            require(meta is Map<*, *> || meta is Keyword || meta is Symbol || meta is String) { "Metadata must be Symbol,Keyword,String or Map" }
//            if (meta !is Symbol && meta !is String) {
//                if (meta is Keyword) {
//                    meta = mapOf(meta to true)
//                }
//            } else {
//                meta = mapOf<Any?, Any?>(TAG_KEY to meta)
//            }
//            val o = read(r, true, null as Any?, true, opts)
//            return if (o !is IMeta<*>) {
//                throw IllegalArgumentException("Metadata can only be applied to IMetas")
//            } else {
//                if (line != -1 && o is List<*>?) {
//                    meta = (meta as Map<Any?, Any?>) + mapOf<Any?, Any?>(RT.LINE_KEY to line, RT.COLUMN_KEY to column)
//                }
//                if (o is IReference<*>) {
//                    o.resetMeta(meta as Map<Any?, Any?>)
//                    o
//                } else {
//                    var ometa = meta(o)
//                    RT.seq(meta)!!.forEach {
//                        val kv: Map.Entry<*, *> = it as Map.Entry<*, *>
//                        ometa = ometa!! + (kv.key to kv.value)
//                    }
//                    (o as IObj<*>).withMeta(ometa as Map<Any?, Any?>)
//                }
//            }
        }
    }

    private val DispatchReader = { reader: Any?, _: Any?, opts: Any?->
            val ch = read1(reader as Reader)
            if (ch == -1) throw EdnParserException(msg="EOF while reading character")

                val fn: AFn? = dispatchMacros[ch]
                if (fn == null) {
                    if (Character.isLetter(ch)) {
                        unread(reader as PushbackReader, ch)
                        TaggedReader.invoke(reader, ch, opts)
                    } else {
                        throw EdnReaderException(String.format("No dispatch macro for: %c", ch.toChar()))
                    }
                } else {
                    fn.invoke(reader, ch, opts)
                }
    }

    private val NamespaceMapReader: (Any?,Any?,Any?)->Any? = { reader: Any?, _: Any?, opts: Any? ->
            val r = reader as PushbackReader
            val sym = read(r, true, null as Any?, false, opts)
            if (sym is Symbol && sym.prefix == null) {
                val ns: String = sym.name
                var nextChar: Int
                nextChar = read1(r)
                while (isWhitespace(nextChar)) {
                    nextChar = read1(r)
                }
                if (123 != nextChar) {
                    throw RuntimeException("Namespaced map must specify a map")
                } else {
                    val kvs = readDelimitedList('}', r, true, opts)
                    if (kvs.size and 1 == 1) {
                        throw EdnParserException(msg="Namespaced map literal must contain an even number of forms")
                    } else {
                        val a = arrayOfNulls<Any>(kvs.size)
                        val iter = kvs.iterator()
                        var i = 0
                        while (iter.hasNext()) {
                            var key = iter.next()!!
                            val iterNext = iter.next()!!
                            if (key is Keyword) {
                                val kw: Keyword = key as Keyword
                                if (kw.prefix == null) {
                                    key = Keyword.keyword(ns, kw.name)
                                } else if (kw.prefix.equals("_")) {
                                    key = Keyword.keyword(null as String?, kw.name)
                                }
                            } else if (key is Symbol) {
                                val s: Symbol = key as Symbol
                                if (s.prefix == null) {
                                    key = Symbol.symbol(ns, s.name)
                                } else if (s.prefix == "_") {
                                    key = Symbol.symbol(null as String?, s.name)
                                }
                            }
                            a[i] = key
                            a[i + 1] = iterNext
                            i += 2
                        }
                        RT.map(a)
                    }
                }
            } else {
                throw RuntimeException("Namespaced map must specify a valid namespace: $sym")
            }
    }

    private val DiscardReader = { reader: Any?, _: Any?, opts: Any?->
            val r = reader as PushbackReader
            read(r, true, null as Any?, true, opts)
            r
        }

    private val CommentReader = { reader: Any?, _: Any?, _: Any?->
            val r = reader as Reader
            var ch: Int
            do {
                ch = read1(r)
            } while (ch != -1 && ch != 10 && ch != 13)
             r
    }

    private val StringReader = { reader: Any?, _: Any?, _: Any?->
        val sb = StringBuilder()
        val r = reader as Reader
        var ch = read1(r)
        while (ch != 34) {
            if (ch == -1) {
                throw EdnParserException(msg="EOF while reading string")
            }
            if (ch == '\\'.code) {
                ch = read1(r)
                if (ch == -1) {
                    throw EdnParserException(msg="EOF while reading string")
                }
                when (ch) {
                    '"'.code, '\\'.code -> {}
                    'b'.code -> ch = '\b'.code
                    'f'.code -> ch = '\u000c'.code // formfeed
                    'n'.code -> ch = '\n'.code
                    'r'.code -> ch = '\r'.code
                    't'.code -> ch = '\t'.code
                    'u'.code -> {
                        ch = read1(r)
                        if ((ch.toChar().digitToIntOrNull(16) ?: -1) == -1) {
                            throw EdnParserException(msg="Invalid unicode escape: \\u" + ch.toString(16))
                        }
                        ch = readUnicodeChar(r as PushbackReader, ch, 16, 4, true)
                    }

                    else -> {
                        if (!Character.isDigit(ch)) {
                            throw EdnParserException(msg="Unsupported escape character: \\" + ch.toChar())
                        }
                        ch = readUnicodeChar(r as PushbackReader, ch, 8, 3, false)
                        if (ch > 255) {
                            throw EdnParserException(msg="Octal escape sequence must be in range [0, 377].")
                        }
                    }
                }
            }
            sb.append(ch.toChar())
            ch = read1(r)
        }
        sb.toString()
    }

    init {
        macros['"'.code] = StringReader
        macros[';'.code] = CommentReader
        macros['^'.code] = MetaReader
        macros['('.code] = ListReader
        macros[')'.code] = UnmatchedDelimiterReader
        macros['['.code] = VectorReader
        macros[']'.code] = UnmatchedDelimiterReader
        macros['{'.code] = MapReader
        macros['}'.code] = UnmatchedDelimiterReader
        macros['\\'.code] = CharacterReader
        macros['#'.code] = DispatchReader
        dispatchMacros['#'.code] = SymbolicValueReader
        dispatchMacros['^'.code] = MetaReader
        dispatchMacros['{'.code] = SetReader
        dispatchMacros['<'.code] = UnreadableReader
        dispatchMacros['_'.code] = DiscardReader
        dispatchMacros[':'.code] = NamespaceMapReader
    }

    class ReaderException(val line: Int=-1, val column: Int=-1, cause: Throwable?=null) : RuntimeException(cause)
    
    class EdnParserException(msg:String?=null, cause: Throwable?=null) : RuntimeException(msg, cause) {
    }

    private class LineNumberingPushbackReader(r: Reader) : PushbackReader(LineNumberReader(r)) {
        private var _atLineStart = true
        private var _prev = false
        var columnNumber = 1
            private set
        private var sb: java.lang.StringBuilder? = null

        var lineNumber: Int
            get() = (`in` as LineNumberReader).lineNumber + 1
            set(line) {
                (`in` as LineNumberReader).lineNumber = line - 1
            }

        fun captureString() {
            sb = java.lang.StringBuilder()
        }

        val string: String?
            get() = if (sb != null) {
                val ret = sb.toString()
                sb = null
                ret
            } else {
                null
            }

        @Throws(IOException::class)
        override fun read(): Int {
            val c = super.read()
            _prev = _atLineStart
            if (c != 10 && c != -1) {
                _atLineStart = false
                ++columnNumber
            } else {
                _atLineStart = true
                columnNumber = 1
            }
            if (sb != null && c != -1) {
                sb!!.append(c.toChar())
            }
            return c
        }

        @Throws(IOException::class)
        override fun unread(c: Int) {
            super.unread(c)
            _atLineStart = _prev
            --columnNumber
            if (sb != null) {
                sb!!.deleteCharAt(sb!!.length - 1)
            }
        }

        @Throws(IOException::class)
        fun readLine(): String? {
            val c = this.read()
            val line: String?
            when (c) {
                -1 -> line = null
                10 -> line = ""
                else -> {
                    val first = c.toChar().toString()
                    val rest = (`in` as LineNumberReader).readLine()
                    if (sb != null) {
                        sb!!.append(rest + "\n")
                    }
                    line = if (rest == null) first else first + rest
                    _prev = false
                    _atLineStart = true
                    columnNumber = 1
                }
            }
            return line
        }

        fun atLineStart(): Boolean {
            return _atLineStart
        }
    }

    internal object RT {
        val LINE_KEY: Keyword = Keyword.keyword(null as String?, "line")
        val COLUMN_KEY: Keyword = Keyword.keyword(null as String?, "column")

        fun map(init: Array<Any?>): Map<Any?, Any?> {
            val res: MutableMap<Any?, Any?> = HashMap()
            var i = 0
            while (i < init.size) {
                res[init[i]] = init[i + 1]
                i += 2
            }
            return res
        }

        fun seq(o: Any?) = if (o is Collection<*>) o.toList() else null

        fun booleanCast(x: Any?): Boolean = if (x is Boolean) x else x != null
    }
}

//    interface AFn {
//        operator fun invoke(reader: Any?, doublequote: Any?, opts: Any?): Any?
//    }
    typealias AFn = (Any?,Any?,Any?)->Any?