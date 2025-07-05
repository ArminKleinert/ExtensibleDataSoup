import kleinert.soap.*

fun main() {
    run {
        val text = "[ ##INF ]"
        val parsed = EDNSoapReader.readString(text)
        println(parsed)
    }
    for (n in ("0".toInt(8))..("777".toInt(8))) {
        println("" + n.toString(8) + " " + n.toChar())
    }
}
