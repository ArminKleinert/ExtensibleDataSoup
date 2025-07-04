package kleinert.soap

import kleinert.soap.data.PersistentList
import kleinert.soap.data.PersistentVector
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.jvm.functions.FunctionN

object ExtendedEDNDecoders {
    private fun ensureAllIntegral(iterable: Iterable<*>): Boolean {
        for (elem in iterable)
            when (elem) {
                is Long, is Int, is Byte, is Short, is BigInteger -> null
                else -> throw EdnReaderException.EdnClassConversionError("Requires integral number type, but got $elem of type ${elem?.javaClass ?: "null"}.")
            }
        return true
    }

    private inline fun <reified T> requireType(elem: Any?, s: String) {
        if (elem !is T)
            throw EdnReaderException.EdnClassConversionError("Needed $s, but got $elem of type ${elem?.javaClass ?: "null"}.")
    }

    private inline fun <reified T> requireAllType(elem: Iterable<Any?>, s: String) {
        for (e in elem) requireType<Number>(elem, s)
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
        requireAllType<Number>(it, "Number")
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

    private fun vectorToByteArray(it: Any?): ByteArray = vectorToIntegralArray(it, 0) as ByteArray
    private fun vectorToShortArray(it: Any?): ShortArray = vectorToIntegralArray(it, 1) as ShortArray
    private fun vectorToIntArray(it: Any?): IntArray = vectorToIntegralArray(it, 2) as IntArray
    private fun vectorToLongArray(it: Any?): LongArray = vectorToIntegralArray(it, 3) as LongArray
    private fun vectorToBigIntArray(it: Any?): Array<BigInteger> {
        val temp = vectorToIntegralArray(it, 4)
        require(temp is Array<*> && temp.isArrayOf<BigInteger>())
        return temp as Array<BigInteger>
    }

    private fun vectorToFloatArray(it: Any?): FloatArray = vectorToFloatyArray(it, 0) as FloatArray
    private fun vectorToDoubleArray(it: Any?): DoubleArray = vectorToFloatyArray(it, 1) as DoubleArray
    private fun vectorToBigDecimalArray(it: Any?): Array<BigDecimal> {
        val temp = vectorToFloatyArray(it, 2)
        require(temp is Array<*> && temp.isArrayOf<BigDecimal>())
        return temp as Array<BigDecimal>
    }

    private fun vectorToStringArray(it: Any?): Array<String> {
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        return it.map { it.toString() }.toTypedArray()
    }

    private  fun vectorToArray(it: Any?): Array<Any?> {
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        return it.toTypedArray()
    }

    private fun vectorTo2dArray(it: Any?): Array<Array<Any?>> {
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        val arrays = it.map {
            require(it is List<*>)
            it.toTypedArray()
        }
        return arrays.toTypedArray()
    }

    private fun listToMatrix(it: Any?): List<List<Any?>> {
        requireType<List<*>>(it, "List")
        require(it is List<*>)
        var size = -1
        for (e in it) {
            require(e is List<*>)
            if (size == -1) size = it.size
            require(e.size == size)
        }
        return it as List<List<Any?>>
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
            "bytearray" to ::vectorToByteArray,
            "shortarray" to ::vectorToShortArray,
            "intarray" to ::vectorToIntArray,
            "longarray" to ::vectorToLongArray,
            "floatarray" to ::vectorToFloatArray,
            "doublearray" to ::vectorToDoubleArray,
            "bigintarray" to ::vectorToBigIntArray,
            "bigdecimalarray" to ::vectorToBigDecimalArray,
            "stringarray" to ::vectorToStringArray,
            "array" to ::vectorToArray,
            "bitset" to ::setToBitSet,
            "array2d" to ::vectorTo2dArray,
            "matrix" to ::listToMatrix,
        )

    val prettyDecoders: Map<String, (Any?) -> Any?>
        get() = mapOf(
            "pretty" to {EDNSoapWriter.pprintS(it)},
        )
}

data class EDNSoapOptions(
    val allowSchemeUTF32Codes: Boolean = false,
    val allowDispatchChars: Boolean = false,
    val ednClassDecoders: Map<String, (Any?) -> Any?> = mapOf(),
    val ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?> = mapOf(),
    val allowTimeDispatch: Boolean = false,
    val allowNumericSuffixes: Boolean = false,
    val allowMoreEncoderDecoderNames: Boolean = false,
    val decodingSequenceSeparator: String = ", ",
    val useFasterSetConstruction: Boolean = false,
    val forceImmutableCollections: Boolean = true,
    val listToPersistentListConverter: (List<*>) -> List<*> = { PersistentList(it) },
    val listToPersistentVectorConverter: (List<*>) -> List<*> = { PersistentVector(it) },
) {
    companion object {
        val extendedOptions: EDNSoapOptions
            get() = extendedReaderOptions(mapOf())

        val defaultOptions: EDNSoapOptions
            get() = EDNSoapOptions(
                allowSchemeUTF32Codes = false,
                allowDispatchChars = false,
                allowTimeDispatch = false,
                allowNumericSuffixes = false,
                allowMoreEncoderDecoderNames = false,
                decodingSequenceSeparator = ", ",
                forceImmutableCollections = true,
                useFasterSetConstruction = false,
                ednClassDecoders = mapOf(),
                ednClassEncoders = mapOf(),
            )

        fun extendedReaderOptions(ednClassDecoder: Map<String, (Any?) -> Any?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowDispatchChars = true,
                allowTimeDispatch = true,
                allowNumericSuffixes = true,
                allowMoreEncoderDecoderNames = true,
                decodingSequenceSeparator = ", ",
                forceImmutableCollections = true,
                useFasterSetConstruction = true,
                ednClassDecoders = ednClassDecoder,
                ednClassEncoders = mapOf(),
            )

        fun extendedWriterOptions(ednClassEncoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowDispatchChars = true,
                allowTimeDispatch = true,
                allowNumericSuffixes = true,
                allowMoreEncoderDecoderNames = true,
                decodingSequenceSeparator = ", ",
                forceImmutableCollections = true,
                useFasterSetConstruction = false,
                ednClassDecoders = mapOf(),
                ednClassEncoders = ednClassEncoders,
            )
    }
}
