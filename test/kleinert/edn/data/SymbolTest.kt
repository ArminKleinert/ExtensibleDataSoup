package kleinert.edn.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SymbolTest {
    @Test
    fun getFullyQualified() {
        Assertions.assertFalse(Symbol.symbol(null, "abc").fullyQualified)
        Assertions.assertFalse(Symbol.symbol("abc").fullyQualified)
        Assertions.assertTrue(Symbol.symbol("ns", "abc").fullyQualified)
        Assertions.assertFalse(Symbol.parse("abc")!!.fullyQualified)
        Assertions.assertTrue(Symbol.parse("ns/abc")!!.fullyQualified)

        // Corner case: Empty string as ns is valid.
        Assertions.assertTrue(Symbol.symbol("", "abc").fullyQualified)

        // Corner case: A single slash is a valid symbol but does not have a namespace.
        Assertions.assertFalse(Symbol.parse("/")!!.fullyQualified)
    }


    @Test
    fun getNamespace() {
        Symbol.symbol(null, "abc").let {
            Assertions.assertEquals(null, it.namespace)
        }
        Symbol.symbol("abc").let {
            Assertions.assertEquals(null, it.namespace)
        }
        Symbol.symbol("ns", "abc").let {
            Assertions.assertEquals("ns", it.namespace)
        }
        Symbol.symbol("", "abc").let {
            Assertions.assertEquals("", it.namespace)
        }
        Symbol.symbol("", "").let {
            Assertions.assertEquals("", it.namespace)
        }
    }

    @Test
    fun getName() {
        Symbol.symbol(null, "abc").let {
            Assertions.assertEquals("abc", it.name)
        }
        Symbol.symbol("abc").let {
            Assertions.assertEquals("abc", it.name)
        }
        Symbol.symbol("ns", "abc").let {
            Assertions.assertEquals("abc", it.name)
        }
        Symbol.symbol("").let {
            Assertions.assertEquals("", it.name)
        }
        Symbol.symbol("", "").let {
            Assertions.assertEquals("", it.name)
        }
    }

    @Test
    fun isValidSymbolSimple() {
        Assertions.assertTrue(Symbol.isValidSymbol("abc"))
        Assertions.assertTrue(Symbol.isValidSymbol("ns/abc"))
        Assertions.assertTrue(Symbol.isValidSymbol("/"))
        Assertions.assertTrue(Symbol.isValidSymbol("+"))
        Assertions.assertTrue(Symbol.isValidSymbol("-"))
        Assertions.assertTrue(Symbol.isValidSymbol("."))
    }

    @Test
    fun isValidSymbolInvalidFormat() {
        Assertions.assertFalse(Symbol.isValidSymbol("")) //not valid
        Assertions.assertFalse(Symbol.isValidSymbol("/abc")) // empty namespace
        Assertions.assertFalse(Symbol.isValidSymbol("ns/"))// no name
        Assertions.assertFalse(Symbol.isValidSymbol("+1"))// invalid first char
        Assertions.assertFalse(Symbol.isValidSymbol("-1"))// invalid first char
        Assertions.assertFalse(Symbol.isValidSymbol(".1"))// invalid first char
        Assertions.assertFalse(Symbol.isValidSymbol("+1"))// invalid first char
    }

    @Test
    fun isValidSymbolInvalidChar() {
        Assertions.assertFalse(Symbol.isValidSymbol("🎁"))
        Assertions.assertFalse(Symbol.isValidSymbol("#"))
        Assertions.assertFalse(Symbol.isValidSymbol("~"))
        Assertions.assertFalse(Symbol.isValidSymbol("#a"))
        Assertions.assertFalse(Symbol.isValidSymbol("~a"))
        Assertions.assertFalse(Symbol.isValidSymbol("a#"))
        Assertions.assertFalse(Symbol.isValidSymbol("a~"))
    }

    @Test
    fun isValidSymbol() {
        Assertions.assertTrue(Symbol.isValidSymbol("."))
        Assertions.assertTrue(Symbol.isValidSymbol("*"))
        Assertions.assertTrue(Symbol.isValidSymbol("+"))
        Assertions.assertTrue(Symbol.isValidSymbol("!"))
        Assertions.assertTrue(Symbol.isValidSymbol("-"))
        Assertions.assertTrue(Symbol.isValidSymbol("_"))
        Assertions.assertTrue(Symbol.isValidSymbol("?"))
        Assertions.assertTrue(Symbol.isValidSymbol("$"))
        Assertions.assertTrue(Symbol.isValidSymbol("%"))
        Assertions.assertTrue(Symbol.isValidSymbol("&"))
        Assertions.assertTrue(Symbol.isValidSymbol("="))
        Assertions.assertTrue(Symbol.isValidSymbol("<"))
        Assertions.assertTrue(Symbol.isValidSymbol(">"))
        Assertions.assertTrue(Symbol.isValidSymbol("🎁", true))
        Assertions.assertTrue(Symbol.isValidSymbol("a.b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a*b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a+b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a!b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a-b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a_b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a?b"))
        Assertions.assertTrue(Symbol.isValidSymbol($$"a$b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a%b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a&b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a=b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a<b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a>b"))
        Assertions.assertTrue(Symbol.isValidSymbol("a🎁b", true))
    }

    @Test
    fun parseNamespaceAndName() {
        Assertions.assertEquals(Symbol.symbol("ns", "abc"), Symbol.parse("ns/abc"))
    }

    @Test
    fun parseName() {
        Assertions.assertEquals(Symbol.symbol("abc"), Symbol.parse("abc"))
    }

    @Test
    fun parseInvalid() {
        Assertions.assertNull(Symbol.parse("")) //not valid
        Assertions.assertNull(Symbol.parse("/abc")) // empty namespace
        Assertions.assertNull(Symbol.parse("+1"))// invalid first char
        Assertions.assertNull(Symbol.parse("-1"))// invalid first char
        Assertions.assertNull(Symbol.parse(".1"))// invalid first char
        Assertions.assertNull(Symbol.parse("🎁"))
        Assertions.assertNull(Symbol.parse("#"))
        Assertions.assertNull(Symbol.parse("~"))
        Assertions.assertNull(Symbol.parse("#a"))
        Assertions.assertNull(Symbol.parse("~a"))
        Assertions.assertNull(Symbol.parse("a#"))
        Assertions.assertNull(Symbol.parse("a~"))
    }

    @Test
    fun parseInvalidWithNs() {
        Assertions.assertNull(Symbol.parse("ns/"))// no name
        Assertions.assertNull(Symbol.parse("ns/+1"))// invalid first char
        Assertions.assertNull(Symbol.parse("ns/-1"))// invalid first char
        Assertions.assertNull(Symbol.parse("ns/.1"))// invalid first char
        Assertions.assertNull(Symbol.parse("ns/🎁"))
        Assertions.assertNull(Symbol.parse("ns/#"))
        Assertions.assertNull(Symbol.parse("ns/~"))
        Assertions.assertNull(Symbol.parse("ns/#a"))
        Assertions.assertNull(Symbol.parse("ns/~a"))
        Assertions.assertNull(Symbol.parse("ns/a#"))
        Assertions.assertNull(Symbol.parse("ns/a~"))
    }


    @Test
    fun parseInvalidWitInvalidhNs() {
        Assertions.assertNull(Symbol.parse("/abc")) // empty namespace
        Assertions.assertNull(Symbol.parse("+1/abc"))// invalid first char
        Assertions.assertNull(Symbol.parse("-1/abc"))// invalid first char
        Assertions.assertNull(Symbol.parse(".1/abc"))// invalid first char
        Assertions.assertNull(Symbol.parse("🎁/abc"))
        Assertions.assertNull(Symbol.parse("#/abc"))
        Assertions.assertNull(Symbol.parse("~/abc"))
        Assertions.assertNull(Symbol.parse("#a/abc"))
        Assertions.assertNull(Symbol.parse("~a/abc"))
        Assertions.assertNull(Symbol.parse("a#/abc"))
        Assertions.assertNull(Symbol.parse("a~/abc"))
    }

    @Test
    fun symbolNamespaceAndName() {
        val xs = listOf(
            "", " ", "abc", "123", "#", "~", ".", "*", "+", "!", "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        )
        for (string in xs) {
            Symbol.symbol(string, "abc").let {
                Assertions.assertEquals(string, it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
        for (string in xs) {
            Symbol.symbol("a$string", "abc").let {
                Assertions.assertEquals("a$string", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
        for (string in xs) {
            Symbol.symbol(string + "a", "abc").let {
                Assertions.assertEquals(string + "a", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
        for (string in xs) {
            Symbol.symbol("a" + string + "b", "abc").let {
                Assertions.assertEquals("a" + string + "b", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
    }

    @Test
    fun symbolOnlyName() {
        val xs = listOf(
            "", " ", "abc", "123", "#", "~", ".", "*", "+", "!", "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        )
        for (string in xs) {
            Symbol.symbol(string).let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals(string, it.name)
            }
        }
        for (string in xs) {
            Symbol.symbol("a$string").let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals("a$string", it.name)
            }
        }
        for (string in xs) {
            Symbol.symbol(string + "a").let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals(string + "a", it.name)
            }
        }
        for (string in xs) {
            Symbol.symbol("a" + string + "b").let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals("a" + string + "b", it.name)
            }
        }
    }

    @Test
    fun get() {
        Assertions.assertEquals(Symbol.symbol("ns", "abc"), Symbol.get("ns/abc"))
        Assertions.assertThrows(IllegalArgumentException::class.java) { Symbol.get("ns/🎁") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Symbol.get("ns/") }// no name
        Assertions.assertThrows(IllegalArgumentException::class.java) { Symbol.get("/abc") } // empty namespace
        Assertions.assertThrows(IllegalArgumentException::class.java) { Symbol.get("+1") }// invalid first char
    }

    @Test
    fun compareTo() {
        let {
            val ks = listOf(
                Symbol.symbol(null, ""), Symbol.symbol("a", "b"),
                Symbol.symbol(null, "b"), Symbol.symbol("a", "🎁")
            )
            val res = ks.flatMap { k ->
                ks.map { b -> listOf(k, b).sorted().let { (a, b) -> a to b } }
            }
            val expect = listOf(
                Symbol.symbol(null, "") to Symbol.symbol(null, ""),
                Symbol.symbol(null, "") to Symbol.symbol("a", "b"),
                Symbol.symbol(null, "") to Symbol.symbol(null, "b"),
                Symbol.symbol(null, "") to Symbol.symbol("a", "🎁"),
                Symbol.symbol(null, "") to Symbol.symbol("a", "b"),
                Symbol.symbol("a", "b") to Symbol.symbol("a", "b"),
                Symbol.symbol(null, "b") to Symbol.symbol("a", "b"),
                Symbol.symbol("a", "b") to Symbol.symbol("a", "🎁"),
                Symbol.symbol(null, "") to Symbol.symbol(null, "b"),
                Symbol.symbol(null, "b") to Symbol.symbol("a", "b"),
                Symbol.symbol(null, "b") to Symbol.symbol(null, "b"),
                Symbol.symbol(null, "b") to Symbol.symbol("a", "🎁"),
                Symbol.symbol(null, "") to Symbol.symbol("a", "🎁"),
                Symbol.symbol("a", "b") to Symbol.symbol("a", "🎁"),
                Symbol.symbol(null, "b") to Symbol.symbol("a", "🎁"),
                Symbol.symbol("a", "🎁") to Symbol.symbol("a", "🎁")
            )
            Assertions.assertEquals(expect, res)
        }

        let {
            val ks = listOf(
                Symbol.symbol(""), Symbol.symbol("a"),
                Symbol.symbol("b"), Symbol.symbol("🎁")
            )
            val res = ks.flatMap { k ->
                ks.map { b -> listOf(k, b).sorted().let { (a, b) -> a to b } }
            }
            val expect = listOf(
                Symbol.symbol("") to Symbol.symbol(""), Symbol.symbol("") to Symbol.symbol("a"),
                Symbol.symbol("") to Symbol.symbol("b"), Symbol.symbol("") to Symbol.symbol("🎁"),
                Symbol.symbol("") to Symbol.symbol("a"), Symbol.symbol("a") to Symbol.symbol("a"),
                Symbol.symbol("a") to Symbol.symbol("b"), Symbol.symbol("a") to Symbol.symbol("🎁"),
                Symbol.symbol("") to Symbol.symbol("b"), Symbol.symbol("a") to Symbol.symbol("b"),
                Symbol.symbol("b") to Symbol.symbol("b"), Symbol.symbol("b") to Symbol.symbol("🎁"),
                Symbol.symbol("") to Symbol.symbol("🎁"), Symbol.symbol("a") to Symbol.symbol("🎁"),
                Symbol.symbol("b") to Symbol.symbol("🎁"), Symbol.symbol("🎁") to Symbol.symbol("🎁")
            )
            Assertions.assertEquals(expect, res)
        }
    }

    @Test
    fun equals() {
        Assertions.assertEquals(Symbol.symbol(null, "abc"), Symbol.symbol(null, "abc"))
        Assertions.assertEquals(Symbol.symbol("ns", "+"), Symbol.symbol("ns", "+"))
        Assertions.assertEquals(Symbol.symbol("ns", "abc"), Symbol.symbol("ns", "abc"))
        Assertions.assertNotEquals(Symbol.symbol(null, "abc"), Symbol.symbol("ns", "+"))
    }
}