package kleinert.soap.edn

import kleinert.soap.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderConstantsTest {
    @Test
    fun parseDirectConstantsTest() {
        Assertions.assertEquals(false, EDN.read("false"))
        Assertions.assertEquals(true, EDN.read("true"))
        Assertions.assertEquals(null, EDN.read("nil"))
    }
}
