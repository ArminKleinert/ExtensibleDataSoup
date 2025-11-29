package kleinert.edn.reader

import kleinert.edn.data.Ratio
import java.math.BigDecimal
import java.math.BigInteger

object EDNReaderPredefMacros {
    fun macroMergeMaps(args: Any?) = when (args) {
        is Map<*, *> -> args
        is Iterable<*> -> {
            val res = mutableMapOf<Any?, Any?>()
            for (it in args) {
                require(it is Map<*, *>)
                res += it
            }
            res
        }

        else -> throw IllegalArgumentException()
    }

    fun macroPlusLong(args: Any?) =
        if (args is Iterable<*>) {
            var res = 0L
            for (it in args) res += when (it) {
                is Long -> it
                is Double -> it.toLong()
                is Ratio -> it.toLong()
                is BigInteger -> it.toLong()
                is BigDecimal -> it.toLong()
                is Byte -> it.toLong()
                is Short -> it.toLong()
                is Int -> it.toLong()
                is Float -> it.toLong()
                else -> throw IllegalArgumentException()
            }
            res
        } else {
            args
        }

    fun macroPrint(args: Any?): Any? {
        println(args); return args
    }

    val preDefined: Map<String, (Any?) -> Any?> = mapOf(
        "merge" to ::macroMergeMaps,
        "plusLong" to ::macroPlusLong,
        "printAndGet" to ::macroPrint,
        "toString" to Any?::toString
    )
}