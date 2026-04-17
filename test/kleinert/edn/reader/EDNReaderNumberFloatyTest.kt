package kleinert.edn.reader

import kleinert.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EDNReaderNumberFloatyTest {
    @Test
    fun parseDouble() {
        run {
            val it = EDN.read("0.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("+0.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("-0.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        run {
            val it = EDN.read("0.5")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("+0.5")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("-0.5")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }

        run {
            val it = EDN.read("1.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(1.0, it)
        }
        run {
            val it = EDN.read("+1.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(1.0, it)
        }
        run {
            val it = EDN.read("-1.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-1.0, it)
        }

        run {
            val it = EDN.read("128.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(128.0, it)
        }
        run {
            val it = EDN.read("+128.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(128.0, it)
        }
        run {
            val it = EDN.read("-128.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-128.0, it)
        }

        run {
            val it = EDN.read("255.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.0, it)
        }
        run {
            val it = EDN.read("+255.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.0, it)
        }
        run {
            val it = EDN.read("-255.0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-255.0, it)
        }

        run {
            val it = EDN.read("255.25")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.25, it)
        }
        run {
            val it = EDN.read("+255.25")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(255.25, it)
        }
        run {
            val it = EDN.read("-255.25")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-255.25, it)
        }
    }

    @Test
    fun parseDoubleENotation() {
        run {
            val it = EDN.read("0e+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0e-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("-0e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        run {
            val it = EDN.read("-0e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        run {
            val it = EDN.read("5e+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("5e-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("5e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(50.0, it)
        }
        run {
            val it = EDN.read("+5e+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("+5e-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("+5e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(50.0, it)
        }
        run {
            val it = EDN.read("5e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("-5e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-50.0, it)
        }
        run {
            val it = EDN.read("-5e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }
    }

    @Test
    fun parseDoubleENotationBig() {
        run {
            val it = EDN.read("0E+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0E-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("-0E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        run {
            val it = EDN.read("-0E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        run {
            val it = EDN.read("5E+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("5E-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("5E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(50.0, it)
        }
        run {
            val it = EDN.read("5E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("-5E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-50.0, it)
        }
        run {
            val it = EDN.read("-5E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.5, it)
        }
    }

    @Test
    fun parseDoubleENotation2() {
        run {
            val it = EDN.read("0.0e+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0.0e-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0.0e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0.0e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("-0.0e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        run {
            val it = EDN.read("-0.0e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        run {
            val it = EDN.read("0.5e+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("0.5e-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("0.5e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("0.5e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.05, it)
        }
        run {
            val it = EDN.read("-0.5e+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-5.0, it)
        }
        run {
            val it = EDN.read("-0.5e-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.05, it)
        }
    }

    @Test
    fun parseDoubleENotationBig2() {
        run {
            val it = EDN.read("0.0E+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0.0E-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0.0E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("0.0E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.0, it)
        }
        run {
            val it = EDN.read("-0.0E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }
        run {
            val it = EDN.read("-0.0E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.0, it)
        }

        run {
            val it = EDN.read("0.5E+0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("0.5E-0")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.5, it)
        }
        run {
            val it = EDN.read("0.5E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(5.0, it)
        }
        run {
            val it = EDN.read("0.5E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(0.05, it)
        }
        run {
            val it = EDN.read("-0.5E+1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-5.0, it)
        }
        run {
            val it = EDN.read("-0.5E-1")
            Assertions.assertTrue(it is Double)
            Assertions.assertEquals(-0.05, it)
        }
    }

    @Test
    fun parseBigDecimal() {
        var temp = BigDecimal.valueOf(0.0)
        run {
            val it = EDN.read("0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal.valueOf(0), it)
        }
        run {
            val it = EDN.read("0.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("+0.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("-0.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(0.5)
        run {
            val it = EDN.read("0.5M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("+0.5M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("-0.5M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(1.0)
        run {
            val it = EDN.read("1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal.valueOf(1L), it)
        }
        run {
            val it = EDN.read("-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal.valueOf(1L).negate(), it)
        }
        run {
            val it = EDN.read("1.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("+1.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("-1.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(128.0)
        run {
            val it = EDN.read("128M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal.valueOf(128L), it)
        }
        run {
            val it = EDN.read("-128M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal.valueOf(128L).negate(), it)
        }
        run {
            val it = EDN.read("128.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("+128.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("-128.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(255.0)
        run {
            val it = EDN.read("255M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal.valueOf(255), it)
        }
        run {
            val it = EDN.read("-255M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal.valueOf(255).negate(), it)
        }
        run {
            val it = EDN.read("255.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("+255.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("-255.0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp.negate(), it)
        }

        temp = BigDecimal.valueOf(255.25)
        run {
            val it = EDN.read("255.25M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("+255.25M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp, it)
        }
        run {
            val it = EDN.read("-255.25M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(temp.negate(), it)
        }
    }

    @Test
    fun parseBigDecimalENotation() {
        run {
            val it = EDN.read("0.0e+0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e+0"), it)
        }
        run {
            val it = EDN.read("0.0e-0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e-0"), it)
        }
        run {
            val it = EDN.read("0.0e+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e+1"), it)
        }
        run {
            val it = EDN.read("0.0e-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e-1"), it)
        }
        run {
            val it = EDN.read("-0.0e+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.0e+1"), it)
        }
        run {
            val it = EDN.read("-0.0e-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.0e-1"), it)
        }

        run {
            val it = EDN.read("0.5e+0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e+0"), it)
        }
        run {
            val it = EDN.read("0.5e-0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e-0"), it)
        }
        run {
            val it = EDN.read("0.5e+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e+1"), it)
        }
        run {
            val it = EDN.read("0.5e-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e-1"), it)
        }
        run {
            val it = EDN.read("-0.5e+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.5e+1"), it)
        }
        run {
            val it = EDN.read("-0.5e-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.5e-1"), it)
        }
    }

    @Test
    fun parseBigDecimalENotationBig2() {
        run {
            val it = EDN.read("0.0E+0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e+0"), it)
        }
        run {
            val it = EDN.read("0.0E-0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e-0"), it)
        }
        run {
            val it = EDN.read("0.0E+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e+1"), it)
        }
        run {
            val it = EDN.read("0.0E-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.0e-1"), it)
        }
        run {
            val it = EDN.read("-0.0E+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.0e+1"), it)
        }
        run {
            val it = EDN.read("-0.0E-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.0e-1"), it)
        }

        run {
            val it = EDN.read("0.5E+0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e+0"), it)
        }
        run {
            val it = EDN.read("0.5E-0M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e-0"), it)
        }
        run {
            val it = EDN.read("0.5E+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e+1"), it)
        }
        run {
            val it = EDN.read("0.5E-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("0.5e-1"), it)
        }
        run {
            val it = EDN.read("-0.5E+1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.5e+1"), it)
        }
        run {
            val it = EDN.read("-0.5E-1M")
            Assertions.assertTrue(it is BigDecimal)
            Assertions.assertEquals(BigDecimal("-0.5e-1"), it)
        }
    }
}
