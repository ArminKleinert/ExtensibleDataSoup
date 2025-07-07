package kleinert.soap

import kleinert.soap.data.Complex
import kleinert.soap.data.Ratio
import kleinert.soap.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderNumberRatioComplexTest {
    @Test
    fun parseRatio() {
        EDN.read("0/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.ZERO, it)
        }
        EDN.read("-0/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.ZERO, it)
        }

        EDN.read("1/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1), it)
        }
        EDN.read("-1/1").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1).negate(), it)
        }

        EDN.read("1/2").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2), it)
        }
        EDN.read("-1/2").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2).negate(), it)
        }

        EDN.read("1/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2567), it)
        }
        EDN.read("-1/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(1, 2567).negate(), it)
        }

        EDN.read("123456789/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567), it)
        }
        EDN.read("-123456789/2567").let {
            Assertions.assertInstanceOf(Ratio::class.java, it)
            Assertions.assertEquals(Ratio.valueOf(123456789, 2567).negate(), it)
        }
    }

    @Test
    fun parseComplex() {
        val optionsWithComplex = EDN.defaultOptions.copy(allowComplexNumberLiterals = true)
        
        EDN.read("0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 0), it)
        }
        EDN.read("+0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 0), it)
        }
        EDN.read("-0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0.0, -0.0), it)
        }

        EDN.read("1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 1), it)
        }
        EDN.read("+1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, 1), it)
        }
        EDN.read("-1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(0, -1), it)
        }

        EDN.read("1+0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1), it)
        }
        EDN.read("+1+0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1), it)
        }
        EDN.read("-1+0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1), it)
        }

        EDN.read("1+1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, 1), it)
        }
        EDN.read("+1+1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, 1), it)
        }
        EDN.read("-1+1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1, 1), it)
        }

        EDN.read("1-1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, -1), it)
        }
        EDN.read("+1-1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1, -1), it)
        }
        EDN.read("-1-1i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1, -1), it)
        }

        EDN.read("1.5+0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5), it)
        }
        EDN.read("+1.5+0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5, 0.0), it)
        }
        EDN.read("-1.5+0i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1.5, 0.0), it)
        }

        EDN.read("1.5+0.5i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5, 0.5), it)
        }
        EDN.read("+1.5+0.5i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(1.5, 0.5), it)
        }
        EDN.read("-1.5+0.5i", optionsWithComplex).let {
            Assertions.assertInstanceOf(Complex::class.java, it)
            Assertions.assertEquals(Complex.valueOf(-1.5, 0.5), it)
        }
    }
}
