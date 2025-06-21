package kleinert.soap

import java.time.Instant
import java.util.*

class EDNSoapWriter private constructor(private val options: EDNSoapOptions = EDNSoapOptions.extendedOptions) {

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
            is String -> TODO()
            is Char -> TODO()
            is Byte -> TODO()
            is Short -> TODO()
            is Int -> TODO()
            is Long -> TODO()
            is Number -> TODO()
            is ByteArray -> tryEncoder(obj) ?: encode(obj.toList())
            is ShortArray -> tryEncoder(obj) ?: encode(obj.toList())
            is IntArray -> tryEncoder(obj) ?: encode(obj.toList())
            is LongArray -> tryEncoder(obj) ?: encode(obj.toList())
            is FloatArray -> tryEncoder(obj) ?: encode(obj.toList())
            is DoubleArray -> tryEncoder(obj) ?: encode(obj.toList())
            is Array<*> -> tryEncoder(obj) ?: encode(obj.toList())
            is List<*> -> TODO()
            is Set<*> -> TODO()
            is Map<*, *> -> TODO()
            is Iterable<*> -> TODO()
            is UUID -> TODO()
            is Instant -> TODO()
            else -> tryEncoder(obj) ?: obj.toString()
        }
    }
}