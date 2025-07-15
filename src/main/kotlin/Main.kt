import kleinert.soap.data.Ratio
import kotlin.math.abs


fun main(args: Array<String>) {
//    println(EDN.read("symbol")) // Symbol without namespace
//    println(EDN.read("namespace/symbol")) // Symbol
//    println(EDN.read(":keyword")) // Keyword without namespace
//    println(EDN.read(":namespace/symbol")) // Keyword
//    println(EDN.read("\"string\"")) // String
//    println(EDN.read("\\c")) // Character
//
//    println(EDN.read("(list elements)")) // List
//    println(EDN.read("[vector elements]")) // Vector
//    println(EDN.read("#{set elements}")) // Set
//    println(EDN.read("{map-key map-value}")) // Map
//
//    println(EDN.read("0xC0FFEE")) // Long
//    println(EDN.read("12648430")) // The same Long
//    println(EDN.read("12648430N")) // The same as BigInt
//    println(EDN.read("5.0")) // Double
//    println(EDN.read("5")) // BigDecimal
//    println(EDN.read("5.0M")) // BigDecimal
//    println(EDN.read("5/6")) // Ratio
//    println(EDN.read("5+7i", EDNSoapOptions(allowComplexNumberLiterals = true))) // Complex
//
//    println(EDN.read("nil")) // Null
//    println(EDN.read("false")) // False
//    println(EDN.read("true")) // True
//
//    println(EDN.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")) // UUID
//    println(EDN.read("#inst \"1985-04-12T23:20:50.52Z\"")) // Instant
//
//    println(EDN.read("##INF")) // Infinity
//    println(EDN.read("##-INF")) // Negative infinity
//    println(EDN.read("##NaN")) // NaN
//    println(EDN.read("##-NaN")) // The cooler NaN
//
//    data class Dice(val sides: Int)
//
//    val anyToDice = { it: Any? ->
//        if (it is List<*> && it.size == 1 && it[0] is Number) Dice((it[0] as Number).toInt())
//        else null
//    }
//
//    println(EDN.read("#my/dice [6]", EDN.defaultOptions.copy(ednClassDecoders = mapOf("my/dice" to anyToDice))))
//
//    val opts =
//        EDN.defaultOptions.copy(allowMoreEncoderDecoderNames = true, ednClassDecoders = mapOf("dice" to anyToDice))
//    println(EDN.read("#dice [6]", opts))

    println(Ratio.estimate(1.5))
    println(Ratio.estimate(0.5))
    println(Ratio.estimate(0.0))
    println(Ratio.estimate(1.0))
    println(Ratio.estimate(3.1415))
    println(Ratio.estimate(0.31415))
}
