package de.maxpower.tomahawk.lexer

fun lex(source: String): TokenList {
    val tokens = TokenList()
    source
        .lineSequence()
        .filterNot { it.trimStart().startsWith("#") }
        .forEachIndexed { lineIndex, line ->
            val chars = line.toList().listIterator()
            while (chars.hasNext()) {
                val charIndex = chars.nextIndex()
                val current = chars.next()
                when {
                    current.isWhitespace() -> continue
                    current == '|' -> tokens.addToken(TokenType.Pipe, lineIndex + 1, charIndex, charIndex)
                    current == '=' -> tokens.addToken(TokenType.Equals, lineIndex + 1, charIndex, charIndex)
                    current == ',' -> tokens.addToken(TokenType.Comma, lineIndex + 1, charIndex, charIndex)
                    current == '+' -> tokens.addToken(TokenType.Plus, lineIndex + 1, charIndex, charIndex)
                    current == '-' -> tokens.addToken(TokenType.Minus, lineIndex + 1, charIndex, charIndex)
                    current == '*' -> tokens.addToken(TokenType.Multiply, lineIndex + 1, charIndex, charIndex)
                    current == '/' -> tokens.addToken(TokenType.Divide, lineIndex + 1, charIndex, charIndex)
                    current == '(' -> tokens.addToken(TokenType.LeftParen, lineIndex + 1, charIndex, charIndex)
                    current == ')' -> tokens.addToken(TokenType.RightParen, lineIndex + 1, charIndex, charIndex)
                    current == '[' -> tokens.addToken(TokenType.LeftBracket, lineIndex + 1, charIndex, charIndex)
                    current == ']' -> tokens.addToken(TokenType.RightBracket, lineIndex + 1, charIndex, charIndex)
                    current == '{' -> tokens.addToken(TokenType.LeftCurly, lineIndex + 1, charIndex, charIndex)
                    current == '}' -> tokens.addToken(TokenType.RightCurly, lineIndex + 1, charIndex, charIndex)
                    current == '<' -> tokens.addToken(TokenType.LessThan, lineIndex + 1, charIndex, charIndex)
                    current == '>' -> tokens.addToken(TokenType.GreaterThan, lineIndex + 1, charIndex, charIndex)
                    current == ':' -> tokens.addToken(TokenType.Colon, lineIndex + 1, charIndex, charIndex)
                    current == '.' -> tokens.addToken(TokenType.Period, lineIndex + 1, charIndex, charIndex)
                    current == '\'' -> {
                        val read = readString(current, chars)
                        tokens.addToken(TokenType.String, lineIndex + 1, charIndex, charIndex + read.length, read)
                    }

                    current.isDigit() -> {
                        val read = readNumber(current, chars)
                        tokens.addToken(TokenType.Number, lineIndex + 1, charIndex, charIndex + read.length, read)
                    }

                    current.isLetter() -> {
                        val read = readIdentifier(current, chars)
                        tokens.addToken(
                            read.tokenType,
                            lineIndex.inc(),
                            charIndex,
                            charIndex + read.identifier.length.dec(),
                            read.identifier
                        )
                    }
                }
            }
        }
    return tokens
}

private fun readString(current: Char, chars: ListIterator<Char>): String {
    TODO("implement strings")
}

private fun readNumber(current: Char, chars: ListIterator<Char>): String {
    var number = "$current"
    while (chars.hasNext()) {
        val next = chars.next()
        if (next.isDigit() || next == '_' || next == '.') {
            number += next
            continue
        }

        chars.previous()
        break
    }
    return number
}

data class Identifier(val identifier: String, val tokenType: TokenType)

private val keywords = mapOf(
    "true" to TokenType.True,
    "false" to TokenType.False,
    "const" to TokenType.Const,
    "var" to TokenType.Var,
    "return" to TokenType.Return
)

private fun readIdentifier(current: Char, chars: ListIterator<Char>): Identifier {
    var identifier = "$current"
    while (chars.hasNext()) {
        val next = chars.next()
        if (next.isLetterOrDigit() || next == '_') {
            identifier += next
            continue
        }

        chars.previous()
        break
    }
    return Identifier(identifier, keywords[identifier] ?: TokenType.Identifier)
}
