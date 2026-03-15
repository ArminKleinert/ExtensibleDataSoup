package kleinert.edn.data

import kleinert.edn.data.Keyword.Companion.parse
import kleinert.edn.data.Symbol.Companion.isValidSymbol
import kleinert.edn.data.Symbol.Companion.parse
import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

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
class Keyword private constructor(val sym: Symbol) : Comparable<Keyword> {
    val name: String
        get() = sym.name

    val fullyQualified: Boolean
        get() = sym.fullyQualified

    val namespace
        get() = sym.namespace

    operator fun component1(): String? = namespace
    operator fun component2(): String = name

    override fun toString(): String = ":$sym"

    override fun compareTo(other: Keyword): Int = sym.compareTo(other.sym)

    override fun hashCode(): Int = this.sym.hashCode() + -1640531527

    override fun equals(other: Any?): Boolean =
        this === other || (other is Keyword && sym == other.sym)

    companion object {
        private val table = ConcurrentHashMap<Symbol, Reference<Keyword?>>()
        private val rq = ReferenceQueue<Keyword?>()

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

            if (!isValidSymbol(substring, allowUTF)) return null

            return intern(Symbol.parse(substring, allowUTF) ?: return null)
        }

        private fun clearCache() {
            if (rq.poll() == null)
                return

            var rqPoll = rq.poll()
            while (rqPoll != null) {
                rqPoll = rq.poll()
            }

            for (e in table.entries) {
                val ref: Reference<Keyword?> = e.value
                if (ref.get() == null) {
                    table.remove(e.key, ref)
                }
            }
        }

        fun intern(sym: Symbol): Keyword {
            var k: Keyword? = null
            var existingRef = table[sym]
            if (existingRef == null) {
                clearCache()

                k = Keyword(sym)
                existingRef = table.putIfAbsent(sym, WeakReference<Keyword?>(k, rq))
            }

            if (existingRef == null) {
                return k!!
            }

            val existingKeyword = existingRef.get()
            if (existingKeyword != null) {
                return existingKeyword
            }
            table.remove(sym, existingRef)
            return intern(sym)
        }

        fun find(sym: Symbol): Keyword? {
            val ref = table[sym]
            return ref?.get()
        }

        fun find(nsAndName: String): Keyword? {
            return find(Symbol[nsAndName])
        }

        /**
         * Creates a new [Keyword]. Unlike [parse], this function performs no additional format checking.
         * @return A new [Keyword] from the input [Symbol].
         */
        fun keyword(s: Symbol): Keyword {
            return intern(s)
        }

        /**
         * Creates a new [Keyword]. Unlike [parse], this function performs no additional format checking.
         * @return A new [Keyword] from the input [Symbol].
         */
        fun keyword(s: String): Keyword {
            return keyword(null, s)
        }

        /**
         * Creates a new [Keyword]. Unlike [parse], this function performs no additional format checking.
         * @return A new [Keyword].
         */
        fun keyword(prefix: String?, name: String): Keyword {
            return intern(Symbol.symbol(prefix, name))
        }

        /**
         * Equivalent to [parse], but Nicer to read.
         * Keyword["abc"] == Keyword.parse("abc")
         * @return A new [Keyword] from the input [Symbol].
         */
        operator fun get(s: String): Keyword {
            val name = if (s.startsWith(':')) s else ":$s"
            return parse(name) ?: throw IllegalArgumentException("Illegal keyword format: $s")
        }

        operator fun get(s: Symbol): Keyword {
            return intern(s)
        }
    }
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
            if (s.length == 1 && s[0] == '/')
                return -1

            var dividerIndex: Int? = -1

            val codepoints = s.codePoints().toArray()
            when (codepoints[0]) {
                '.'.code, '+'.code, '-'.code ->
                    if (codepoints.size > 1 && (codepoints[1] in '0'.code..'9'.code)) return null

                '/'.code -> return if (codepoints.size == 1) -1 else null
            }

            val iter = codepoints.iterator().withIndex()
            for ((index, chr) in iter) {
                when (chr) {
                    '*'.code, '!'.code, '_'.code, '?'.code, '$'.code, '%'.code, '&'.code, '='.code, '<'.code, '>'.code -> {}
                    in 'a'.code..'z'.code, in 'A'.code..'Z'.code, in '0'.code..'9'.code -> {}
                    '.'.code, '+'.code, '-'.code -> {
                        if ((index == 0 || index - 1 == dividerIndex) && index < codepoints.size - 1 && codepoints[index + 1] in '0'.code..'9'.code)
                            return null
                    }

                    '/'.code -> {
                        if (!iter.hasNext()) return null
                        if (dividerIndex == -1) dividerIndex = index
                    }

                    else -> {
                        if (chr.toChar().isWhitespace()) return null
                        else if (!allowUTF) return null
//                        else if (chr >= Char.MIN_VALUE.code && chr <= Char.MAX_VALUE.code) return null
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
        get() = namespace != null

    override fun toString() = if (namespace.isNullOrEmpty()) name else "$namespace/$name"

    override fun compareTo(other: Symbol): Int {
        if (this === other)
            return 0
        val prefixCompare = when {
            namespace == null -> if (other.namespace == null) 0 else -1
            other.namespace == null -> 1
            else -> namespace.compareTo(other.namespace)
        }
        if (prefixCompare != 0) return prefixCompare
        return name.compareTo(other.name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Symbol) return false

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
