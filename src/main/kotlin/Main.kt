import kleinert.soap.EDNSoapOptions
import kleinert.soap.EDNSoapReader

fun examples1() {
    fun testFunDefault(s:String) {
        val data = EDNSoapReader.readString(s)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    fun testFunExtend(s:String) {
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    testFunDefault("\\a") // "a Class Char"

    // Unicode sequences in strings work.
    testFunDefault("\"\\uD83C\\uDF81\"") // "🎁" class java.lang.String
    testFunExtend ("\"\\x0001F381\"") // "🎁" class java.lang.String

    // UTF-32 evaluates to strings.
    testFunDefault("\\u0660") // "٠" class java.lang.Character
    testFunExtend ("#\\u0660") // "٠" class java.lang.String
    testFunExtend ("#\\u0001F381") // "🎁" class java.lang.String

    // Discard is right-associative.
    testFunDefault("#_ #_ \"1\" \\a 1") // "1" class java.lang.Long
    testFunDefault("#_#_\"1\" \\a 1") // "1" class java.lang.Long

    // Comments are ignored
    testFunDefault(";bc\n123") // "123" class java.lang.Long
    testFunDefault(";bc\n") // "123" class java.lang.Long

    testFunDefault("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"") // "f81d4fae-7dec-11d0-a765-00a0c91e6bf6" class java.util.UUID

    testFunDefault("#inst \"1985-04-12T23:20:50.52Z\"") // "1985-04-12T23:20:50.520Z" class java.time.Instant

    testFunDefault(",")
}

fun examples2() {
    fun testFunDefault(s:String, decoders: Map<String, (Any?) -> Any?>) {
        val options = EDNSoapOptions.defaultOptions.copy(ednClassDecoders = decoders)
        val data = EDNSoapReader.readString(s, options)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    fun testFunExtend(s:String, decoders: Map<String, (Any?) -> Any?>) {
        val options = EDNSoapOptions.defaultOptions.copy(allowNonMapDecode = true, ednClassDecoders = decoders)
        val data = EDNSoapReader.readString(s, options)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }

    testFunDefault(
        "#pair {\"first\" 4 \"second\" 5}",
        mapOf("pair" to { elem: Any? -> elem as Map<*,*>; (elem["first"] to elem["second"]) })
    )

    testFunExtend(
        "#pair [4 5]",
        mapOf("pair" to { elem: Any? -> elem as List<*>; (elem[0] to elem[1]) })
    )
}

fun main(args: Array<String>) {
    examples1()
    examples2()
}
