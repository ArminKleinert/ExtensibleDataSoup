package kleinert.soap.edn

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EDNReaderNumberFloatyTest {
    @Test
    fun parseDouble() {
        EDN.read("0.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("+0.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("-0.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        EDN.read("0.5").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("+0.5").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("-0.5").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }

        EDN.read("1.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(1.0, it)
        }
        EDN.read("+1.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(1.0, it)
        }
        EDN.read("-1.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-1.0, it)
        }

        EDN.read("128.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(128.0, it)
        }
        EDN.read("+128.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(128.0, it)
        }
        EDN.read("-128.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-128.0, it)
        }

        EDN.read("255.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.0, it)
        }
        EDN.read("+255.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.0, it)
        }
        EDN.read("-255.0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-255.0, it)
        }

        EDN.read("255.25").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.25, it)
        }
        EDN.read("+255.25").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.25, it)
        }
        EDN.read("-255.25").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-255.25, it)
        }
    }

    @Test
    fun parseDoubleENotation() {
        EDN.read("0e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("-0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        EDN.read("-0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        EDN.read("5e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        EDN.read("5e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        EDN.read("5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(50.0, it)
        }
        EDN.read("5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("-5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-50.0, it)
        }
        EDN.read("-5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }
    }

    @Test
    fun parseDoubleENotationBig() {
        EDN.read("0E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("-0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        EDN.read("-0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        EDN.read("5E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        EDN.read("5E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        EDN.read("5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(50.0, it)
        }
        EDN.read("5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("-5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-50.0, it)
        }
        EDN.read("-5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }
    }

    @Test
    fun parseDoubleENotation2() {
        EDN.read("0.0e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0.0e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0.0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0.0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("-0.0e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        EDN.read("-0.0e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        EDN.read("0.5e+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("0.5e-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("0.5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        EDN.read("0.5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.05, it)
        }
        EDN.read("-0.5e+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-5.0, it)
        }
        EDN.read("-0.5e-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.05, it)
        }
    }

    @Test
    fun parseDoubleENotationBig2() {
        EDN.read("0.0E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0.0E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0.0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("0.0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        EDN.read("-0.0E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        EDN.read("-0.0E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        EDN.read("0.5E+0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("0.5E-0").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        EDN.read("0.5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        EDN.read("0.5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.05, it)
        }
        EDN.read("-0.5E+1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-5.0, it)
        }
        EDN.read("-0.5E-1").let {
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.05, it)
        }
    }

    @Test
    fun parseBigDecimal() {
        var temp = BigDecimal.valueOf(0.0)
        EDN.read("0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(0), it)
        }
        EDN.read("0.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("+0.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("-0.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(0.5)
        EDN.read("0.5M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("+0.5M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("-0.5M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(1.0)
        EDN.read("1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(1L), it)
        }
        EDN.read("-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(1L).negate(), it)
        }
        EDN.read("1.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("+1.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("-1.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(128.0)
        EDN.read("128M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(128L), it)
        }
        EDN.read("-128M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(128L).negate(), it)
        }
        EDN.read("128.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("+128.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("-128.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(255.0)
        EDN.read("255M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(255), it)
        }
        EDN.read("-255M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal.valueOf(255).negate(), it)
        }
        EDN.read("255.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("+255.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("-255.0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(255.25)
        EDN.read("255.25M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("+255.25M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp, it)
        }
        EDN.read("-255.25M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(temp.negate(), it)
        }
    }

    @Test
    fun parseBigDecimalENotation() {
        EDN.read("0.0e+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+0"), it)
        }
        EDN.read("0.0e-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-0"), it)
        }
        EDN.read("0.0e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+1"), it)
        }
        EDN.read("0.0e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-1"), it)
        }
        EDN.read("-0.0e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e+1"), it)
        }
        EDN.read("-0.0e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e-1"), it)
        }

        EDN.read("0.5e+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+0"), it)
        }
        EDN.read("0.5e-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-0"), it)
        }
        EDN.read("0.5e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+1"), it)
        }
        EDN.read("0.5e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-1"), it)
        }
        EDN.read("-0.5e+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e+1"), it)
        }
        EDN.read("-0.5e-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e-1"), it)
        }
    }

    @Test
    fun parseBigDecimalENotationBig2() {
        EDN.read("0.0E+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+0"), it)
        }
        EDN.read("0.0E-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-0"), it)
        }
        EDN.read("0.0E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e+1"), it)
        }
        EDN.read("0.0E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.0e-1"), it)
        }
        EDN.read("-0.0E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e+1"), it)
        }
        EDN.read("-0.0E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.0e-1"), it)
        }

        EDN.read("0.5E+0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+0"), it)
        }
        EDN.read("0.5E-0M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-0"), it)
        }
        EDN.read("0.5E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e+1"), it)
        }
        EDN.read("0.5E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("0.5e-1"), it)
        }
        EDN.read("-0.5E+1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e+1"), it)
        }
        EDN.read("-0.5E-1M").let {
            Assertions.assertInstanceOf(BigDecimal::class.java, it)
            Assertions.assertEquals(BigDecimal("-0.5e-1"), it)
        }
    }
}
