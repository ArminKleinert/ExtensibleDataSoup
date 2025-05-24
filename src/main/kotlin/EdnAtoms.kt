class Keyword private constructor(private val s: String) : Comparable<Keyword> {
    companion object {
        private val definedKeywords = mutableMapOf<String, Int>()
        private val definedKeywordList = mutableListOf<Keyword>()

        fun keyword(s: String): Keyword {
            if (s != s.lowercase())
                System.err.println("Bad keyword :$s. Should be lowercase.")

            return if (s in definedKeywords) {
                val index = definedKeywords[s] as Int
                definedKeywordList[index]
            } else {
                val nextIndex = definedKeywords.size
                definedKeywords[s] = nextIndex
                val kw = Keyword(s)
                definedKeywordList.add(kw)
                kw
            }
        }

        fun keywordList(names: List<String>) = names.map { keyword(it) }
        fun keywordList(vararg names: String) = names.map { keyword(it) }

        fun keywordSet(names: Collection<String>) = names.map { keyword(it) }.toSet()
        fun keywordSet(vararg names: String) = names.map { keyword(it) }.toSet()

        operator fun get(s: String): Keyword =
            if (s.startsWith(":")) {
                System.err.println("Warning: Keyword initialization with ':' is discouraged: $s")
                keyword(s.substring(1))
            } else keyword(s)
    }

    fun name() = s

    override fun toString() = ":$s"
    override fun hashCode() = definedKeywords[s]!!

    override fun compareTo(other: Keyword): Int =
        when {
            this === other -> 0
            s.length < other.s.length -> -1
            s.length > other.s.length -> 1
            else -> s.compareTo(other.s)
        }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Keyword) return false
        return this === other
    }
}

class Symbol private constructor(private val s: String) {
    companion object {
        private val definedSymbols = mutableMapOf<String, Int>()
        private val definedSymbolList = mutableListOf<Symbol>()

        fun symbol(s: String): Symbol {
            return if (s in definedSymbols) {
                val index = definedSymbols[s] as Int
                definedSymbolList[index]
            } else {
                val nextIndex = definedSymbols.size
                definedSymbols[s] = nextIndex
                val kw = Symbol(s)
                definedSymbolList.add(kw)
                kw
            }
        }
    }

    fun name() = s

    override fun toString() = s
    override fun hashCode() = definedSymbols[s]!!
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Symbol) return false
        return this === other
    }
}
