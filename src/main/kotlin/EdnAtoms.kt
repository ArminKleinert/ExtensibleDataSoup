class Keyword private constructor(
    private val s:Symbol) : Comparable<Keyword> {
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

        fun keyword(     prefix: String,                         name: String): Keyword {
            return Keyword(Symbol.symbol(prefix, name ))
        }

        operator fun get(s: String): Keyword = keyword(s) ?: throw IllegalStateException("Illegal keyword format: $s")
    }

    val length = s.length

    val fullyQualified: Boolean = s.fullyQualified

    override fun toString() = ":$s"
    override fun hashCode() = definedKeywords[s.toString()]!!

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
}

class Symbol private constructor(
    val prefix: String,
    val name: String
): Comparable<Symbol> {
    companion object {
        fun symbol(s: String): Symbol? {
            //. * + ! - _ ? $ % & = < >
            var dividerIndex = -1

            s.forEachIndexed { index, chr ->
                when (chr) {
                    '*', '!', '_', '?', '$', '%', '&', '=', '<', '>', in 'a'..'z', in 'A'..'Z' -> {}

                    // First-char restriction: If the name starts with dot, plus, or minus, the second char can not be numeric
                    '.', '+', '-' -> if (index == dividerIndex+1 && s.length > dividerIndex+2 && s[dividerIndex+2] in '0'..'9') return null

                    // First-char restriction: Can not start with numeric, dot, or hash symbol.
                    in '0'..'9', ':', '#' -> if (index == dividerIndex+1) return null

                    // Restriction: Only one slash per symbol.
                    '/' -> if (dividerIndex == -1 && index > 0 && index < s.length - 2)
                        dividerIndex = index else return null

                    else -> return null
                }
            }

            if (dividerIndex != -1) {
                val prefix = s.substring(0..dividerIndex)
                val postfix = s.substring(dividerIndex..<s.length)
                return Symbol(prefix, postfix)
            }

            return Symbol("", s)
        }

        fun symbol(     prefix: String,                    name: String) = Symbol(prefix, name)
    }

    val length = if (prefix.isEmpty()) name.length else prefix.length+name.length+1

    val fullyQualified: Boolean = prefix.isNotEmpty()

    override fun toString() = if (prefix.isEmpty()) name else "$prefix/$name"

    override fun compareTo(other: Symbol): Int {
        val prefixCompare = prefix.compareTo(other.prefix)
        if (prefixCompare != 0) return prefixCompare
        return name.compareTo(name)
    }
}
