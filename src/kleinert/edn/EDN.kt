package kleinert.edn

import kleinert.edn.pprint.EDNSoupWriter
import kleinert.edn.pprint.EdnWriterException
import kleinert.edn.reader.CodePointIterator
import kleinert.edn.reader.EDNSoupReader
import java.io.File
import java.io.InputStream
import java.io.Reader

/**
 * Utilities for parsing and writing EDN format texts.
 *
 * @author Armin Kleinert
 */
object EDN {
    val defaultOptions = EDNSoupOptions.defaultOptions
    val extendedOptions = EDNSoupOptions.extendedOptions

    /**
     * Parse EDN from a string.
     *
     * Possible options:
     *  - [EDNSoupOptions.allowDispatchChars]
     *  - [EDNSoupOptions.allowMoreEncoderDecoderNames]
     *  - [EDNSoupOptions.allowNumericSuffixes]
     *  - [EDNSoupOptions.allowSchemeUTF32Codes]
     *  - [EDNSoupOptions.allowUTFSymbols]
     *  - [EDNSoupOptions.ednClassDecoders]
     *  - [EDNSoupOptions.encoderCollectionElementLimit]
     *  - [EDNSoupOptions.listToPersistentListConverter]
     *  - [EDNSoupOptions.listToPersistentVectorConverter]
     *  - [EDNSoupOptions.mapToPersistentMapConverter]
     *  - [EDNSoupOptions.moreNumberPrefixes]
     *  - [EDNSoupOptions.setToPersistentSetConverter]
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
    fun read(s: String, options: EDNSoupOptions = defaultOptions): Any? {
        val cpi1 = CodePointIterator(s.codePoints())
        return cpi1.use { cpi -> EDNSoupReader.read(cpi, options) }
    }

    /**
     * Parse EDN from a [File]. The file is assumed to exist and be a non-directory.
     */
    fun read(file: File, options: EDNSoupOptions = defaultOptions): Any? {
        val cpi1 = CodePointIterator(file.reader(Charsets.UTF_8))
        return cpi1.use { cpi -> EDNSoupReader.read(cpi, options) }
    }


    fun read(reader: InputStream, options: EDNSoupOptions = defaultOptions): Any? {
        val cpi = CodePointIterator(reader)
        return EDNSoupReader.read(cpi, options)
    }

    fun read(reader: Reader, options: EDNSoupOptions = defaultOptions): Any? {
        val cpi = CodePointIterator(reader)
        return EDNSoupReader.read(cpi, options)
    }

    fun pprint(obj: Any?, file: File, options: EDNSoupOptions = defaultOptions) {
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
     *   [EDNSoupOptions.allowDispatchChars]
     *   [EDNSoupOptions.allowMoreEncoderDecoderNames]
     *   [EDNSoupOptions.allowNumericSuffixes]
     *   [EDNSoupOptions.ednClassEncoders]
     *   [EDNSoupOptions.encoderCollectionElementLimit]
     *   [EDNSoupOptions.encoderLineIndent]
     *   [EDNSoupOptions.encoderMaxColumn]
     *   [EDNSoupOptions.encoderSequenceElementLimit]
     *   [EDNSoupOptions.encodingSequenceSeparator]
     *   [EDNSoupOptions.moreNumberPrefixes]
     *
     * If [writer] is null, uses [System.out].
     */
    fun pprint(obj: Any?, writer: Appendable? = null, options: EDNSoupOptions = defaultOptions) {
        try {
            if (writer == null) EDNSoupWriter.pprint(obj, options, System.out.writer())
            else EDNSoupWriter.pprint(obj, options, writer)
        } catch (ex: Exception) {
            throw EdnWriterException(cause = ex)
        }
    }

    /**
     * Same as [pprint], but appends a linebreak.
     */
    fun pprintln(obj: Any?, writer: Appendable? = null, options: EDNSoupOptions = defaultOptions) {
        try {
            if (writer == null) EDNSoupWriter.pprintln(obj, options, System.out.writer())
            else EDNSoupWriter.pprintln(obj, options, writer)
        } catch (ex: Exception) {
            throw EdnWriterException(cause = ex)
        }
    }

    /**
     * Encodes an object into a pretty string.
     * @see pprint
     */
    fun pprintToString(obj: Any?, options: EDNSoupOptions = defaultOptions): String {
        try {
            return EDNSoupWriter.pprintToString(obj, options)
        } catch (ex: Exception) {
            throw EdnWriterException(cause = ex)
        }
    }
}

//fun main() {
//    val decoders = mapOf("my/range" to { elem: Any? ->
//        require(elem is List<*> && elem.size == 2 && elem[0] is Number && elem[1] is Number)
//        (elem[0] as Number).toLong()..(elem[1] as Number).toLong()
//    })
//    val options = EDN.defaultOptions.copy(ednClassDecoders = decoders)
//    println(EDN.read("#my/range (0 9)", options))
//}
