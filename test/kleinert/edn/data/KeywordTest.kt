package kleinert.edn.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
    fun parseInvalidWitInvalidhNs() {
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
        Assertions.assertEquals(Keyword.keyword(null, "abc"), Keyword.get("abc"))
        Assertions.assertEquals(Keyword.keyword("ns", "+"), Keyword.get("ns/+"))
        Assertions.assertEquals(Keyword.keyword("ns", "abc"), Keyword.get("ns/abc"))
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword.get("ns/🎁") }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword.get("ns/") }// no name
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword.get("/abc") } // empty namespace
        Assertions.assertThrows(IllegalArgumentException::class.java) { Keyword.get("+1") }// invalid first char
    }

    @Test
    fun getSymbol() {
        Assertions.assertEquals(Keyword.get(Symbol.symbol("abc")), Keyword.get(Symbol.symbol("abc")))
        Assertions.assertEquals(Keyword.get(Symbol.symbol("ns", "+")), Keyword.get(Symbol.symbol("ns","+")))
        Assertions.assertEquals(Keyword.get(Symbol.symbol("ns", "abc")), Keyword.get(Symbol.symbol("ns","abc")))
    }

    @Test
    fun findSymbol() {
        TODO()
    }

    @Test
    fun findString() {
        TODO()
    }

    @Test
    fun compareTo() {
        TODO()
    }
}