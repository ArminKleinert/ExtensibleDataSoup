package kleinert.soap.edn

import java.io.File
import java.io.Reader
import java.io.Writer

class EDN {
    companion object {
        val defaultOptions = EDNSoapOptions.defaultOptions
        val extendedOptions = EDNSoapOptions.extendedOptions

        fun read(s: String, options: EDNSoapOptions = defaultOptions) =
            EDNSoapReader.readString(s, options)

        fun read(file: File, options: EDNSoapOptions = defaultOptions) =
            EDNSoapReader.readFile(file, options)

        fun read(reader: Reader, options: EDNSoapOptions = defaultOptions) =
            EDNSoapReader.readReader(reader, options)

        fun write(string:String, writer: Writer, options: EDNSoapOptions = defaultOptions){}
    }
}
