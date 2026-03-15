package kleinert.edn.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

class KeywordTest {
    @Test
    fun getFullyQualifiedString() {
        Assertions.assertFalse(Keyword.keyword(null, "abc").fullyQualified)
        Assertions.assertFalse(Keyword.keyword("abc").fullyQualified)
        Assertions.assertTrue(Keyword.keyword("ns", "abc").fullyQualified)
        Assertions.assertTrue(Keyword.parse(":ns/abc")!!.fullyQualified)

        // Corner case: Empty string as ns is valid.
        Assertions.assertTrue(Keyword.keyword("", "abc").fullyQualified)
    }

    @Test
    fun getFullyQualifiedSymbol() {
        Assertions.assertFalse(Keyword.keyword(Symbol.symbol(null, "abc")).fullyQualified)
        Assertions.assertFalse(Keyword.keyword(Symbol.symbol("abc")).fullyQualified)
        Assertions.assertTrue(Keyword.keyword(Symbol.symbol("ns", "abc")).fullyQualified)
        Assertions.assertTrue(Keyword.keyword(Symbol.parse("ns/abc")!!).fullyQualified)

        // Corner case: Empty string as ns is valid.
        Assertions.assertTrue(Keyword.keyword(Symbol.symbol("", "abc")).fullyQualified)

        // Corner case: A single slash is a valid symbol but does not have a namespace.
        Assertions.assertFalse(Keyword.keyword(Symbol.parse("/")!!).fullyQualified)
    }


    @Test
    fun getNamespace() {
        Keyword.keyword(null, "abc").let {
            Assertions.assertEquals(null, it.namespace)
        }
        Keyword.keyword("abc").let {
            Assertions.assertEquals(null, it.namespace)
        }
        Keyword.keyword("ns", "abc").let {
            Assertions.assertEquals("ns", it.namespace)
        }
        Keyword.keyword("", "abc").let {
            Assertions.assertEquals("", it.namespace)
        }
        Keyword.keyword("", "").let {
            Assertions.assertEquals("", it.namespace)
        }
    }

    @Test
    fun getName() {
        Keyword.keyword(null, "abc").let {
            Assertions.assertEquals("abc", it.name)
        }
        Keyword.keyword("abc").let {
            Assertions.assertEquals("abc", it.name)
        }
        Keyword.keyword("ns", "abc").let {
            Assertions.assertEquals("abc", it.name)
        }
        Keyword.keyword("").let {
            Assertions.assertEquals("", it.name)
        }
        Keyword.keyword("", "").let {
            Assertions.assertEquals("", it.name)
        }
    }

    @Test
    fun sym() {
        val xs = listOf(
            "", " ", "abc", "123", "#", "~", ".", "*", "+", "!", "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        )
        for (string in xs) {
            Keyword.keyword(Symbol.symbol(string)).let {
                Assertions.assertEquals(Symbol.symbol(string), it.sym)
            }
            Keyword.keyword(Symbol.symbol("a" + string + "b", "abc")).let {
                Assertions.assertEquals(
                    Symbol.symbol("a" + string + "b", "abc"), it.sym
                )
            }
        }
    }

    @Test
    fun parseNamespaceAndName() {
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword.parse(":ns/abc"))
    }

    @Test
    fun parseName() {
        Assertions.assertEquals(Keyword.keyword("abc"), Keyword.parse(":abc"))
    }

    @Test
    fun parseInvalid() {
        Assertions.assertNull(Keyword.parse(":")) //not valid
        Assertions.assertNull(Keyword.parse(":/abc")) // empty namespace
        Assertions.assertNull(Keyword.parse(":+1"))// invalid first char
        Assertions.assertNull(Keyword.parse(":-1"))// invalid first char
        Assertions.assertNull(Keyword.parse(":.1"))// invalid first char
        Assertions.assertNull(Keyword.parse(":🎁"))
        Assertions.assertNull(Keyword.parse(":#"))
        Assertions.assertNull(Keyword.parse(":~"))
        Assertions.assertNull(Keyword.parse(":#a"))
        Assertions.assertNull(Keyword.parse(":~a"))
        Assertions.assertNull(Keyword.parse(":a#"))
        Assertions.assertNull(Keyword.parse(":a~"))
    }

    @Test
    fun parseInvalidWithNs() {
        Assertions.assertNull(Keyword.parse(":ns/"))// no name
        Assertions.assertNull(Keyword.parse(":ns/+1"))// invalid first char
        Assertions.assertNull(Keyword.parse(":ns/-1"))// invalid first char
        Assertions.assertNull(Keyword.parse(":ns/.1"))// invalid first char
        Assertions.assertNull(Keyword.parse(":ns/🎁"))
        Assertions.assertNull(Keyword.parse(":ns/#"))
        Assertions.assertNull(Keyword.parse(":ns/~"))
        Assertions.assertNull(Keyword.parse(":ns/#a"))
        Assertions.assertNull(Keyword.parse(":ns/~a"))
        Assertions.assertNull(Keyword.parse(":ns/a#"))
        Assertions.assertNull(Keyword.parse(":ns/a~"))
    }


    @Test
    fun parseInvalidWitInvalidNs() {
        Assertions.assertNull(Keyword.parse(":/abc")) // empty namespace
        Assertions.assertNull(Keyword.parse(":+1/abc"))// invalid first char
        Assertions.assertNull(Keyword.parse(":-1/abc"))// invalid first char
        Assertions.assertNull(Keyword.parse(":.1/abc"))// invalid first char
        Assertions.assertNull(Keyword.parse(":🎁/abc"))
        Assertions.assertNull(Keyword.parse(":#/abc"))
        Assertions.assertNull(Keyword.parse(":~/abc"))
        Assertions.assertNull(Keyword.parse(":#a/abc"))
        Assertions.assertNull(Keyword.parse(":~a/abc"))
        Assertions.assertNull(Keyword.parse(":a#/abc"))
        Assertions.assertNull(Keyword.parse(":a~/abc"))
    }

    @Test
    fun keywordNamespaceAndName() {
        val xs = listOf(
            "", " ", "abc", "123", "#", "~", ".", "*", "+", "!", "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        )
        for (string in xs) {
            Keyword.keyword(string, "abc").let {
                Assertions.assertEquals(string, it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
        for (string in xs) {
            Keyword.keyword("a$string", "abc").let {
                Assertions.assertEquals("a$string", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
        for (string in xs) {
            Keyword.keyword(string + "a", "abc").let {
                Assertions.assertEquals(string + "a", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
        for (string in xs) {
            Keyword.keyword("a" + string + "b", "abc").let {
                Assertions.assertEquals("a" + string + "b", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
    }

    @Test
    fun keywordOnlyName() {
        val xs = listOf(
            "", " ", "abc", "123", "#", "~", ".", "*", "+", "!", "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        )
        for (string in xs) {
            Keyword.keyword(string).let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals(string, it.name)
            }
        }
        for (string in xs) {
            Keyword.keyword("a$string").let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals("a$string", it.name)
            }
        }
        for (string in xs) {
            Keyword.keyword(string + "a").let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals(string + "a", it.name)
            }
        }
        for (string in xs) {
            Keyword.keyword("a" + string + "b").let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals("a" + string + "b", it.name)
            }
        }
    }

    @Test
    fun keywordSymbol() {
        val xs = listOf(
            "", " ", "abc", "123", "#", "~", ".", "*", "+", "!", "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        )
        for (string in xs) {
            Keyword.keyword(Symbol.symbol(string)).let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals(string, it.name)
            }
            Keyword.keyword(Symbol.symbol("a" + string + "b", "abc")).let {
                Assertions.assertEquals("a" + string + "b", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
    }

    @Test
    fun internOnlyName() {
        val xs = listOf(
            "", " ", "abc", "123", "#", "~", ".", "*", "+", "!", "-", "_", "?", "$", "%", "&", "=", "<", ">", "🎁"
        )
        for (string in xs) {
            Keyword.intern(Symbol.symbol(string)).let {
                Assertions.assertEquals(null, it.namespace)
                Assertions.assertEquals(string, it.name)
            }
            Keyword.intern(Symbol.symbol("a" + string + "b", "abc")).let {
                Assertions.assertEquals("a" + string + "b", it.namespace)
                Assertions.assertEquals("abc", it.name)
            }
        }
    }

    @Test
    fun get() {
        Assertions.assertEquals(Keyword.keyword(null, "abc"), Keyword["abc"])
        Assertions.assertEquals(Keyword.keyword("ns", "+"), Keyword["ns/+"])
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword["ns/abc"])
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword["ns/🎁"] }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword["ns/"] }// no name
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword["/abc"] } // empty namespace
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword["+1"] }// invalid first char
    }

    @Test
    fun getSymbol() {
        Assertions.assertSame(Keyword[Symbol.symbol("abc")], Keyword[Symbol.symbol("abc")])
        Assertions.assertSame(Keyword[Symbol.symbol("ns", "+")], Keyword[Symbol.symbol("ns", "+")])
        Assertions.assertSame(Keyword[Symbol.symbol("ns", "abc")], Keyword[Symbol.symbol("ns", "abc")])
    }

    @Test
    fun findSymbol() {
        val random = Random(0xCAFEC0FFEE)
        Symbol.symbol(random.nextDouble().toString()).let { // A hopefully random name
            Assertions.assertNull(Keyword.find(it)) // Did not exist.
            Assertions.assertNull(Keyword.find(it)) // Was not interned by the first call.

            val k = Keyword.intern(it) // Create and intern
            Assertions.assertEquals(k, Keyword.find(it))
        }
    }

    @Test
    fun findString() {
        val random = Random(0xDEADBEEF)
        random.nextDouble().toString().let { // A hopefully random name
            Assertions.assertNull(Keyword.find(it)) // Did not exist.
            Assertions.assertNull(Keyword.find(it)) // Was not interned by the first call.

            val k = Keyword.intern(Symbol.symbol(it)) // Create and intern
            Assertions.assertEquals(k, Keyword.find(it))
        }
    }

    @Test
    fun compareTo() {
        let {
            val ks = listOf(
                Keyword.keyword(null, ""), Keyword.keyword("a", "b"),
                Keyword.keyword(null, "b"), Keyword.keyword("a", "🎁")
            )
            val res = ks.flatMap { k ->
                ks.map { b -> listOf(k, b).sorted().let { (a, b) -> a to b } }
            }
            val expect = listOf(
                Keyword.keyword(null, "") to Keyword.keyword(null, ""),
                Keyword.keyword(null, "") to Keyword.keyword("a", "b"),
                Keyword.keyword(null, "") to Keyword.keyword(null, "b"),
                Keyword.keyword(null, "") to Keyword.keyword("a", "🎁"),
                Keyword.keyword(null, "") to Keyword.keyword("a", "b"),
                Keyword.keyword("a", "b") to Keyword.keyword("a", "b"),
                Keyword.keyword(null, "b") to Keyword.keyword("a", "b"),
                Keyword.keyword("a", "b") to Keyword.keyword("a", "🎁"),
                Keyword.keyword(null, "") to Keyword.keyword(null, "b"),
                Keyword.keyword(null, "b") to Keyword.keyword("a", "b"),
                Keyword.keyword(null, "b") to Keyword.keyword(null, "b"),
                Keyword.keyword(null, "b") to Keyword.keyword("a", "🎁"),
                Keyword.keyword(null, "") to Keyword.keyword("a", "🎁"),
                Keyword.keyword("a", "b") to Keyword.keyword("a", "🎁"),
                Keyword.keyword(null, "b") to Keyword.keyword("a", "🎁"),
                Keyword.keyword("a", "🎁") to Keyword.keyword("a", "🎁")
            )
            Assertions.assertEquals(expect, res)
        }

        let {
            val ks = listOf(
                Keyword.keyword(""), Keyword.keyword("a"),
                Keyword.keyword("b"), Keyword.keyword("🎁")
            )
            val res = ks.flatMap { k ->
                ks.map { b -> listOf(k, b).sorted().let { (a, b) -> a to b } }
            }
            val expect = listOf(
                Keyword.keyword("") to Keyword.keyword(""), Keyword.keyword("") to Keyword.keyword("a"),
                Keyword.keyword("") to Keyword.keyword("b"), Keyword.keyword("") to Keyword.keyword("🎁"),
                Keyword.keyword("") to Keyword.keyword("a"), Keyword.keyword("a") to Keyword.keyword("a"),
                Keyword.keyword("a") to Keyword.keyword("b"), Keyword.keyword("a") to Keyword.keyword("🎁"),
                Keyword.keyword("") to Keyword.keyword("b"), Keyword.keyword("a") to Keyword.keyword("b"),
                Keyword.keyword("b") to Keyword.keyword("b"), Keyword.keyword("b") to Keyword.keyword("🎁"),
                Keyword.keyword("") to Keyword.keyword("🎁"), Keyword.keyword("a") to Keyword.keyword("🎁"),
                Keyword.keyword("b") to Keyword.keyword("🎁"), Keyword.keyword("🎁") to Keyword.keyword("🎁")
            )
            Assertions.assertEquals(expect, res)
        }
    }

    @Test
    fun equals() {
        Assertions.assertEquals(Keyword.keyword(null, "abc"), Keyword.keyword(null, "abc"))
        Assertions.assertEquals(Keyword.keyword("ns", "+"), Keyword.keyword("ns", "+"))
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword.keyword("ns", "abc"))

        Assertions.assertNotEquals(Keyword.keyword(null, "abc"), Keyword.keyword("ns", "+"))
    }

    @Test
    fun equalsBecauseInterned() {
        Assertions.assertSame(Keyword.keyword(null, "abc"), Keyword.keyword(null, "abc"))
        Assertions.assertSame(Keyword.keyword("ns", "+"), Keyword.keyword("ns", "+"))
        Assertions.assertSame(Keyword.keyword("ns", "abc"), Keyword.keyword("ns", "abc"))

        Assertions.assertNotSame(Keyword.keyword(null, "abc"), Keyword.keyword("ns", "+"))
    }
}
