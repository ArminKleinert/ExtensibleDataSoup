//package kleinert.soap
//
//fun tokenize(cpi: CodePointIterator): MutableList<String> {
//    /*
//    [\s,]*
//    \\u\d{4}
//    \\p\(
//    \\.
//    [()\[\]{}]
//    "(?:\\.|[^\\"])*"?
//    ;.*
//    ~@
//    ~
//    @
//    #\{
//    `
//    #\(|[^\s\[\]{}('"`,;)]*'?)
//    */
//    val tokens = mutableListOf<String>()
//    val strBuilder = StringBuilder()
//
//    do {
//        if (cpi.isFinished())
//            break
//        val pt = cpi.nextInt()
//
//        when (pt) {
//            ' '.code, '\n'.code, '\r'.code, '\t'.code, ','.code ->
//                null
//
//            '('.code, ')'.code, '['.code, ']'.code, '{'.code, '}'.code, '\''.code, '`'.code ->
//                tokens.add(pt.toChar().toString())
//
//            '\\'.code -> {
//
//            }
//        }
//    } while (true)
//
//    return tokens
//}
//
//fun tokenizeChar(cpi: CodePointIterator): Int {
//    var pt = cpi.nextInt()
//    when (pt) {
//        ' '.code, '\t'.code, '\n'.code, '\r'.code ->
//            throw IllegalArgumentException()
//
//        '['.code, ']'.code, '('.code, ')'.code, '{'.code, '}'.code, '\''.code, '"'.code, ';'.code, ','.code ->
//            return pt
//    }
//
//    val token = takeToken(cpi, StringBuilder().appendCodePoint(pt))
//
//    require(token.isNotEmpty())
//
//    if (token.length == 1) return token[0] .code
//
//    when (token) {
//        "newline" -> return '\n'.code
//        "space" -> return ' '.code
//        "tab" -> return '\t'.code
//        "backspace" -> return '\b'.code
//        "formfeed" -> return 12 // '\f'
//        "return" -> return '\r'.code
//    }
//
//    val base: Int
//    val length: Int
//    when (token[0]) {
//        'o' -> {
//            base = 8
//            if (token.length != 4)
//                throw IllegalArgumentException("Illegal unicode literal $token.")
//        }
//            'u' -> {
//                base = 16
//                length = 8
//            }
//                'x' -> {
//                    base = 16
//                    length = 8
//                }
//                else -> throw IllegalArgumentException() // Length is >1, it is not a special char form and is not a unicode codepoint
//    }
//
//    return token.substring(1).toInt(base)
//}
//
//private fun parseUnicodeChar(token: CharSequence, base: Int, initChar: Char): Int {
//    try {
//        var code = 0
//        for (it in token) code = code * base + it.digitToInt(base)
//        return code
//    } catch (nfe: NumberFormatException) {
//        throw EdnReaderException(nfe.message ?: "Invalid unicode sequence \\$initChar$token")
//    }
//}
//
//fun takeToken(cpi: CodePointIterator, strBuilder: StringBuilder): String {
//    // ' ', '\t', '\n', '\r', '[', ']', '(', ')', '{', '}', '\'', '"', ';', ','
//    do {
//        if (!cpi.hasNext())
//            break
//        when (val pt = cpi.nextInt()) {
//            ' '.code, '\t'.code, '\n'.code, '\r'.code, '['.code, ']'.code, '('.code, ')'.code, '{'.code, '}'.code, '\''.code, '"'.code, ';'.code, ','.code ->
//                break
//
//            else -> strBuilder.appendCodePoint(pt)
//        }
//    } while (true)
//    return strBuilder.toString()
//}
