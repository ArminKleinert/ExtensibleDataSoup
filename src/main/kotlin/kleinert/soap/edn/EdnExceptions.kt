package kleinert.soap.edn


open class EdnReaderException(
    text: String, cause: Throwable? = null,
    val lineIndex: Int = -1, val textIndex: Int = -1
) : Exception(text, cause) {
    class EdnClassConversionError : Exception {
        constructor(text: String) : super(text)
        constructor(ex: Exception) : super(ex)
    }

    class EdnEmptyInputException : Exception {
        constructor(text: String) : super(text)
        constructor(ex: Exception) : super(ex)
    }
}

class EdnWriterException(text: String) : Exception(text)
