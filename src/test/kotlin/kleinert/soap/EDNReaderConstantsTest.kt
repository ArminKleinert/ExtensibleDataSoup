package kleinert.soap

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderConstantsTest {
    private fun soap(s: String): Any? {
        return EDNSoapReader.readString(s, EDNSoapOptions.defaultOptions)
    }

    @Test
    fun parseDirectConstantsTest() {
        Assertions.assertEquals(false, soap("false"))
        Assertions.assertEquals(true, soap("true"))
        Assertions.assertEquals(null, soap("nil"))
    }
}
