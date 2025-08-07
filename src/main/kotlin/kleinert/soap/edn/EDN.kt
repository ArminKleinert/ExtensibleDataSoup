package kleinert.soap.edn

import java.io.File
import java.io.InputStream
import java.io.Reader

/**
 * Utilities for parsing and writing EDN format texts.
 *
 * @author Armin Kleinert
 */
object EDN {
    val defaultOptions = EDNSoapOptions.defaultOptions
    val extendedOptions = EDNSoapOptions.extendedOptions

    /**
     * Parse EDN from a string.
     *
     * Possible options:
     *  - [EDNSoapOptions.allowComplexNumberLiterals]
     *  - [EDNSoapOptions.allowDispatchChars]
     *  - [EDNSoapOptions.allowMoreEncoderDecoderNames]
     *  - [EDNSoapOptions.allowNumericSuffixes]
     *  - [EDNSoapOptions.allowSchemeUTF32Codes]
     *  - [EDNSoapOptions.allowUTFSymbols]
     *  - [EDNSoapOptions.ednClassDecoders]
     *  - [EDNSoapOptions.encoderCollectionElementLimit]
     *  - [EDNSoapOptions.listToPersistentListConverter]
     *  - [EDNSoapOptions.listToPersistentVectorConverter]
     *  - [EDNSoapOptions.mapToPersistentMapConverter]
     *  - [EDNSoapOptions.moreNumberPrefixes]
     *  - [EDNSoapOptions.setToPersistentSetConverter]
     *
     * Using tagged objects, users can define their own tagged objects.
     * ```
     *     val decoders = mapOf("my/range" to { elem: Any? ->
     *         require(elem is List<*> && elem.size == 2 && elem[0] is Number && elem[1] is Number)
     *         ((elem[0] as Number).toLong()) .. (elem[1] as Number).toLong())
     *     })
     *     val options = EDN.defaultOptions.copy(ednClassDecoders = decoders)
     *     println(EDN.read("#my/range (0 9)", options)) // Returns the range 0..9
     * ```
     */
    fun read(s: String, options: EDNSoapOptions = defaultOptions): Any? {
        val cpi1 = CodePointIterator(s.codePoints())
        return cpi1.use { cpi -> EDNSoapReader.read(cpi, options) }
    }

    /**
     * Parse EDN from a [File]. The file is assumed to exist and be a non-directory.
     */
    fun read(file: File, options: EDNSoapOptions = defaultOptions): Any? {
        val cpi1 = CodePointIterator(file.reader(Charsets.UTF_8))
        return cpi1.use { cpi -> EDNSoapReader.read(cpi, options) }
    }


    fun read(reader: InputStream, options: EDNSoapOptions = defaultOptions): Any? {
        val cpi = CodePointIterator(reader)
        return EDNSoapReader.read(cpi, options)
    }

    fun read(reader: Reader, options: EDNSoapOptions = defaultOptions): Any? {
        val cpi = CodePointIterator(reader)
        return EDNSoapReader.read(cpi, options)
    }

    fun pprint(obj: Any?, file: File, options: EDNSoapOptions = defaultOptions) {
        return file.writer(Charsets.UTF_8).use {
            try {
                pprint(obj, it, options)
            } catch (ex: Exception) {
                throw EdnWriterException(cause = ex)
            }
        }
    }

    /**
     * Encodes an object into a pretty string and writes it to some writer.
     * For writing to a string, the user can pass a [StringBuilder] or use [pprintToString] instead.
     * The writer is assumed to use UTF-8 encoding.
     *
     * Possible options:
     *   [EDNSoapOptions.allowComplexNumberLiterals]
     *   [EDNSoapOptions.allowDispatchChars]
     *   [EDNSoapOptions.allowMoreEncoderDecoderNames]
     *   [EDNSoapOptions.allowNumericSuffixes]
     *   [EDNSoapOptions.ednClassEncoders]
     *   [EDNSoapOptions.encoderCollectionElementLimit]
     *   [EDNSoapOptions.encoderLineIndent]
     *   [EDNSoapOptions.encoderMaxColumn]
     *   [EDNSoapOptions.encoderSequenceElementLimit]
     *   [EDNSoapOptions.encodingSequenceSeparator]
     *   [EDNSoapOptions.moreNumberPrefixes]
     *
     * If [writer] is null, uses [System.out].
     */
    fun pprint(obj: Any?, writer: Appendable? = null, options: EDNSoapOptions = defaultOptions) {
        try {
            if (writer == null) EDNSoapWriter.pprint(obj, options, System.out.writer())
            else EDNSoapWriter.pprint(obj, options, writer)
        } catch (ex: Exception) {
            throw EdnWriterException(cause = ex)
        }
    }

    /**
     * Same as [pprint], but appends a linebreak.
     */
    fun pprintln(obj: Any?, writer: Appendable? = null, options: EDNSoapOptions = defaultOptions) {
        try {
            if (writer == null) EDNSoapWriter.pprintln(obj, options, System.out.writer())
            else EDNSoapWriter.pprintln(obj, options, writer)
        } catch (ex: Exception) {
            throw EdnWriterException(cause = ex)
        }
    }

    /**
     * Encodes an object into a pretty string.
     * @see pprint
     */
    fun pprintToString(obj: Any?, options: EDNSoapOptions = defaultOptions): String {
        try {
            return EDNSoapWriter.pprintToString(obj, options)
        } catch (ex: Exception) {
            throw EdnWriterException(cause = ex)
        }
    }
}

fun main() {
    val decoders = mapOf("my/range" to { elem: Any? ->
        require(elem is List<*> && elem.size == 2 && elem[0] is Number && elem[1] is Number)
        (elem[0] as Number).toLong()..(elem[1] as Number).toLong()
    })
    val options = EDN.defaultOptions.copy(ednClassDecoders = decoders)
    println(EDN.read("#my/range (0 9)", options))
}
