package kleinert.soap


open class EdnReaderException(text: String, cause: Throwable? = null) : Exception(text, cause) {
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
