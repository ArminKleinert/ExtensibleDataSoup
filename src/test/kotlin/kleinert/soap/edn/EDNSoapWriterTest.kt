package kleinert.soap.edn

import kleinert.soap.data.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

class EDNSoapWriterTest {
    @Test
    fun encodeNull() {
        Assertions.assertEquals("nil", EDN.pprintToString(null))
    }

    @Test
    fun encodeTrue() {
        Assertions.assertEquals("true", EDN.pprintToString(true))
    }

    @Test
    fun encodeFalse() {
        Assertions.assertEquals("false", EDN.pprintToString(false))
    }

    @Test
    fun encodeString() {
        Assertions.assertEquals("\"\"", EDN.pprintToString(""))
        Assertions.assertEquals("\"abc\"", EDN.pprintToString("abc"))
    }

    @Test
    fun encodeChar() {
        Assertions.assertEquals("\\a", EDN.pprintToString('a'))
    }

    @Test
    fun encodeCharSpecial() {
        Assertions.assertEquals("\\newline", EDN.pprintToString('\n'))
        Assertions.assertEquals("\\space", EDN.pprintToString(' '))
        Assertions.assertEquals("\\tab", EDN.pprintToString('\t'))
        Assertions.assertEquals("\\backspace", EDN.pprintToString('\b'))
        Assertions.assertEquals("\\formfeed", EDN.pprintToString(12.toChar()))
        Assertions.assertEquals("\\return", EDN.pprintToString('\r'))
    }

    @Test
    fun encodeCharCode() {
        Assertions.assertEquals('\u271D', EDN.read("\\u271D")) // Latin cross
    }

    @Test
    fun encodeByte() {
        Assertions.assertEquals("0", EDN.pprintToString(0.toByte()))
        Assertions.assertEquals("1", EDN.pprintToString(1.toByte()))
        Assertions.assertEquals("-1", EDN.pprintToString((-1).toByte()))
    }

    @Test
    fun encodeShort() {
        Assertions.assertEquals("0", EDN.pprintToString(0.toShort()))
        Assertions.assertEquals("1", EDN.pprintToString(1.toShort()))
        Assertions.assertEquals("-1", EDN.pprintToString((-1).toShort()))
    }

    @Test
    fun encodeInt() {
        Assertions.assertEquals("0", EDN.pprintToString(0))
        Assertions.assertEquals("1", EDN.pprintToString(1))
        Assertions.assertEquals("-1", EDN.pprintToString(-1))
    }

    @Test
    fun encodeLong() {
        Assertions.assertEquals("0", EDN.pprintToString(0L))
        Assertions.assertEquals("1", EDN.pprintToString(1L))
        Assertions.assertEquals("-1", EDN.pprintToString(-1L))
    }

    @Test
    fun encodeFloat() {
        Assertions.assertEquals("0.0", EDN.pprintToString(0.0))
        Assertions.assertEquals("1.0", EDN.pprintToString(1.0))
        Assertions.assertEquals("1.5", EDN.pprintToString(1.5))
        Assertions.assertEquals("-1.5", EDN.pprintToString(-1.5))
    }

    @Test
    fun encodeDouble() {
        Assertions.assertEquals("0.0", EDN.pprintToString(0.0))
        Assertions.assertEquals("1.0", EDN.pprintToString(1.0))
        Assertions.assertEquals("1.5", EDN.pprintToString(1.5))
        Assertions.assertEquals("-1.5", EDN.pprintToString(-1.5))
    }

    @Test
    fun encodeRatio() {
        Assertions.assertEquals("123/8", EDN.pprintToString(Ratio.valueOf(123, 8)))
    }

    @Test
    fun encodeBigInteger() {
        Assertions.assertEquals("123N", EDN.pprintToString(BigInteger.valueOf(123L)))
    }

    @Test
    fun encodeBigDecimal() {
        Assertions.assertEquals("123M", EDN.pprintToString(BigDecimal.valueOf(123L)))
    }

    @Test
    fun encodeMapEntry() {
        Assertions.assertEquals("[2, 33]", EDN.pprintToString(mapOf(2 to 33).entries.first()))
    }

    @Test
    fun encodeIObj() {
        Assertions.assertEquals("^{:tag \"abc\"} :a", EDN.pprintToString(IObj.valueOf("abc", Keyword["a"])))
        Assertions.assertEquals("^{:abc true} :a", EDN.pprintToString(IObj.valueOf(Keyword["abc"], Keyword["a"])))
        Assertions.assertEquals("^{:tag abc} :a", EDN.pprintToString(IObj.valueOf(Symbol.symbol("abc"), Keyword["a"])))
        Assertions.assertEquals("^{\"b\" \"c\"} :a", EDN.pprintToString(IObj.valueOf(mapOf("b" to "c"), Keyword["a"])))
    }

    @Test
    fun encodePersistentList() {
        Assertions.assertEquals("()", EDN.pprintToString(PersistentList.of<Keyword>()))
        Assertions.assertEquals("(:a, :b)", EDN.pprintToString(PersistentList.of(Keyword["a"], Keyword["b"])))
    }

    @Test
    fun encodePair() {
        Assertions.assertEquals("[2, 33]", EDN.pprintToString(2 to 33))
    }

    @Test
    fun encodeByteArray() {
        Assertions.assertEquals("[]", EDN.pprintToString(ByteArray(0)))
        Assertions.assertEquals("[0, 0]", EDN.pprintToString(ByteArray(2)))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            ByteArray::class.java to { "bytearray" to (it as ByteArray).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals("#bytearray [0, 0]", EDN.pprintToString(ByteArray(2), options))
    }

    @Test
    fun encodeShortArray() {
        Assertions.assertEquals("[]", EDN.pprintToString(ShortArray(0)))
        Assertions.assertEquals("[0, 0]", EDN.pprintToString(ShortArray(2)))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            ShortArray::class.java to { "shortarray" to (it as ShortArray).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals("#shortarray [0, 0]", EDN.pprintToString(ShortArray(2), options))
    }

    @Test
    fun encodeIntArray() {
        Assertions.assertEquals("[]", EDN.pprintToString(IntArray(0)))
        Assertions.assertEquals("[0, 0]", EDN.pprintToString(IntArray(2)))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            IntArray::class.java to { "intarray" to (it as IntArray).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals("#intarray [0, 0]", EDN.pprintToString(IntArray(2), options))
    }

    @Test
    fun encodeLongArray() {
        Assertions.assertEquals("[]", EDN.pprintToString(LongArray(0)))
        Assertions.assertEquals("[0, 0]", EDN.pprintToString(LongArray(2)))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            LongArray::class.java to { "longarray" to (it as LongArray).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals("#longarray [0, 0]", EDN.pprintToString(LongArray(2), options))
    }

    @Test
    fun encodeFloatArray() {
        Assertions.assertEquals("[]", EDN.pprintToString(FloatArray(0)))
        Assertions.assertEquals("[0.0, 0.0]", EDN.pprintToString(FloatArray(2)))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            FloatArray::class.java to { "floatarray" to (it as FloatArray).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals("#floatarray [0.0, 0.0]", EDN.pprintToString(FloatArray(2), options))
    }

    @Test
    fun encodeDoubleArray() {
        Assertions.assertEquals("[]", EDN.pprintToString(DoubleArray(0)))
        Assertions.assertEquals("[0.0, 0.0]", EDN.pprintToString(DoubleArray(2)))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            DoubleArray::class.java to { "doublearray" to (it as DoubleArray).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals("#doublearray [0.0, 0.0]", EDN.pprintToString(DoubleArray(2), options))
    }

    @Test
    fun encodeArray() {
        Assertions.assertEquals("[]", EDN.pprintToString(arrayOf<Keyword>()))
        Assertions.assertEquals("[:a, :b]", EDN.pprintToString(arrayOf(Keyword["a"], Keyword["b"])))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            Array::class.java to { "array" to (it as Array<*>).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals("#array [:a, :b]", EDN.pprintToString(arrayOf(Keyword["a"], Keyword["b"]), options))
    }

    @Test
    fun encodeVector() {
        Assertions.assertEquals("[]", EDN.pprintToString(PersistentVector.of<Keyword>()))
        Assertions.assertEquals("[:a, :b]", EDN.pprintToString(PersistentVector.of(Keyword["a"], Keyword["b"])))
        Assertions.assertEquals("[:a, :b]", EDN.pprintToString(listOf(Keyword["a"], Keyword["b"])))
    }

    @Test
    fun encodeSet() {
        Assertions.assertEquals("#{}", EDN.pprintToString(PersistentSet.of<Keyword>()))
        Assertions.assertEquals("#{:a, :b}", EDN.pprintToString(PersistentSet.of(Keyword["a"], Keyword["b"])))

        val encoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(
            PersistentSet::class.java to { "set" to (it as Set<*>).toList() }
        )
        val options = EDNSoapOptions.defaultOptions.copy(ednClassEncoders = encoders)
        Assertions.assertEquals(
            "#set [:a, :b]",
            EDN.pprintToString(PersistentSet.of(Keyword["a"], Keyword["b"]), options)
        )
    }

    @Test
    fun encodeMap() {
        Assertions.assertEquals("{}", EDN.pprintToString(PersistentMap.of<Keyword, Keyword>()))
        Assertions.assertEquals(
            "{:a :b, :c :d}",
            EDN.pprintToString(PersistentMap.of(Keyword["a"] to Keyword["b"], Keyword["c"] to Keyword["d"]))
        )
    }

    @Test
    fun encodeIterable() {
        Assertions.assertEquals("()", EDN.pprintToString(IntProgression.fromClosedRange(0, -1, step = 2)))
        Assertions.assertEquals("(0, 1)", EDN.pprintToString(0..1))
    }

    @Test
    fun encodeSequence() {
        Assertions.assertEquals("()", EDN.pprintToString(sequenceOf<Keyword>()))
        Assertions.assertEquals("(:a, :b)", EDN.pprintToString(sequenceOf(Keyword["a"], Keyword["b"])))
    }

    @Test
    fun encodeUUID() {
        Assertions.assertEquals(
            "#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"",
            EDN.pprintToString(UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"))
        )
    }

    @Test
    fun encodeInstant() {
        Assertions.assertEquals(
            "#inst \"1985-04-12T23:20:50.520Z\"",
            EDN.pprintToString(Instant.parse("1985-04-12T23:20:50.520Z"))
        )
    }

    @Test
    fun encodeNaN() {
        Assertions.assertEquals("##NaN", EDN.pprintToString(Double.NaN))
        Assertions.assertEquals("##NaN", EDN.pprintToString(-Double.NaN))
    }

    @Test
    fun encodeInfinity() {
        Assertions.assertEquals("##INF", EDN.pprintToString(Double.POSITIVE_INFINITY))
        Assertions.assertEquals("##-INF", EDN.pprintToString(Double.NEGATIVE_INFINITY))
    }

    @Test
    fun encodeKeyword() {
        Assertions.assertEquals(":abc", EDN.pprintToString(Keyword["abc"]))
    }

    @Test
    fun encodeSymbol() {
        Assertions.assertEquals("abc", EDN.pprintToString(Symbol.symbol("abc")))
    }
}