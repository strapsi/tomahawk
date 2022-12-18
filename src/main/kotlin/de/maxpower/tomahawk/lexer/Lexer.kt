package de.maxpower.tomahawk.lexer

object Lexer {

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
                        current == '(' -> tokens.addToken(TokenType.LeftBrace, lineIndex + 1, charIndex, charIndex)
                        current == ')' -> tokens.addToken(TokenType.RightBrace, lineIndex + 1, charIndex, charIndex)
                        current == '[' -> tokens.addToken(TokenType.LeftBracket, lineIndex + 1, charIndex, charIndex)
                        current == ']' -> tokens.addToken(TokenType.RightBracket, lineIndex + 1, charIndex, charIndex)
                        current == '{' -> tokens.addToken(TokenType.LeftCurly, lineIndex + 1, charIndex, charIndex)
                        current == '}' -> tokens.addToken(TokenType.RightCurly, lineIndex + 1, charIndex, charIndex)
                        current == '<' -> tokens.addToken(TokenType.LessThan, lineIndex + 1, charIndex, charIndex)
                        current == '>' -> tokens.addToken(TokenType.GreaterThan, lineIndex + 1, charIndex, charIndex)
                        current == ':' -> tokens.addToken(TokenType.Colon, lineIndex + 1, charIndex, charIndex)
                        current == '\'' -> TODO("implement strings")
                        current.isDigit() -> TODO("implement numbers")
                        current.isLetter() -> TODO("implement identifiers")
                    }
                }
            }
        return tokens
    }

}
