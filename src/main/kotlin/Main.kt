import java.math.BigInteger



fun main(args: Array<String>) {
//    run {
//        val s = """
//            {:a :b}
//        """.trimIndent()
//        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
//        println(data)
//    }
//
//    run {
//        val s = """
//            :a
//        """.trimIndent()
//        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
//        println(data)
//    }
//
//    run {
//        val s = """
//            [#'n :a #'n :abc]
//        """.trimIndent()
//        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedReaderOptions(
//            mapOf("n" to {o -> (o as Keyword).name().length})
//        ))
//        println(data)
//    }
//
//    run {
//        val s = """
//            [#'f #'n :abc]
//        """.trimIndent()
//        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedReaderOptions(
//            mapOf("n" to {o -> (o as Keyword).name().length}, "f" to {o -> (o as Int)+1})
//        ))
//        println(data)
//    }
//
//    run {
//        data class EDNTestClass(val bbb: Int)
//
//        val s = listOf(EDNTestClass(4))
//        val encoders: Map<Class<*>?, (Any?) -> Pair<String, Any?>?> = mapOf(
//            EDNTestClass::class.java to { o:Any? -> o as EDNTestClass
//                                        "test" to o.bbb*3},
//        )
//        val encoded = EDNSoapWriter(EDNSoapOptions.extendedWriterOptions(encoders)).encode(s)
//        println(encoded)
//        val data = EDNSoapReader.readString(encoded, EDNSoapOptions.extendedReaderOptions(
//            mapOf("test" to {o -> EDNTestClass((o as Int) / 3)})
//        ))
//        println(data)
//    }
//
//    run {
//        val s = """
//            \u03A9
//        """.trimIndent()
//        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedReaderOptions(
//            mapOf("n" to {o -> (o as Keyword).name().length})
//        ))
//        println(data)
//    }

//    run {
//        val s = """
//            [123 +123 -123 123i +123i -123i]
//        """.trimIndent()
//        val data = EDNSoapReader.readString(s)
//        println(data)
//    }
//
//    run {
//        println(BigInteger.valueOf(256).toByte())
//    }

//    run {
//        val s = "[-123 -123M -123L -123i -123S -123B -123D -123F -.123 -.123M -123.123]"
//        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions.copy(intPostfix = 'i'))
//        data as List<*>
//        println(data.map { it to it?.javaClass }.joinToString("\n"))
//    }

//    run {
//        val s = "2.2E-2"
//        val data = EdnReader.readString(s)
//        println(""+data + " " + (data?.javaClass?: "null"))
//    }

    run {
        val s = "\"\uD83C\uDF81\""
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "\"\\uD83C\\uDF81\""
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "\"\\x0001F381\""
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "\\u0660"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "#\\x0001F381"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "#\\u0001F381"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = ";bc\n#\\u0001F381"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "[]"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "[\"abc\"\"abc\"]"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "[\"abc\" \"abc\" \"b\" \"c\"]"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "{\"abc\" \"abc\" \"b\" \"c\"}"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
    run {
        val s = "#{\"dbc\" \"abc\" \"b\" \"c\"}"
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\""+data+"\" " + (data?.javaClass ?: "null"))
    }
}
