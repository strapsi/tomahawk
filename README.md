# Tomahawk Language

### how to use
```shell
sdk install java 22.3.r17-nik
sdk use java 22.3.r17-nik
./gradlew nativeCompile
./build/native/nativeCompile/tomahawk

# fish
ln -s (pwd)/build/native/nativeCompile/tomahawk ./th
```

### currently implemented
- Lexer
- Basic Parser
- Basic Evaluator

### example
```
const x := y
var y := -13 + x
return !true != !false
```