# Tomahawk Language

### how to use
```shell
sdk install java 22.3.r17-nik
./gradlew nativeCompile
./build/native/nativeCompile/tomahawk
```

### currently implemented
- Lexer
- Basic Parser

### example
```
const x := y
var y := -13 + x
return !true != !false
```