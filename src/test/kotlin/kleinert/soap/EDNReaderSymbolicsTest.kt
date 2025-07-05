package kleinert.soap

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant

class EDNReaderSymbolicsTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    private fun soapE(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    }

    @Test
    fun parseNaNTest() {
        Assertions.assertTrue((soap("##NaN") as Double).isNaN())
        Assertions.assertTrue((soap("##-NaN") as Double).isNaN())
    }

    @Test
    fun parseInfinityTest() {
        Assertions.assertEquals(Double.POSITIVE_INFINITY, soap("##INF"))
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, soap("##-INF"))
    }

    @Test
    fun parseTimeTest() {
        Assertions.assertThrows(EdnReaderException::class.java) { soap("##time") }
        Assertions.assertInstanceOf(Instant::class.java, soapE("##time"))
    }
}
