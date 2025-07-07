package kleinert.soap.edn

import java.io.File
import java.io.InputStream
import java.io.Reader
import java.io.Writer

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

        fun write(string: String, writer: Writer, options: EDNSoapOptions = defaultOptions) {}
    }
}
