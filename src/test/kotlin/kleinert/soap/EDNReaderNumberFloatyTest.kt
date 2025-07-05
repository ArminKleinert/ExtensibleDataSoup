package kleinert.soap

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions

class EDNReaderNumberFloatyTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseDouble() {
        soap("0.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("+0.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("-0.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        soap("0.5").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("+0.5").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("-0.5").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }

        soap("1.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(1.0, it)
        }
        soap("+1.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(1.0, it)
        }
        soap("-1.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-1.0, it)
        }

        soap("128.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(128.0, it)
        }
        soap("+128.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(128.0, it)
        }
        soap("-128.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-128.0, it)
        }

        soap("255.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.0, it)
        }
        soap("+255.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.0, it)
        }
        soap("-255.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-255.0, it)
        }

        soap("255.25").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.25, it)
        }
        soap("+255.25").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.25, it)
        }
        soap("-255.25").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-255.25, it)
        }
    }

    @Test
    fun parseDoubleENotation() {
        soap("0e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("-0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        soap("-0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        soap("5e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        soap("5e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        soap("5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(50.0, it)
        }
        soap("5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("-5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-50.0, it)
        }
        soap("-5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }
    }

    @Test
    fun parseDoubleENotationBig() {
        soap("0E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("-0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        soap("-0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        soap("5E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        soap("5E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        soap("5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(50.0, it)
        }
        soap("5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("-5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-50.0, it)
        }
        soap("-5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }
    }

    @Test
    fun parseDoubleENotation2() {
        soap("0.0e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0.0e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0.0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0.0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("-0.0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        soap("-0.0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        soap("0.5e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("0.5e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("0.5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        soap("0.5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.05, it)
        }
        soap("-0.5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-5.0, it)
        }
        soap("-0.5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.05, it)
        }
    }

    @Test
    fun parseDoubleENotationBig2() {
        soap("0.0E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0.0E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0.0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("0.0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        soap("-0.0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        soap("-0.0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        soap("0.5E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("0.5E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        soap("0.5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        soap("0.5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.05, it)
        }
        soap("-0.5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-5.0, it)
        }
        soap("-0.5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.05, it)
        }
    }

    @Test
    fun parseBigDecimal() {
        var temp = BigDecimal.valueOf(0.0)
        soap("0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(0), it)
        }
        soap("0.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("+0.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("-0.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(0.5)
        soap("0.5M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("+0.5M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("-0.5M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(1.0)
        soap("1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(1L), it)
        }
        soap("-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(1L).negate(), it)
        }
        soap("1.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("+1.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("-1.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(128.0)
        soap("128M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(128L), it)
        }
        soap("-128M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(128L).negate(), it)
        }
        soap("128.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("+128.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("-128.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(255.0)
        soap("255M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(255), it)
        }
        soap("-255M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(255).negate(), it)
        }
        soap("255.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("+255.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("-255.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(255.25)
        soap("255.25M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("+255.25M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        soap("-255.25M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }
    }

    @Test
    fun parseBigDecimalENotation() {
        soap("0.0e+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+0"), it)
        }
        soap("0.0e-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-0"), it)
        }
        soap("0.0e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+1"), it)
        }
        soap("0.0e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-1"), it)
        }
        soap("-0.0e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e+1"), it)
        }
        soap("-0.0e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e-1"), it)
        }

        soap("0.5e+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+0"), it)
        }
        soap("0.5e-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-0"), it)
        }
        soap("0.5e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+1"), it)
        }
        soap("0.5e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-1"), it)
        }
        soap("-0.5e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e+1"), it)
        }
        soap("-0.5e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e-1"), it)
        }
    }

    @Test
    fun parseBigDecimalENotationBig2() {
        soap("0.0E+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+0"), it)
        }
        soap("0.0E-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-0"), it)
        }
        soap("0.0E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+1"), it)
        }
        soap("0.0E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-1"), it)
        }
        soap("-0.0E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e+1"), it)
        }
        soap("-0.0E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e-1"), it)
        }

        soap("0.5E+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+0"), it)
        }
        soap("0.5E-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-0"), it)
        }
        soap("0.5E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+1"), it)
        }
        soap("0.5E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-1"), it)
        }
        soap("-0.5E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e+1"), it)
        }
        soap("-0.5E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e-1"), it)
        }
    }
}
