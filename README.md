# ExtensibleDataSoap
An EDN library for Kotlin.

## Standard things implemented

☑ Use `UTF-8`  
☑ String literals  
☑ Symbol  
☑ Keyword  
☑ Char literals  
☑ Unicode Char literals (`\uXXXX` and `\oXXX`)  
☑ Comments  
☑ `nil`, `true`, `false`  

☑ Number literals  
- Signed values.
- `N` and `M` suffixes.
- Base prefix `0` for base 8 and `0x` for base 16.
- `kleinert.soap.Ratio` type.

☑ Symbolic values `##NaN`, `##INF` and `##-INF`  
☑ Built-in tagged elements `#uuid` and `#inst`  
☑ Discard with `#_`  
☑ Tags  

☑ Lists and vectors  
☑ Sets  
☑ Maps

## Known bugs

- If an input contains more than one expression, the reader just returns the first expression. For example, `EDNSoapReader.readString("1 2")` should fail, but does not. It just returns `1`.

## Not implemented
☐ Metadata  
- Why not? I don't see a use for this yet.
- Ideas: May return a `data class` which can be split with `componentN` methods.

☐ `@`, `'`, `~` and backtick prefixes, `#` prefix for lists.
- Why not? I don't see a use for them in this context.

## Non-standard

I included some non-standard features. All of these can be turned off (see examples).

☑ `0o` and `0b` prefixes for numbers (for octal and binary respectively). (Option: `moreNumberPrefixes`)  
☑ Number suffixes for different sizes. (Option: `allowNumericSuffixes`)
- `_i8` (`Byte`)
- `_i16` (`Short`)
- `_i32` (`Int`)
- `_i64` and `L` (`Long`)  

☑ UTF-32 char literals (`\xXXXXXXXX`). (Option: `allowSchemeUTF32Codes`)   
☑ Scheme-style char literals. These return strings, not chars. (Option: `allowDispatchChars`)   
- `#\oXXX`
- `#\uXXXX` and `#\uXXXXXXXX`
- `#\xXXXXXXXX`

☑ `##time` dispatch. Returns `LocalDateTime.now()`. (Option: `allowTimeDispatch`)  
☑ Tags can be more free in their naming. (Option: `allowMoreEncoderDecoderNames`)   

## Examples

```kotlin
import kleinert.soap.EDNSoapOptions
import kleinert.soap.EDNSoapReader

fun examples1() {
    fun testFunDefault(s: String) {
        val data = EDNSoapReader.readString(s)
        println("\"" + data + "\" " + (data?.javaClass ?: "null"))
    }

    fun testFunExtend(s: String) {
        val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
        println("\"" + data + "\" " + (data?.javaClass ?: "null"))
    }
    testFunDefault("\\a") // "a Class Char"

    // Unicode sequences in strings work.
    testFunDefault("\"\\uD83C\\uDF81\"") // "🎁" class java.lang.String
    testFunExtend("\"\\x0001F381\"") // "🎁" class java.lang.String

    // UTF-32 evaluates to strings.
    testFunDefault("\\u0660") // "٠" class java.lang.Character
    testFunExtend("#\\u0660") // "٠" class java.lang.String
    testFunExtend("#\\u0001F381") // "🎁" class java.lang.String

    // Discard is right-associative.
    testFunDefault("#_ #_ \"1\" \\a 1") // "1" class java.lang.Long
    testFunDefault("#_#_ \"1\" \\a 1") // "1" class java.lang.Long

    // Comments are ignored
    testFunDefault(";bc\n123") // "123" class java.lang.Long

    testFunDefault("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"") // "f81d4fae-7dec-11d0-a765-00a0c91e6bf6" class java.util.UUID

    testFunDefault("#inst \"1985-04-12T23:20:50.52Z\"") // "1985-04-12T23:20:50.520Z" class java.time.Instant

    testFunDefault("1,") // "1" class java.lang.Long
    testFunDefault("1 2") // Error: Only one expression allowed.
}
```

Tags could have been implemented by using reflection, but I prefer the illusion of safety, so to use tagged elements, we take a `Map<String, (Any?)->Any?>`.
As noted in the examples above, the buildins `#uuid` and `#inst` do not require such "complex" operations.
```kotlin
import kleinert.soap.EDNSoapOptions
import kleinert.soap.EDNSoapReader

fun examples2() {
    fun mapOrListToPair(elem: Any?): Any? = when (elem) {
        is Map<*, *> -> (elem["first"] to elem["second"])
        is List<*> -> elem[0] to elem[1]
        else -> throw IllegalArgumentException()
    }

    run {
        val decoders = mapOf("my/pair" to ::mapOrListToPair)
        println(EDNSoapReader.readString(
            "[ #my/pair {\"first\" 4 \"second\" 5} #my/pair [4 5] ] ",
            EDNSoapOptions.defaultOptions.copy(ednClassDecoders = decoders)
        ))
    } // Output: [(4, 5), (4, 5)]

    run {
        // Allow more freedom in naming here.
        val decoders = mapOf("pair" to ::mapOrListToPair)
        println(EDNSoapReader.readString(
            "[ #pair {\"first\" 4 \"second\" 5} #pair [4 5] ] ",
            EDNSoapOptions.defaultOptions.copy(allowMoreEncoderDecoderNames = true, ednClassDecoders = decoders)
        ))
    } // Output: [(4, 5), (4, 5)]
}
```

☑
☐