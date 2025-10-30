package kleinert.edn.reader

import kleinert.edn.EDN
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EDNReaderNumberWithSuffixTest {
    @Test
    fun parseIntegerDecimal() {
        val numberPairs = listOf(
            0.toByte() to "0_i8", 0.toShort() to "0_i16", 0 to "0_i32", 0L to "0L", 0L to "0_i64",
            1.toByte() to "1_i8", 1.toShort() to "1_i16", 1 to "1_i32", 1L to "1L", 1L to "1_i64",
            (-1).toByte() to "-1_i8", (-1).toShort() to "-1_i16", (-1) to "-1_i32", (-1L) to "-1L", (-1L) to "-1_i64",
            128.toByte() to "128_i8", 128.toShort() to "128_i16", 128 to "128_i32", 128L to "128L", 128L to "128_i64",
        )
        for ((num, str) in numberPairs) {
            Assertions.assertEquals(
                num,
                EDN.read(str, EDN.defaultOptions.copy(allowNumericSuffixes = true))
            )
        }
    }

    @Test
    fun parseIntegerOctal() {
        val numberPairs = listOf(
            0.toByte() to "0o0_i8", 0.toShort() to "0o0_i16", 0 to "0o0_i32", 0L to "0o0L", 0L to "0o0_i64",
            1.toByte() to "0o1_i8", 1.toShort() to "0o1_i16", 1 to "0o1_i32", 1L to "0o1L", 1L to "0o1_i64",

            (-1).toByte() to "-0o1_i8", (-1).toShort() to "-0o1_i16", (-1) to "-0o1_i32",
            (-1L) to "-0o1L", (-1L) to "-0o1_i64",

            128.toByte() to "0o200_i8", 128.toShort() to "0o200_i16", 128 to "0o200_i32",
            128L to "0o200L", 128L to "0o200_i64",
        )
        for ((num, str) in numberPairs) {
            Assertions.assertEquals(
                num,
                EDN.read(str, EDN.defaultOptions.copy(allowNumericSuffixes = true, moreNumberPrefixes = true))
            )
        }
    }

    @Test
    fun parseIntegerBinary() {
        val numberPairs = listOf(
            0.toByte() to "0b0_i8", 0.toShort() to "0b0_i16", 0 to "0b0_i32", 0L to "0b0L", 0L to "0b0_i64",
            1.toByte() to "0b1_i8", 1.toShort() to "0b1_i16", 1 to "0b1_i32", 1L to "0b1L", 1L to "0b1_i64",

            (-1).toByte() to "-0b1_i8", (-1).toShort() to "-0b1_i16", (-1) to "-0b1_i32",
            (-1L) to "-0b1L", (-1L) to "-0b1_i64",

            128.toByte() to "0b10000000_i8", 128.toShort() to "0b10000000_i16", 128 to "0b10000000_i32",
            128L to "0b10000000L", 128L to "0b10000000_i64",
        )
        for ((num, str) in numberPairs) {
            Assertions.assertEquals(
                num,
                EDN.read(str, EDN.defaultOptions.copy(allowNumericSuffixes = true, moreNumberPrefixes = true))
            )
        }
    }

    @Test
    fun parseIntegerHex() {
        val numberPairs = listOf(
            0.toByte() to "0x0_i8", 0.toShort() to "0x0_i16", 0 to "0x0_i32", 0L to "0x0L", 0L to "0x0_i64",
            1.toByte() to "0x1_i8", 1.toShort() to "0x1_i16", 1 to "0x1_i32", 1L to "0x1L", 1L to "0x1_i64",

            (-1).toByte() to "-0x1_i8", (-1).toShort() to "-0x1_i16", (-1) to "-0x1_i32",
            (-1L) to "-0x1L", (-1L) to "-0x1_i64",

            128.toByte() to "0x80_i8", 128.toShort() to "0x80_i16", 128 to "0x80_i32",
            128L to "0x80L", 128L to "0x80_i64",
        )
        for ((num, str) in numberPairs) {
            Assertions.assertEquals(
                num,
                EDN.read(str, EDN.defaultOptions.copy(allowNumericSuffixes = true))
            )
        }
    }
}
