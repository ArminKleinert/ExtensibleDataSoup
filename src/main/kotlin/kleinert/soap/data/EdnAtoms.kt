package kleinert.soap.data

class Keyword private constructor(private val s: Symbol) : Comparable<Keyword> {
    companion object {
        private val definedKeywords = mutableMapOf<String, Int>()
        private val definedKeywordList = mutableListOf<Keyword>()

        fun parse(s: String, allowUTF: Boolean = false): Keyword? {
            if (s.length <= 1) return null // Keywords must be at least 2 chars long
            if (s[0] != ':') return null // Keywords must start with a colon
            if (s.length == 2 && s[1] == '/') return null // Keywords can not be `:/`

            val substring = s.substring(1)

            if (!Symbol.isValidSymbol(substring, allowUTF)) return null

            val existingIndex = definedKeywords[s]
            if (existingIndex != null) return definedKeywordList[existingIndex]

            // Keywords follow the same conventions as symbols.
            val tempSymbol = Symbol.parse(substring, allowUTF) ?: return null
            return keyword(tempSymbol)
        }

        fun keyword(s: Symbol): Keyword {
            val k = Keyword(s)
            val index = definedKeywordList.size
            definedKeywords[k.toString()] = index
            definedKeywordList.add(k)
            return k
        }

        fun keyword(prefix: String?, name: String): Keyword {
            return Keyword(Symbol.symbol(prefix, name))
        }

        operator fun get(s: String): Keyword {
            val name = if (s.startsWith(':')) s else ":$s"
            return parse(name) ?: throw IllegalStateException("Illegal keyword format: $s")
        }
    }

    val length
        get() = s.length

    val fullyQualified: Boolean
        get() = s.fullyQualified

    val prefix
        get() = s.prefix

    val name
        get() = s.name

    override fun toString() = ":$s"
    override fun hashCode() = definedKeywords[s.toString()] ?: 0

    override fun compareTo(other: Keyword): Int =
        when {
            this === other -> 0
            else -> s.compareTo(other.s)
        }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Keyword) return false
        return this === other
    }

    //operator fun <T> invoke(m: Map<Keyword, T>): T? = m[this]

    operator fun <T> invoke(m: Map<*, T>): T? = m[this]
}

class Symbol private constructor(val prefix: String?, val name: String) : Comparable<Symbol> {
    companion object {
        private fun dividerIndexIfValid(s: String, allowUTF: Boolean = false): Int? {
            var dividerIndex = -1
            for ((index, chr) in s.codePoints().toArray().withIndex()) {
                when (chr) {
                    // First-char restriction: If the name starts with dot, plus, or minus, the second char can not be numeric
                    '.'.code, '+'.code, '-'.code ->
                        if (index == dividerIndex + 1 && s.length > dividerIndex + 2 && s[dividerIndex + 2] in '0'..'9')
                            return null

                    // First-char restriction: Can not start with numeric, dot, or hash symbol.
                    in '0'.code..'9'.code, ':'.code, '#'.code ->
                        if (index == dividerIndex + 1) return null

                    // Restriction: Only one slash per symbol.
                    '/'.code ->
                        if (dividerIndex == -1 && index > 0 && index < s.length - 1) dividerIndex = index
                        else return null

                    '*'.code, '!'.code, '_'.code, '?'.code, '$'.code, '%'.code, '&'.code, '='.code, '<'.code, '>'.code -> {}

                    in 'a'.code..'z'.code, in 'A'.code..'Z'.code -> {}

                    else -> {
                        if (chr < Char.MIN_VALUE.code || chr > Char.MAX_VALUE.code)
                            if (!allowUTF) return null
                            else if (chr.toChar().isWhitespace()) return null
                    }
                }
            }
            return dividerIndex
        }

        fun isValidSymbol(s: String, allowUTF: Boolean = false): Boolean =
            dividerIndexIfValid(s, allowUTF) != null

        fun parse(s: String, allowUTF: Boolean = false): Symbol? {
            if (s == "/")
                return symbol(null, "/")

            val dividerIndex = dividerIndexIfValid(s, allowUTF) ?: return null

            if (dividerIndex != -1) {
                val (prefix, postfix) = s.split('/')
                return symbol(prefix, postfix)
            }

            return symbol(null, s)
        }

        fun symbol(prefix: String?, name: String) = Symbol(prefix, name)
        fun symbol(name: String) = Symbol(null, name)
    }

    val length: Int
        get() = if (prefix?.isEmpty() != false) name.length else prefix.length + name.length

    val fullyQualified: Boolean
        get() = prefix?.isNotEmpty() ?: false

    override fun toString() = if (prefix.isNullOrEmpty()) name else "$prefix/$name"

    override fun compareTo(other: Symbol): Int {
        val prefixCompare = when {
            prefix == null -> if (other.prefix == null) 0 else -1
            other.prefix == null -> 1
            else -> prefix.compareTo(other.prefix)
        }
        if (prefixCompare != 0) return prefixCompare
        return name.compareTo(name)
    }

    fun toStringInner(): String {
        return "Symbol(prefix=$prefix, name='$name', length=$length, fullyQualified=$fullyQualified)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Symbol

        if (prefix != other.prefix) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prefix?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        return result
    }
}
