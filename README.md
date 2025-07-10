# ExtensibleDataSoap

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
- `kleinert.soap.Ratio` type.

☑ Symbolic values `##NaN`, `##INF` and `##-INF`  
☑ Built-in tagged elements `#uuid` and `#inst`  
☑ Discard with `#_`  
☑ Tagged elements.

☑ Lists and vectors  
☑ Sets  
☑ Maps

## TODOs

☐ Metadata

- Returns an `IObj` object which can be split into its `meta` and `object` parts.

## Not implemented

☐ `@`, `'`, `~` and backtick prefixes, `#` prefix for lists.

- Why not? I don't see a use for them in this context.

## Non-standard

I included some non-standard features. All of these can be turned off (see examples).

| Option                            | Description                                                                  | Default                  |
|:----------------------------------|:-----------------------------------------------------------------------------|:-------------------------|
| `allowSchemeUTF32Codes`           | Allow scheme UTF-32 char codes (`\\xXXXXXXXX`).                              | `false`                  |
| `allowDispatchChars`              | Allow dispatch UTF-32chars (`#\uXXXXXXXX`).                                  | `false`                  |
| `ednClassDecoders`                | Functions for decoding tagged elements to objects.                           | `mapOf()`                |
| `ednClassEncoders`                | Functions for encoding objects to tagged elements.                           | `mapOf()`                |
| `allowTimeDispatch`               | Enable `##time` dispatch.                                                    | `false`                  |
| `allowNumericSuffixes`            | Enable `_i8`, `_16`, `_i32`, `_64`, and `L` suffixes for integral numbers.   | `false`                  |
| `allowMoreEncoderDecoderNames`    | Allow symbols without explicit namespaces for tagged elements.               | `false`                  |
| `decodingSequenceSeparator`       | Separator between elements in lists, vectors, maps, and sets.                | `", "`                   |
| `allowComplexNumberLiterals`      | Allow complex number literals.                                               | `false`                  |
| `allowUTFSymbols`                 | Allow UTF-8 strings as symbols.                                              | `false`                  |
| `sequenceElementLimit`            | Limit of elements after which lazy sequences are shortened.                  | `10000`                  |
| `listToPersistentListConverter`   | Converter function from Kotlin's default List to your preferred list type.   | `{PersistentList(it)}`   |
| `listToPersistentVectorConverter` | Converter function from Kotlin's default List to your preferred vector type. | `{PersistentVector(it)}` |
| `setToPersistentSetConverter`     | Converter function from sets (`LinkedHashSet`) to your preferred set type.   | `{PersistentSet(it)}`    |
| `mapToPersistentMapConverter`     | Converter function from maps (`LinkedHashMap`) to your preferred map type.   | `{PersistentMap(it)}`    |

☑ `0o` and `0b` prefixes for numbers (for octal and binary respectively). (Option: `moreNumberPrefixes`)

☑ Number suffixes for different sizes. (Option: `allowNumericSuffixes`)

- `_i8` (`Byte`)
- `_i16` (`Short`)
- `_i32` (`Int`)
- `_i64` and `L` (`Long`)

☑ Complex numbers (Option: `allowComplexNumberLiterals`)

- Format: `([+\-]?\d+(.\d)?+i)|([+\-]?\d+(.\d+)?[+\-](\d+(.\d+)?)?i)`
- Eg. `1+1i`, `0i`, `1.2i`, `+0.1-0i`

☑ Scheme-style char literals. These return strings, not chars. (Option: `allowDispatchChars`)

- `#\oXXX`
- `#\uXXXX` and `#\uXXXXXXXX`
- `#\xXXXXXXXX`

☑ `##time` dispatch. Returns `LocalDateTime.now()`. (Option: `allowTimeDispatch`)

☑ Tags can be more free in their naming. (Option: `allowMoreEncoderDecoderNames`)

## Examples

```kotlin
import kleinert.soap.edn.EDN

val parsed = EDN.read()
```

☑
☐