package kleinert.soap.edn

import kleinert.soap.data.Complex
import kleinert.soap.data.Keyword
import kleinert.soap.data.PersistentList
import kleinert.soap.data.Symbol
import org.junit.jupiter.api.Test

class ATest {
    @Test
    fun test() {
        EDN.pprintln(1)
        EDN.pprintln(1.0)
        EDN.pprintln("abc")
        EDN.pprintln(
            listOf(
                "str",
                Symbol.symbol("abc", "def"),
                Keyword["abc"],
                listOf(77),
                Complex.Companion.valueOf(1, 2),
                5,
                intArrayOf(9, 9, 0),
                PersistentList.of(5, 6, 7),
                mapOf("key" to "val", "key2" to "val2")
            )
        )

        data class Dice(val sides: Int)
        val encoder: (Any)->Pair<String,Any?>? = {"dice" to listOf((it as Dice).sides) }
        val options = EDN.defaultOptions.copy(ednClassEncoders=listOf(Dice::class.java to encoder))
        EDN.pprintln(Dice(6), options=options)

        EDN.pprintln((0..100))
    }
}
