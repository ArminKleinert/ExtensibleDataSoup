package kleinert.soap.edn

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EdnReaderExceptionTest {
    @Test
    fun positionStartTest() {
        try {EDN.read("[")}
        catch(ex:EdnReaderException){
            Assertions.assertEquals(1, ex.textIndex)
            Assertions.assertEquals(0, ex.lineIndex)
        }
        try {EDN.read("]")}
        catch(ex:EdnReaderException){
            Assertions.assertEquals(1, ex.textIndex)
            Assertions.assertEquals(0, ex.lineIndex)
        }
    }
    @Test
    fun positionTest() {
        try {EDN.read("   [")}
        catch(ex:EdnReaderException){
            Assertions.assertEquals(4, ex.textIndex)
            Assertions.assertEquals(0, ex.lineIndex)
        }
        try {EDN.read("\n\n   ]")}
        catch(ex:EdnReaderException){
            Assertions.assertEquals(6, ex.textIndex)
            Assertions.assertEquals(2, ex.lineIndex)
        }
    }
}