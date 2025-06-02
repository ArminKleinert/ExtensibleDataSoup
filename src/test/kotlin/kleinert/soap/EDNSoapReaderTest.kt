package kleinert.soap

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigInteger

class EDNSoapReaderTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun readNumberDefaultOptionsTest() {
        soap("0").let { assertEquals(0L, it) }
        soap("00").let { assertEquals(0L, it) }
        soap("+0").let { assertEquals(0L, it) }
        soap("-0").let { assertEquals(0L, it) }

        soap("255").let { assertEquals(255L, it) }
        soap("0377").let { assertEquals(255L, it) }
        soap("0xFF").let { assertEquals(255L, it) }
        soap("0b11111111").let { assertEquals(255L, it) }
        soap("+255").let { assertEquals(255L, it) }
        soap("+0377").let { assertEquals(255L, it) }
        soap("+0xFF").let { assertEquals(255L, it) }
        soap("+0b11111111").let { assertEquals(255L, it) }
        soap("-255").let { assertEquals(-255L, it) }
        soap("-0377").let { assertEquals(-255L, it) }
        soap("-0xFF").let { assertEquals(-255L, it) }
        soap("-0b11111111").let { assertEquals(-255L, it) }

        soap("0N").let { assertEquals(BigInteger.ZERO, it) }
        soap("00N").let { assertEquals(BigInteger.ZERO, it) }
        soap("+0N").let { assertEquals(BigInteger.ZERO, it) }
        soap("-0N").let { assertEquals(BigInteger.ZERO, it) }

        val tff = BigInteger.valueOf(255L)
        soap("255N").let { assertEquals(tff, it) }
        soap("0377N").let { assertEquals(tff, it) }
        soap("0xFFN").let { assertEquals(tff, it) }
        soap("0b11111111N").let { assertEquals(tff, it) }
        soap("+255N").let { assertEquals(tff, it) }
        soap("+0377N").let { assertEquals(tff, it) }
        soap("+0xFFN").let { assertEquals(tff, it) }
        soap("+0b11111111N").let { assertEquals(tff, it) }
        soap("-255N").let { assertEquals(-tff, it) }
        soap("-0377N").let { assertEquals(-tff, it) }
        soap("-0xFFN").let { assertEquals(-tff, it) }
        soap("-0b11111111N").let { assertEquals(-tff, it) }
    }
}
