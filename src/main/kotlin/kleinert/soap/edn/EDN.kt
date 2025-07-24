package kleinert.soap.edn

import java.io.File
import java.io.InputStream
import java.io.Reader

/**
 * TODO
 *
 * @author Armin Kleinert
 */
class EDN {
    companion object {
        val defaultOptions = EDNSoapOptions.defaultOptions
        val extendedOptions = EDNSoapOptions.extendedOptions

        fun read(s: String, options: EDNSoapOptions = defaultOptions): Any? {
            val cpi1 = CodePointIterator(s.codePoints())
            return cpi1.use { cpi -> EDNSoapReader.read(cpi, options) }
        }

        fun read(file: File, options: EDNSoapOptions = defaultOptions): Any? {
            val cpi1 = CodePointIterator(file.reader(Charsets.UTF_8))
            return cpi1.use { cpi -> EDNSoapReader.read(cpi, options) }
        }

        fun read(reader: InputStream, options: EDNSoapOptions = defaultOptions): Any? {
            val cpi1 = CodePointIterator(reader)
            return cpi1.use { cpi -> EDNSoapReader.read(cpi, options) }
        }

        fun read(reader: Reader, options: EDNSoapOptions = defaultOptions): Any? {
            val cpi1 = CodePointIterator(reader)
            return cpi1.use { cpi -> EDNSoapReader.read(cpi, options) }
        }

        fun pprint(obj: Any?, file: File, options: EDNSoapOptions = defaultOptions) =
            file.writer(Charsets.UTF_8).use {
                try {
                    pprint(obj, it, options)
                } catch (ex: Exception) {
                    throw EdnWriterException(cause = ex)
                }
            }

        fun pprint(obj: Any?, writer: Appendable? = null, options: EDNSoapOptions = defaultOptions) {
            try {
                if (writer == null) EDNSoapWriter.pprint(obj, options, System.out.writer())
                else EDNSoapWriter.pprint(obj, options, writer)
            } catch (ex: Exception) {
                throw EdnWriterException(cause = ex)
            }
        }

        fun pprintln(obj: Any?, writer: Appendable? = null, options: EDNSoapOptions = defaultOptions) {
            try {
                if (writer == null) EDNSoapWriter.pprintln(obj, options, System.out.writer())
                else EDNSoapWriter.pprintln(obj, options, writer)
            } catch (ex: Exception) {
                throw EdnWriterException(cause = ex)
            }
        }

        fun pprintToString(obj: Any?, options: EDNSoapOptions = defaultOptions): String {
            try {
                return EDNSoapWriter.pprintToString(obj, options)
            } catch (ex: Exception) {
                throw EdnWriterException(cause = ex)
            }
        }
    }
}
