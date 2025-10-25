package kleinert.edn.reader

import kleinert.edn.EDN
import kleinert.edn.EDNSoapOptions
import kleinert.edn.ExtendedEDNDecoders
import kleinert.edn.data.Keyword
import kleinert.edn.data.PersistentList
import kleinert.edn.data.Symbol
import kleinert.edn.reader.EdnReaderException
import kleinert.edn.reader.EdnReaderException.EdnClassConversionError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EDNReaderConstantsTest {
    @Test
    fun parseDirectConstantsTest() {
        Assertions.assertEquals(false, EDN.read("false"))
        Assertions.assertEquals(true, EDN.read("true"))
        Assertions.assertEquals(null, EDN.read("nil"))
    }
}
