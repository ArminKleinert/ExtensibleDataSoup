package kleinert.edn.data

/**
 * A Keyword as specified in the EDN format specification.
 * When a keyword is created, it is interned.
 *
 * @property namespace The Keyword as a [String] without the colon and without [name].
 * @property name The Keyword as a [String] without the colon and the namespace.
 * @property fullyQualified True if [namespace] is not null.
 *
 * @author Armin Kleinert
 */
class Keyword private constructor(private val s: Symbol) : Comparable<Keyword> {
    companion object {
        private val definedKeywords = mutableMapOf<String, Int>()
        private val definedKeywordList = mutableListOf<Keyword>()

        /**
         * Creates a new [Keyword] from the input [s]. The input must start with a colon.2
         *
         * @param s The format.
         * @param allowUTF If true, the [Keyword] can include chars with more than 16 bits. This is not allowed by the EDN format specification.
         *
         * @return null if the parse was invalid or a [Keyword] if it was valid.
         */
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

        /**
         * Creates a new [Keyword]. Unlike [parse], this function performs no additional format checking.
         * @return A new [Keyword] from the input [Symbol].
         */
        private fun keyword(s: Symbol): Keyword {
            val k = Keyword(s)
            val index = definedKeywordList.size
            definedKeywords[k.toString()] = index
            definedKeywordList.add(k)
            return k
        }

        /**
         * Creates a new [Keyword]. Unlike [parse], this function performs no additional format checking.
         * @return A new [Keyword].
         */
        fun keyword(prefix: String?, name: String): Keyword {
            return Keyword(Symbol.symbol(prefix, name))
        }

        /**
         * Equivalent to [parse], but Nicer to read.
         * Keyword["abc"] == Keyword.parse("abc")
         * @return A new [Keyword] from the input [Symbol].
         */
        operator fun get(s: String): Keyword {
            val name = if (s.startsWith(':')) s else ":$s"
            return parse(name) ?: throw IllegalStateException("Illegal keyword format: $s")
        }
    }

    val fullyQualified: Boolean
        get() = s.fullyQualified

    val namespace
        get() = s.namespace

    val name
        get() = s.name

    override fun toString() = ":$s"
    override fun hashCode() = (definedKeywords[s.toString()] ?: 0) xor 0xCAFEBEEF.toInt()

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

    operator fun <T> invoke(m: Map<*, T>): T? = m[this]
}

/**
 * A Symbol as specified in the EDN format specification.
 *
 * @property namespace The [Symbol] as a [String] without the colon and without [name].
 * @property name The [Symbol] as a [String] without the colon and the namespace.
 * @property fullyQualified True if [namespace] is not null.
 *
 * @author Armin Kleinert
 */
class Symbol private constructor(val namespace: String?, val name: String) : Comparable<Symbol> {
    companion object {
        /**
         * @see isValidSymbol
         *
         * @param s The input string.
         * @param allowUTF Allow char codes over [Char.MAX_VALUE].
         *
         * @return -1 if the string does not include '/', null if the string is invalid or the index of the '/' in the string.
         */
        private fun dividerIndexIfValid(s: String, allowUTF: Boolean = false): Int? {
            if (s.isEmpty())
                return null

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
                        if (chr.toChar().isWhitespace()) return null
                        if (!allowUTF && (chr < Char.MIN_VALUE.code || chr > Char.MAX_VALUE.code)) return null
                    }
                }
            }
            return dividerIndex
        }

        /**
         * Checks whether [s] conforms to the EDN standard, returns true, false otherwise.
         * The naming rules are described in the documentation of [parse].
         *
         * @param s The input string.
         * @param allowUTF Allow char codes over [Char.MAX_VALUE].
         *
         * @return -1 if the string does not include '/', null if the string is invalid or the index of the '/' in the string.
         */
        fun isValidSymbol(s: String, allowUTF: Boolean = false): Boolean =
            dividerIndexIfValid(s, allowUTF) != null

        /**
         * Parses a [String] into a [Symbol]. If the input is invalid, returns null.
         *
         * The string can no more than one '/'. If there is one, the part in front of it is the [namespace] and part
         * after the '/' the [name]. If there  is no '/', the [Symbol] has no [namespace].
         *
         * The standard only allows digits, letters and most chars in the normal char range of 0 to 2^16-1 are allowed.
         * Other char codes can be allowed with the [allowUTF] option.
         *
         * In regex form, the symbol would be as follows:
         * [a-zA-Z.+\\-\*!_?$%&=<>][a-zA-Z0-9.+\\-\*!_?$%&=<>]*(/[a-zA-Z.+\\-\*!_?$%&=<>][a-zA-Z0-9.+\\-\*!_?$%&=<>]*)?
         *
         * @param s The input string.
         * @param allowUTF Allow char codes over [Char.MAX_VALUE].
         *
         * @return -1 if the string does not include '/', null if the string is invalid or the index of the '/' in the string.
         */
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

        /**
         * Creates a new [Symbol]. Unlike [parse], no format checking is performed.
         *
         * @param namespace The new [Symbol]'s [namespace].
         * @param name The new [Symbol]'s [name].
         *
         * @return A new [Symbol].
         */
        fun symbol(namespace: String?, name: String) = Symbol(namespace, name)


        /**
         * Creates a new [Symbol]. Unlike [parse], no format checking is performed.
         *
         * @param name The new [Symbol]'s [name].
         *
         * @return A new [Symbol].
         */
        fun symbol(name: String) = Symbol(null, name)

        /**
         * Parses [s] into a [Symbol]. Throws an exception if parsing fails.
         *
         * @throws IllegalArgumentException If parsing failed.
         *
         * @see parse
         *
         * @param s The input [String].
         *
         * @return A new [Symbol].
         */
        operator fun get(s: String): Symbol = parse(s) ?: throw IllegalArgumentException("Illegal symbol format: $s")
    }

    val fullyQualified: Boolean
        get() = namespace?.isNotEmpty() ?: false

    override fun toString() = if (namespace.isNullOrEmpty()) name else "$namespace/$name"

    override fun compareTo(other: Symbol): Int {
        val prefixCompare = when {
            namespace == null -> if (other.namespace == null) 0 else -1
            other.namespace == null -> 1
            else -> namespace.compareTo(other.namespace)
        }
        if (prefixCompare != 0) return prefixCompare
        return name.compareTo(name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Symbol

        if (namespace != other.namespace) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        return result
    }
}
