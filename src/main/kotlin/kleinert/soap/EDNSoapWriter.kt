package kleinert.soap

import kleinert.soap.cons.Cons
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

class EDNSoapWriter private constructor(private val options: EDNSoapOptions = EDNSoapOptions.extendedOptions) {

    companion object {
        fun pprintS(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.defaultOptions) = EDNSoapWriter(options).encode(obj)
        fun pprint(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.defaultOptions) = print(EDNSoapWriter(options).encode(obj))
        fun pprintln(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.defaultOptions) = println(EDNSoapWriter(options).encode(obj))

        fun pprintSE(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.extendedOptions) = pprintS(obj, options)
        fun pprintE(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.extendedOptions) = pprint(obj, options)
        fun pprintlnE(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.extendedOptions) = pprintln(obj, options)

        fun encode(obj: Any?, options: EDNSoapOptions = EDNSoapOptions.extendedOptions) = pprintS(obj, options)
    }

    private fun tryEncoder(obj:Any): String? {
        val encoder = options.ednClassEncoders[obj.javaClass] ?: return null
        val (prefix, output) = encoder(obj as Any?) ?: return null
        return "#$prefix ${encode(output)}"
    }

    fun encode(obj: Any?): String {
        return when (obj) {
            null -> "nil"
            true -> "true"
            false -> "false"
            is String -> encodeString(obj)
            is Char -> encodeChar(obj)
            is Byte, is Short, is Int, is Long, is Float, is Double, is Ratio -> encodePredefinedNumberType(obj as Number)
            is BigInteger, is BigDecimal -> encodePredefinedNumberType(obj as Number)
            is Map.Entry<*, *> -> "${encode(obj.key)} ${encode(obj.value)}"
            is Cons<*> -> tryEncoder(obj) ?: encodeOtherIterable(obj)
            is ByteArray -> tryEncoder(obj) ?: encode(obj.toList())
            is ShortArray -> tryEncoder(obj) ?: encode(obj.toList())
            is IntArray -> tryEncoder(obj) ?: encode(obj.toList())
            is LongArray -> tryEncoder(obj) ?: encode(obj.toList())
            is FloatArray -> tryEncoder(obj) ?: encode(obj.toList())
            is DoubleArray -> tryEncoder(obj) ?: encode(obj.toList())
            is Array<*> -> tryEncoder(obj) ?: encode(obj.toList())
            is List<*> -> tryEncoder(obj) ?: encodeList(obj)
            is Set<*> -> tryEncoder(obj) ?: encodeSet(obj)
            is Map<*, *> -> tryEncoder(obj) ?: encodeMap(obj)
            is Iterable<*> -> tryEncoder(obj) ?: encodeOtherIterable(obj)
            is Sequence<*> -> tryEncoder(obj) ?: encodeOtherIterable(obj.toList())
            is UUID -> "#uuid $obj"
            is Instant -> "#inst $obj"
            else -> tryEncoder(obj) ?: obj.toString()
        }
    }

    private fun encodePredefinedNumberType(obj: Number): String {
        val temp = StringBuilder()
        when (obj) {
            is Byte, is Short, is Int, is Long, is Float, is Double, is Ratio -> temp.append(obj)
            is BigInteger ->  temp.append(obj).append('N')
            is BigDecimal ->  temp.append(obj).append('M')
            else ->  temp.append(obj)
        }
        if (options.allowNumericSuffixes) {
            when (obj) {
                is Byte -> temp.append("_i8")
                is Short -> temp.append("_i16")
                is Int -> temp.append("_i32")
            }
        }
        return temp.toString()
 }

    fun encodeString(obj:String) = StringBuilder().append('"').append(obj).append('"').toString()

    fun encodeChar(obj:Char) = StringBuilder().append('\\').append(obj).toString()

    fun encodeIterable(obj: Iterable<*>, start: String, end: Char) =
        StringBuilder().append(start).append(obj.joinToString(options.decodingSequenceSeparator) { encode(it) }).append(end).toString()

    fun encodeOtherIterable(obj: Iterable<*>) = encodeIterable(obj, "(", ')')

    fun encodeList(obj: List<*>) = encodeIterable(obj, "[", ']')

    fun encodeSet(obj: Set<*>) = encodeIterable(obj, "#{", '}')

    fun encodeMap(obj: Map<*,*>) = encodeIterable(obj.map { it }, "{", '}')
}