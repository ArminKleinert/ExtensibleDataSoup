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



## Not implemented
☐ Metadata  
- Why not? I don't see a use for this yet.
- Ideas: May return a `data class` which can be split with `componentN` methods.

☐ `@`, `'` and backtick prefixes, `#` prefix for lists.
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
☑ Tagged elements with non-map inputs. (Option: `allowNonMapDecode`)   

## Examples

```kotlin
import kleinert.soap.EDNSoapOptions
import kleinert.soap.EDNSoapReader

fun testFunDefault(s:String) {
    val data = EDNSoapReader.readString(s)
    println("\""+data+"\" " + (data?.javaClass ?: "null"))
}
fun testFunExtend(s:String) {
    val data = EDNSoapReader.readString(s, EDNSoapOptions.extendedOptions)
    println("\""+data+"\" " + (data?.javaClass ?: "null"))
}
testFunDefault("\\a") // "a Class Char"

// Unicode sequences in strings work.
testFunDefault("\"\\uD83C\\uDF81\"") // "🎁" class java.lang.String
testFunExtend ("\"\\x0001F381\"") // "🎁" class java.lang.String

// UTF-32 evaluates to strings.
testFunExtend ("\\x0001F381") // "🎁" class java.lang.String
testFunExtend ("#\\u0001F381") // "🎁" class java.lang.String

// Discard is right-associative.
testFunDefault("#_ #_ \"1\" \\a 1") // "1" class java.lang.Long
testFunDefault("#_#_\"1\" \\a 1") // "1" class java.lang.Long

testFunDefault(";bc\n123") // "123" class java.lang.Long

```

To use tagged elements.
```kotlin
import kleinert.soap.EDNSoapOptions
import kleinert.soap.EDNSoapReader

```

☑
☐