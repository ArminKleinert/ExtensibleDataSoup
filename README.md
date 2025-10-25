# ExtensibleDataSoup

An EDN library for Kotlin.

## Standard things implemented

☑ Can read from strings or from input streams.  
☑ Can write to strings or writers.

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
- `kleinert.edn.data.Ratio` type.

☑ Symbolic values `##NaN`, `##INF` and `##-INF`  
☑ Built-in tagged elements `#uuid` and `#inst`  
☑ Discard with `#_`  
☑ Tagged elements.

☑ Lists and vectors  
☑ Sets  
☑ Maps

☑ Metadata  

## Not implemented

☐ The pretty printer isn't printing so pretty yet.

☐ `@`, `'`, `~` and backtick prefixes, `#` prefix for lists.

- Why not? I don't see a use for them in this context.

## Examples

```kotlin
import kleinert.edn.data.EDN

println(EDN.read("symbol")) // Symbol without namespace
println(EDN.read("namespace/symbol")) // Symbol
println(EDN.read(":keyword")) // Keyword without namespace
println(EDN.read(":namespace/symbol")) // Keyword
println(EDN.read("\"string\"")) // String
println(EDN.read("\\c")) // Character

println(EDN.read("(list elements)")) // List
println(EDN.read("[vector elements]")) // Vector
println(EDN.read("#{set elements}")) // Set
println(EDN.read("{map-key map-value}")) // Map

println(EDN.read("0xC0FFEE")) // Long
println(EDN.read("12648430")) // The same Long
println(EDN.read("12648430N")) // The same as BigInt
println(EDN.read("5.0")) // Double
println(EDN.read("5.0M")) // BigDecimal
println(EDN.read("5/6")) // Ratio

println(EDN.read("nil")) // Null
println(EDN.read("false")) // False
println(EDN.read("true")) // True

println(EDN.read("#uuid \"f81d4fae-7dec-11d0-a765-00a0c91e6bf6\"")) // UUID
println(EDN.read("#inst \"1985-04-12T23:20:50.52Z\"")) // Instant

println(EDN.read("^:a abc")) // Meta
```

```kotlin
import kleinert.edn.data.Keyword
import kleinert.edn.data.Char32
import kleinert.edn.data.PersistentList
import kleinert.edn.data.PersistentVector
import kleinert.edn.data.PersistentSet
import kleinert.edn.data.PersistentMap
import kleinert.edn.data.IObj
import kleinert.edn.data.Ratio
import kleinert.edn.data.Symbol
import kleinert.edn.edn.EDN
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.UUID

EDN.pprintln(Symbol.symbol("symbol"))                       // symbol
EDN.pprintln(Symbol.symbol("namespace/symbol"))             // namespace/symbol
EDN.pprintln(Keyword["keyword"])                            // :keyword
EDN.pprintln(Keyword["namespace/keyword"])                  // :namespace/keyword

EDN.pprintln("string")                                      // "string"
EDN.pprintln('c')                                           // \c
EDN.pprintln('☦')                                           // \u2626

// Without special options, Char32 is printed as String.
EDN.pprintln(Char32(0x0001F381))                            // "🎁"

// PersistentLists, Sequences and other Iterables (which are not Lists, Sets or Maps) are printed with ().
EDN.pprintln(PersistentList.of(1, 2, 3))                    // (1, 2, 3)
EDN.pprintln(sequenceOf(1, 2, 3))                           // (1, 2, 3)
EDN.pprintln(1..3)                                          // (1, 2, 3)

// Vectors, Arrays and Lists (other than PersistentList) are all printed with [].
EDN.pprintln(PersistentVector.of(1, 2, 3))                  // [1, 2, 3]
EDN.pprintln(listOf(1, 2, 3))                               // [1, 2, 3]
EDN.pprintln(intArrayOf(1, 2, 3))                           // [1, 2, 3]

// PersistentSet and other Sets are treated the same
EDN.pprintln(PersistentSet.of(1, 2, 3))                     // #{1, 2, 3}
EDN.pprintln(setOf(1, 2, 3))                                // #{1, 2, 3}

// PersistentMap and other Maps are treated the same
EDN.pprintln(PersistentMap.of(1 to 2, 3 to 4))              // {1 2, 3 4}
EDN.pprintln(mapOf(1 to 2, 3 to 4))                         // {1 2, 3 4}

EDN.pprintln(0xC0FFEE)                                      // 12648430
EDN.pprintln(12648430)                                      // 12648430
EDN.pprintln(BigInteger.valueOf(12648430))                  // 12648430N
EDN.pprintln(5.0)                                           // 5.0
EDN.pprintln(BigDecimal.valueOf(5.0))                       // 5.0M
EDN.pprintln(Ratio.valueOf(5, 6))                           // 5/6

EDN.pprintln(null)                                          // nil
EDN.pprintln(true)                                          // true
EDN.pprintln(false)                                         // false

EDN.pprintln(UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6")) // #uuid "f81d4fae-7dec-11d0-a765-00a0c91e6bf6"
EDN.pprintln(Instant.parse("1985-04-12T23:20:50.52Z"))      // #inst "1985-04-12T23:20:50.520Z"

EDN.pprintln(Double.POSITIVE_INFINITY)                      // ##INF
EDN.pprintln(Double.NEGATIVE_INFINITY)                      // ##-INF
EDN.pprintln(Double.NaN)                                    // ##NaN

EDN.pprintln(IObj.valueOf(mapOf(12 to 13), 159))            // ^{12 13} 159
```

## Non-standard

I included some non-standard features. All of these can be turned off (see examples).

| Option                            | Description                                                                  | Default                  |
|:----------------------------------|:-----------------------------------------------------------------------------|:-------------------------|
| `allowSchemeUTF32Codes`           | Allow scheme UTF-32 char codes (`\\xXXXXXXXX`).                              | `false`                  |
| `allowDispatchChars`              | Allow dispatch UTF-32chars (`#\uXXXXXXXX`).                                  | `false`                  |
| `allowNumericSuffixes`            | Enable `_i8`, `_16`, `_i32`, `_64`, and `L` suffixes for integral numbers.   | `false`                  |
| `allowMoreEncoderDecoderNames`    | Allow symbols without explicit namespaces for tagged elements.               | `false`                  |
| `allowUTFSymbols`                 | Allow UTF-8 strings as symbols.                                              | `false`                  |
| `decodingSequenceSeparator`       | Separator between elements in lists, vectors, maps, and sets.                | `", "`                   |
| `ednClassDecoders`                | Functions for decoding tagged elements to objects.                           | `mapOf()`                |
| `ednClassEncoders`                | Functions for encoding objects to tagged elements.                           | `listOf()`               |
| `listToPersistentListConverter`   | Converter function from Kotlin's default List to your preferred list type.   | `{PersistentList(it)}`   |
| `listToPersistentVectorConverter` | Converter function from Kotlin's default List to your preferred vector type. | `{PersistentVector(it)}` |
| `mapToPersistentMapConverter`     | Converter function from maps (`LinkedHashMap`) to your preferred map type.   | `{PersistentMap(it)}`    |
| `setToPersistentSetConverter`     | Converter function from sets (`LinkedHashSet`) to your preferred set type.   | `{PersistentSet(it)}`    |
| `sequenceElementLimit`            | Limit of elements after which lazy sequences are shortened.                  | `10000`                  |

☑ `0o` and `0b` prefixes for numbers (for octal and binary respectively). (Option: `moreNumberPrefixes`)

☑ Number suffixes for different sizes. (Option: `allowNumericSuffixes`)

- `_i8` (`Byte`)
- `_i16` (`Short`)
- `_i32` (`Int`)
- `_i64` and `L` (`Long`)

☑ Scheme-style char literals. These return strings, not chars. (Option: `allowDispatchChars`)

- `#\oXXX`
- `#\uXXXX` and `#\uXXXXXXXX`
- `#\xXXXXXXXX`

☑ Tags can be more free in their naming. (Option: `allowMoreEncoderDecoderNames`)

☑
☐