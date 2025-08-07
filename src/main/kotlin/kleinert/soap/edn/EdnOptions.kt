package kleinert.soap.edn

import kleinert.soap.data.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * TODO
 *
 * @author Armin Kleinert
 */
object ExtendedEDNDecoders {
    private fun ensureAllIntegral(iterable: Iterable<*>): Boolean {
        for (elem in iterable)
            if (elem !is Long && elem !is Int && elem !is Byte && elem !is Short && elem !is BigInteger)
                throw IllegalArgumentException("Requires integral number type, but got $elem of type ${elem?.javaClass ?: "null"}.")
        return true
    }

    private fun ensureAllFloaty(iterable: Iterable<*>): Boolean {
        for (elem in iterable)
            if (elem !is Number)
                throw IllegalArgumentException("Requires number type, but got $elem of type ${elem?.javaClass ?: "null"}.")
        return true
    }

    private inline fun <reified T> requireType(elem: Any?, s: String) {
        if (elem !is T)
            throw IllegalArgumentException("Needed $s, but got $elem of type ${elem?.javaClass ?: "null"}.")
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
                    this[index] = BigDecimal.valueOf((num as Number).toDouble())
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
                is Byte, is Short, is Int, is Long -> v.toLong().toBigInteger()
                is BigInteger -> v
                else -> throw IllegalArgumentException("Element must be an Integral number type, but is $v (${v.javaClass}).")
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
                is Byte, is Short, is Int, is Long -> BigDecimal.valueOf(v.toLong())
                is BigInteger -> BigDecimal(v)
                is Float, is Double -> BigDecimal.valueOf(v.toDouble())
                is Ratio -> v.toBigDecimal()
                is Complex -> v.toBigDecimal()
                is BigDecimal -> v
                else -> throw IllegalArgumentException("Element must be a number type, but is $v (${v.javaClass}).")
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

    private fun packed2dList(it: Any?): PackedList2D<Any?> {
        require(it is List<*>)
        val lst = mutableListOf<List<Any?>>()
        for (subList in it) {
            require(subList is List<*>)
            lst.add(subList)
        }
        return PackedList2D(lst)
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

/**
 * Options for [EDN.pprint] and [EDN.read].
 *
 * @param allowComplexNumberLiterals Allows [Complex] literals to be read. They must have the form "([+\\-]?\\d+(.\\d)?+i)|([+\\-]?\\d+(.\\d+)?[+\\-](\\d+(.\\d+)?)?i)". [Complex.toString] should be sensible enough to allow printing without this option.
 * @param allowDispatchChars Allow reading and writing of character literals with a '#' as a prefix. The char is read as a [Char32]. For [EDN.pprint], the [Char32] is printed as a string literal without this option.
 * @param allowMoreEncoderDecoderNames By default, the names of decoders for [EDN.read] and encoders for [EDN.pprint] must be valid [Symbol] strings with a namespace. With this option, the namespace is optional.
 * @param allowNumericSuffixes Allow numeric suffixes to specify the number type. "_i8" means [Byte], "_i16" means [Short], "_i32" means [Int]. "_i64" and "L" specify [Long], but all numbers which aren't [BigInteger], [BigDecimal], or [Double], are considered to be [Long] by default.
 * @param allowSchemeUTF32Codes Option for [EDN.read] only. Allow [Char32] literals of the form "\\xXXXXXXXX" where X is a hexadecimal digit.
 * @param allowUTFSymbols For [EDN.read] only. By the EDN specification, [Symbol] names and namespaces can only contain 16-bit chars. This option allows the usage of the full unicode spectrum.
 * @param ednClassDecoders Option for [EDN.read] only. This Map contains string keys and `(Any?)->Any?` functions as values. These can be used to create more complex objects from EDN-objects. See examples at [EDN.read].
 * @param ednClassEncoders For [EDN.pprint] only. A list of Pairs of [Class] objects and functions which convert objects into tagged EDN objects. See examples at [EDN.pprint].
 * @param encoderCollectionElementLimit Only for [EDN.pprint]. Print a maximum of this many elements for collections (Lists, Vectors, Maps, Sets, etc.) before truncating with "...".
 * @param encoderLineIndent For [EDN.pprint] only.
 * @param encoderMaxColumn For [EDN.pprint] only.
 * @param encoderSequenceElementLimit Only for [EDN.pprint]. Print a maximum of this many elements for [Sequence]s before truncating with "...".
 * @param encodingSequenceSeparator Only for [EDN.pprint]. Specifies the separator between elements in printed [Collection] and [Sequence] objects. The default is ", ", but " " is also a good choice.
 * @param listToPersistentListConverter Option for [EDN.read] only. A function which takes a [List] and returns a [List]. The default is the construction of a [PersistentList]. This function is used when reading lists, but not vectors.
 * @param listToPersistentVectorConverter Option for [EDN.read] only. A function which takes a [List] and returns a [List]. The default is the construction of a [PersistentVector]. This function is used when reading vectors, but not lists.
 * @param mapToPersistentMapConverter Option for [EDN.read] only. By default, Maps are ordered (for example, [LinkedHashMap]). This function allows the user to convert it to another [Map] type. The default is the construction of a [PersistentMap].
 * @param moreNumberPrefixes Option for [EDN.read] only. Allows more prefixes for integral numbers with different bases: "0o" for octal, "0b" for binary.
 * @param setToPersistentSetConverter Option for [EDN.read] only. By default, Sets are ordered (for example, [LinkedHashSet]). The user might want unordered Sets. This options allows user-defined conversion. The default is the construction of a [PersistentSet].
 *
 * @author Armin Kleinert
 */
data class EDNSoapOptions(
    val allowSchemeUTF32Codes: Boolean = false,
    val allowDispatchChars: Boolean = false,
    val ednClassDecoders: Map<String, (Any?) -> Any?> = mapOf(),
    val ednClassEncoders: List<Pair<Class<*>, (Any) -> Pair<String, Any?>?>> = listOf(),
    val moreNumberPrefixes: Boolean = false,
    //val allowTimeDispatch: Boolean = false,
    val allowNumericSuffixes: Boolean = false,
    val allowMoreEncoderDecoderNames: Boolean = false,
    val encodingSequenceSeparator: String = ", ",
    val listToPersistentListConverter: (List<*>) -> List<*> = { PersistentList.wrap(it) },
    val listToPersistentVectorConverter: (List<*>) -> List<*> = { PersistentVector.wrap(it) },
    val setToPersistentSetConverter: (Set<*>) -> Set<*> = { PersistentSet.wrap(it, ordered = true) },
    val mapToPersistentMapConverter: (Map<*, *>) -> Map<*, *> = { PersistentMap.wrap(it, ordered = true) },
    val allowComplexNumberLiterals: Boolean = false,
    val allowUTFSymbols: Boolean = false,
    val encoderSequenceElementLimit: Int = 1000,
    val encoderCollectionElementLimit: Int = 10000,
    val encoderMaxColumn: Int = 80,
    val encoderLineIndent: String = "  ",
) {
    companion object {
        val extendedOptions: EDNSoapOptions
            get() = extendedReaderOptions(mapOf())

        val allDecoders = ExtendedEDNDecoders.arrayDecoders + ExtendedEDNDecoders.listDecoders +
                ExtendedEDNDecoders.prettyDecoders + ExtendedEDNDecoders.base64Decoders

        val defaultOptions: EDNSoapOptions
            get() = EDNSoapOptions()

        private fun extendedReaderOptions(ednClassDecoder: Map<String, (Any?) -> Any?>) =
            EDNSoapOptions(
                allowSchemeUTF32Codes = true,
                allowDispatchChars = true,
                moreNumberPrefixes = true,
                allowNumericSuffixes = true,
                allowMoreEncoderDecoderNames = true,
                encodingSequenceSeparator = ", ",
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
                encodingSequenceSeparator = ", ",
                allowComplexNumberLiterals = true,
                allowUTFSymbols = true,
                ednClassDecoders = mapOf(),
                ednClassEncoders = ednClassEncoders,
            )
    }
}
