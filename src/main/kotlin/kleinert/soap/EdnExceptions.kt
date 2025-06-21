package kleinert.soap


open class EdnReaderException(text: String, cause: Throwable? = null) : Exception(text, cause) {
    class EdnClassConversionError(text: String) : Exception(text)
    class EdnEmptyInputException(text: String) : Exception(text)
}

class EdnWriterException(text: String) : Exception(text)
