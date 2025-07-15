package kleinert.soap.edn

import kleinert.soap.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderCharTest {
    @Test
    fun parseSpecialCharsTest() {
        Assertions.assertEquals('\n', EDN.read("\\newline"))
        Assertions.assertEquals(' ', EDN.read("\\space"))
        Assertions.assertEquals('\t', EDN.read("\\tab"))
        Assertions.assertEquals('\b', EDN.read("\\backspace"))
        Assertions.assertEquals(12.toChar(), EDN.read("\\formfeed"))
        Assertions.assertEquals('\r', EDN.read("\\return"))
    }

    @Test
    fun parseSimpleCharsTest() {
        for (chr in 'a'..'z')
            Assertions.assertEquals(chr, EDN.read("\\$chr"))
        for (chr in 'A'..'Z')
            Assertions.assertEquals(chr, EDN.read("\\$chr"))
        for (chr in '0'..'9')
            Assertions.assertEquals(chr, EDN.read("\\$chr"))
    }

    @Test
    fun parseSymbolCharsTest() {
        for (chr in "!\"§$%&/()=?*+~^°'#-_.:,<>|€@`´λ")
            Assertions.assertEquals(chr, EDN.read("\\$chr"))

    }

    @Test
    fun parseOctCharsTest() {
        Assertions.assertEquals('!', EDN.read("\\o41"))
        Assertions.assertEquals('!', EDN.read("\\o041"))
        Assertions.assertEquals('ǂ', EDN.read("\\o702"))
    }

    @Test
    fun parseUniCharsTest() {
        Assertions.assertEquals('λ', EDN.read("\\u03BB"))
        Assertions.assertEquals('λ', EDN.read("\\u03bb"))
        Assertions.assertEquals('ῷ', EDN.read("\\u1ff7"))
        Assertions.assertEquals('\u8183', EDN.read("\\u8183"))

        Assertions.assertEquals('\u2626', EDN.read("\\u2626")) // Orthodox cross
        Assertions.assertEquals('\u271D', EDN.read("\\u271D")) // Latin cross

        Assertions.assertEquals("\uD83D\uDD46", EDN.read("#\\u0001F546", options = EDN.extendedOptions)) // White latin cross
        Assertions.assertEquals("\uD83D\uDD47", EDN.read("#\\u0001F547", options = EDN.extendedOptions)) // Heavy latin cross
    }
}
