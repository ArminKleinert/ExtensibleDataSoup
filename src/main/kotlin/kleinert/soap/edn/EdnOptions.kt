package kleinert.soap.edn

import kleinert.soap.edn.ExtendedEDNDecoders.arrayDecoders
import kleinert.soap.edn.ExtendedEDNDecoders.listDecoders
import kleinert.soap.edn.ExtendedEDNDecoders.prettyDecoders
import kleinert.soap.data.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

object ExtendedEDNDecoders {
    private fun ensureAllIntegral(iterable: Iterable<*>): Boolean {
        for (elem in iterable)
            when (elem) {
                is Long, is Int, is Byte, is Short, is BigInteger -> null
                else -> throw EdnReaderException.EdnClassConversionError("Requires integral number type, but got $elem of type ${elem?.javaClass ?: "null"}.")
            }
        return true
    }

    private fun ensureAllFloaty(iterable: Iterable<*>): Boolean {
        for (elem in iterable)
            when (elem) {
                is Number -> null
                else -> throw EdnReaderException.EdnClassConversionError("Requires number type, but got $elem of type ${elem?.javaClass ?: "null"}.")
            }
        return true
    }

    private inline fun <reified T> requireType(elem: Any?, s: String) {
        if (elem !is T)
            throw EdnReaderException.EdnClassConversionError("Needed $s, but got $elem of type ${elem?.javaClass ?: "null"}.")
    }

    private inline fun <reified T> requireAllType(elem: Iterable<Any?>, s: String) {
        for (e in elem) requireType<T>(elem, s)
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
        val temp = vectorToIntegralArray(it, 4)
        require(temp is Array<*> && temp.isArrayOf<BigInteger>())
        return temp as Array<BigInteger>
    }

    private fun listToFloatArray(it: Any?): FloatArray = vectorToFloatyArray(it, 0) as FloatArray
    private fun listToDoubleArray(it: Any?): DoubleArray = vectorToFloatyArray(it, 1) as DoubleArray
    private fun listToBigDecimalArray(it: Any?): Array<BigDecimal> {
        val temp = vectorToFloatyArray(it, 2)
        require(temp is Array<*> && temp.isArrayOf<BigDecimal>())
        return temp as Array<BigDecimal>
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
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        for (e in it) {
            requireType<List<*>>(e, "List")
        }
        return try {
            PackedList(it as List<List<Any?>>)
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
}

data class EDNSoapOptions(
    val allowSchemeUTF32Codes: Boolean = false,
    val allowDispatchChars: Boolean = false,
    val ednClassDecoders: Map<String, (Any?) -> Any?> = mapOf(),
    val ednClassEncoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(),
    val allowTimeDispatch: Boolean = false,
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

        fun extendedReaderOptions(ednClassDecoder: Map<String, (Any?) -> Any?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowDispatchChars = true,
                allowTimeDispatch = true,
                allowNumericSuffixes = true,
                allowMoreEncoderDecoderNames = true,
                decodingSequenceSeparator = ", ",
                allowComplexNumberLiterals = true,
                allowUTFSymbols = true,
                ednClassDecoders = ednClassDecoder,
                ednClassEncoders = listOf(),
            )

        fun extendedWriterOptions(ednClassEncoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowDispatchChars = true,
                allowTimeDispatch = true,
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
