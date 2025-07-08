package kleinert.soap.edn

import java.io.File
import java.io.InputStream
import java.io.Reader

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

        fun pprint(string: Any?, writer: Appendable? = null, options: EDNSoapOptions = defaultOptions) {
            if (writer == null) EDNSoapWriter.pprint(string, options, System.out.writer())
            else EDNSoapWriter.pprint(string, options, writer)
        }

        fun pprintln(string: Any?, writer: Appendable? = null, options: EDNSoapOptions = defaultOptions) {
            if (writer == null) EDNSoapWriter.pprintln(string, options, System.out.writer())
            else EDNSoapWriter.pprintln(string, options, writer)
        }

        fun pprintToString(string: Any?, options: EDNSoapOptions = defaultOptions): String {
            return EDNSoapWriter.pprintToString(string, options)
        }
    }
}
