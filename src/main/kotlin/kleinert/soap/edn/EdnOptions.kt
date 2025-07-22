package kleinert.soap.edn

import kleinert.soap.edn.ExtendedEDNDecoders.arrayDecoders
import kleinert.soap.edn.ExtendedEDNDecoders.listDecoders
import kleinert.soap.edn.ExtendedEDNDecoders.prettyDecoders
import kleinert.soap.data.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object ExtendedEDNDecoders {
    private fun ensureAllIntegral(iterable: Iterable<*>): Boolean {
        for (elem in iterable)
            if (elem !is Long && elem !is Int && elem !is Byte && elem !is Short && elem !is BigInteger)
                throw EdnReaderException.EdnClassConversionError("Requires integral number type, but got $elem of type ${elem?.javaClass ?: "null"}.")
        return true
    }

    private fun ensureAllFloaty(iterable: Iterable<*>): Boolean {
        for (elem in iterable)
           if (elem !is Number)
               throw EdnReaderException.EdnClassConversionError("Requires number type, but got $elem of type ${elem?.javaClass ?: "null"}.")
        return true
    }

    private inline fun <reified T> requireType(elem: Any?, s: String) {
        if (elem !is T)
            throw EdnReaderException.EdnClassConversionError("Needed $s, but got $elem of type ${elem?.javaClass ?: "null"}.")
    }

    private fun vectorToIntegralArray(it: Any?, typeId: Int): Any {
        requireType<List<*>>(it, "Vector")
        it as List<*>
        ensureAllIntegral(it)
        return when (typeId) {
            0 -> ByteArray(it.size).apply {
                for ((index, num) in it.withIndex())
                    this[index] = (num as Number).toByte()
            }

            1 -> ShortArray(it.size).apply {
                for ((index, num) in it.withIndex())
                    this[index] = (num as Number).toShort()
            }

            2 -> IntArray(it.size).apply {
                for ((index, num) in it.withIndex())
                    this[index] = (num as Number).toInt()
            }

            3 -> LongArray(it.size).apply {
                for ((index, num) in it.withIndex())
                    this[index] = (num as Number).toLong()
            }

            4 -> Array<BigInteger>(it.size) { BigInteger.ZERO }.apply {
                for ((index, num) in it.withIndex())
                    this[index] = BigInteger.valueOf((num as Number).toLong())
            }

            else -> throw IllegalStateException()
        }
    }

    private fun vectorToFloatyArray(it: Any?, typeId: Int): Any {
        requireType<List<*>>(it, "Vector or List")
        it as List<*>
        ensureAllFloaty(it)
        return when (typeId) {
            0 -> FloatArray(it.size).apply {
                for ((index, num) in it.withIndex())
                    this[index] = (num as Number).toFloat()
            }

            1 -> DoubleArray(it.size).apply {
                for ((index, num) in it.withIndex())
                    this[index] = (num as Number).toDouble()
            }

            2 -> Array<BigDecimal>(it.size) { BigDecimal.ZERO }.apply {
                for ((index, num) in it.withIndex())
                    this[index] = BigDecimal.valueOf((num as Number).toLong())
            }

            else -> throw IllegalStateException()
        }
    }

    private fun listToByteArray(it: Any?): ByteArray = vectorToIntegralArray(it, 0) as ByteArray
    private fun listToShortArray(it: Any?): ShortArray = vectorToIntegralArray(it, 1) as ShortArray
    private fun listToIntArray(it: Any?): IntArray = vectorToIntegralArray(it, 2) as IntArray
    private fun listToLongArray(it: Any?): LongArray = vectorToIntegralArray(it, 3) as LongArray
    private fun listToBigIntArray(it: Any?): Array<BigInteger> {
        require(it is List<*>)
        val res = Array(it.size) { index ->
            val v = it[index]
            require(v is Number)
            when (v) {
                is Byte,is Short,is Int,is Long -> v.toLong().toBigInteger()
                is BigInteger -> v
                else -> throw IllegalArgumentException("Element must be an Integral number type.")
            }
        }
        return res
    }

    private fun listToFloatArray(it: Any?): FloatArray = vectorToFloatyArray(it, 0) as FloatArray
    private fun listToDoubleArray(it: Any?): DoubleArray = vectorToFloatyArray(it, 1) as DoubleArray
    private fun listToBigDecimalArray(it: Any?): Array<BigDecimal> {
        require(it is List<*>)
        val res = Array(it.size) { index ->
            val v = it[index]
            require(v is Number)
            when (v) {
                is Byte,is Short,is Int,is Long -> v.toLong().toBigDecimal()
                is BigInteger -> v.toBigDecimal()
                is Float -> v.toBigDecimal()
                is Double -> v.toBigDecimal()
                is Ratio-> v.toBigDecimal()
                is Complex -> v.toBigDecimal()
                else -> throw IllegalArgumentException("Element must be a number type.")
            }
        }
        return res
    }

    private fun listToStringArray(it: Any?): Array<String> {
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        return it.map { it.toString() }.toTypedArray()
    }

    private fun listToArray(it: Any?): Array<Any?> {
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        return it.toTypedArray()
    }

    private fun listTo2dArray(it: Any?): Array<Array<Any?>> {
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        val arrays = it.map { e ->
            requireType<List<*>>(e, "List")
            require(e is List<*>)
            e.toTypedArray()
        }
        return arrays.toTypedArray()
    }

    private fun packed2dList(it: Any?): PackedList<Any?> {
        require(it is List<*>)
        val lst = mutableListOf<List<Any?>>()
        for (subList in it) {
            require(subList is List<*>)
            lst.add(subList)
        }
        return try {
            PackedList(lst)
        } catch (iae: IllegalArgumentException) {
            throw EdnReaderException.EdnClassConversionError(iae)
        }
    }

    private fun setToBitSet(it: Any?): Any {
        requireType<Set<*>>(it, "Set")
        require(it is Set<*>)
        return BitSet.valueOf(LongArray(it.size).apply {
            for ((index, num) in it.withIndex())
                this[index] = (num as Number).toLong()
        })
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun base64decode(it: Any?): Any {
        require(it is String)
        return String(Base64.Default.decode(it))
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun base64decodeUrlSafe(it: Any?): Any {
        require(it is String)
        return String(Base64.UrlSafe.decode(it))
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun base64DecodeMime(it: Any?): Any {
        require(it is String)
        return String(Base64.Mime.decode(it))
    }

    val arrayDecoders: Map<String, (Any?) -> Any?>
        get() = mapOf(
            "bytearray" to ExtendedEDNDecoders::listToByteArray,
            "shortarray" to ExtendedEDNDecoders::listToShortArray,
            "intarray" to ExtendedEDNDecoders::listToIntArray,
            "longarray" to ExtendedEDNDecoders::listToLongArray,
            "floatarray" to ExtendedEDNDecoders::listToFloatArray,
            "doublearray" to ExtendedEDNDecoders::listToDoubleArray,
            "bigintarray" to ExtendedEDNDecoders::listToBigIntArray,
            "bigdecimalarray" to ExtendedEDNDecoders::listToBigDecimalArray,
            "stringarray" to ExtendedEDNDecoders::listToStringArray,
            "array" to ExtendedEDNDecoders::listToArray,
            "array2d" to ExtendedEDNDecoders::listTo2dArray,
        )

    val listDecoders: Map<String, (Any?) -> Any?>
        get() = mapOf(
            "bitset" to ExtendedEDNDecoders::setToBitSet,
            "packed2D" to ExtendedEDNDecoders::packed2dList,
        )

    val prettyDecoders: Map<String, (Any?) -> Any?>
        get() = mapOf(
            "pretty" to { EDNSoapWriter.pprintToString(it) },
        )

    val base64Decoders: Map<String, (Any?) -> Any?>
        get() = mapOf(
            "base64" to ::base64decode,
            "base64urlSafe" to ::base64decodeUrlSafe,
            "base64mime" to ::base64DecodeMime,
        )
}

data class EDNSoapOptions(
    val allowSchemeUTF32Codes: Boolean = false,
    val allowDispatchChars: Boolean = false,
    val ednClassDecoders: Map<String, (Any?) -> Any?> = mapOf(),
    val ednClassEncoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(),
    val moreNumberPrefixes: Boolean = false,
    //val allowTimeDispatch: Boolean = false,
    val allowNumericSuffixes: Boolean = false,
    val allowMoreEncoderDecoderNames: Boolean = false,
    val decodingSequenceSeparator: String = ", ",
    val listToPersistentListConverter: (List<*>) -> List<*> = { PersistentList(it) },
    val listToPersistentVectorConverter: (List<*>) -> List<*> = { PersistentVector(it) },
    val setToPersistentSetConverter: (LinkedHashSet<*>) -> Set<*> = { PersistentSet.wrap(it, ordered = true) },
    val mapToPersistentMapConverter: (LinkedHashMap<*, *>) -> Map<*, *> = { PersistentMap.wrap(it, ordered = true) },
    val allowComplexNumberLiterals: Boolean = false,
    val allowUTFSymbols: Boolean = false,
    val sequenceElementLimit: Int = 10000,
) {
    companion object {
        val extendedOptions: EDNSoapOptions
            get() = extendedReaderOptions(mapOf())

        val allDecoders = arrayDecoders + listDecoders + prettyDecoders

        val defaultOptions: EDNSoapOptions
            get() = EDNSoapOptions()

        private fun extendedReaderOptions(ednClassDecoder: Map<String, (Any?) -> Any?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowDispatchChars = true,
                moreNumberPrefixes = true,
                allowNumericSuffixes = true,
                allowMoreEncoderDecoderNames = true,
                decodingSequenceSeparator = ", ",
                allowComplexNumberLiterals = true,
                allowUTFSymbols = true,
                ednClassDecoders = ednClassDecoder,
                ednClassEncoders = listOf(),
            )

        private fun extendedWriterOptions(ednClassEncoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowDispatchChars = true,
                moreNumberPrefixes = true,
                //allowTimeDispatch = true,
                allowNumericSuffixes = true,
                allowMoreEncoderDecoderNames = true,
                decodingSequenceSeparator = ", ",
                allowComplexNumberLiterals = true,
                allowUTFSymbols = true,
                ednClassDecoders = mapOf(),
                ednClassEncoders = ednClassEncoders,
            )
    }
}
