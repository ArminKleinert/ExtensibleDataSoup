package kleinert.edn.reader

import kleinert.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderMacrosTest {
    @Test
    fun parseCommentTest() {
        Assertions.assertEquals(null, EDN.read("nil"))
    }
}
