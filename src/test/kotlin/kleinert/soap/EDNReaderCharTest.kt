package kleinert.soap

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderCharTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseSpecialCharsTest() {
        Assertions.assertEquals('\n', soap("\\newline"))
        Assertions.assertEquals(' ', soap("\\space"))
        Assertions.assertEquals('\t', soap("\\tab"))
        Assertions.assertEquals('\b', soap("\\backspace"))
        Assertions.assertEquals(12.toChar(), soap("\\formfeed"))
        Assertions.assertEquals('\r', soap("\\return"))
    }

    @Test
    fun parseSimpleCharsTest() {
        for (chr in 'a'..'z')
            Assertions.assertEquals(chr, soap("\\$chr"))
        for (chr in 'A'..'Z')
            Assertions.assertEquals(chr, soap("\\$chr"))
        for (chr in '0'..'9')
            Assertions.assertEquals(chr, soap("\\$chr"))
    }

    @Test
    fun parseSymbolCharsTest() {
        for (chr in "!\"§$%&/()=?*+~^°'#-_.:,<>|€@`´λ")
            Assertions.assertEquals(chr, soap("\\$chr"))

    }

    @Test
    fun parseOctCharsTest() {
        Assertions.assertEquals('!', soap("\\o41"))
        Assertions.assertEquals('!', soap("\\o041"))
        Assertions.assertEquals('ǂ', soap("\\o702"))
    }

    @Test
    fun parseUniCharsTest() {
        Assertions.assertEquals('λ', soap("\\u03BB"))
        Assertions.assertEquals('λ', soap("\\u03bb"))
        Assertions.assertEquals('ῷ', soap("\\u1ff7"))
        Assertions.assertEquals('\u8183', soap("\\u8183"))

        Assertions.assertEquals('\u2626', soap("\\u2626")) // Orthodox cross
        Assertions.assertEquals('\u271D', soap("\\u271D")) // Latin cross
        Assertions.assertEquals("\uD83D\uDD46", soapE("#\\u0001F546")) // White latin cross
        Assertions.assertEquals("\uD83D\uDD47", soapE("#\\u0001F547")) // Heavy latin cross
    }
}
