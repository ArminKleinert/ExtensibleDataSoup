package kleinert.soap.data

class Keyword private constructor(private val s: Symbol) : Comparable<Keyword> {
    companion object {
        private val definedKeywords = mutableMapOf<String, Int>()
        private val definedKeywordList = mutableListOf<Keyword>()

        fun keyword(s: String): Keyword? {
            val existingIndex = definedKeywords[s]
            if (existingIndex != null) return definedKeywordList[existingIndex]

            if (s.length <= 1) return null // Keywords must be at least 2 chars long
            if (s[0] != ':') return null // Keywords must start with a colon
            if (s.length == 2 && s[1] == '/') return null // Keywords can not be `:/`

            // Keywords follow the same conventions as symbols.
            val tempSymbol = Symbol.symbol(s.substring(1)) ?: return null
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
            return keyword(name) ?: throw IllegalStateException("Illegal keyword format: $s")
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
        fun symbol(s: String): Symbol? {
            var dividerIndex = -1

            s.forEachIndexed { index, chr ->
                when (chr) {
                    // First-char restriction: If the name starts with dot, plus, or minus, the second char can not be numeric
                    '.', '+', '-' ->
                        if (index == dividerIndex + 1 && s.length > dividerIndex + 2 && s[dividerIndex + 2] in '0'..'9')
                            return null

                    // First-char restriction: Can not start with numeric, dot, or hash symbol.
                    in '0'..'9', ':', '#' ->
                        if (index == dividerIndex + 1) return null

                    // Restriction: Only one slash per symbol.
                    '/' ->
                        if (dividerIndex == -1 && index > 0 && index < s.length - 1) dividerIndex = index
                        else return null

                    '*', '!', '_', '?', '$', '%', '&', '=', '<', '>', in 'a'..'z', in 'A'..'Z' -> {}

                    else -> return null
                }
            }

            if (dividerIndex != -1) {
                val (prefix, postfix) = s.split('/')
                return Symbol(prefix, postfix)
            }

            return Symbol(null, s)
        }

        fun symbol(prefix: String?, name: String) = Symbol(prefix, name)
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
}
