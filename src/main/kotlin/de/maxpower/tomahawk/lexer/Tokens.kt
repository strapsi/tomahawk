package de.maxpower.tomahawk.lexer

enum class TokenType(val pretty: String = "") {
    Identifier,
    Pipe("|"),
    Equals("="),
    Comma(","),
    Plus("+"),
    Minus("-"),
    Multiply("*"),
    Divide("/"),
    LeftBrace("("),
    RightBrace(")"),
    LeftBracket("["),
    RightBracket("]"),
    LeftCurly("{"),
    RightCurly("]"),
    LessThan("<"),
    GreaterThan(">"),
    Colon(":"),
}

data class Position(val line: Int, val start: Int, val end: Int)

data class Token(val type: TokenType, val position: Position, val pretty: String)

class TokenList {
    private val tokens = mutableListOf<Token>()

    private val tokenIterator by lazy(tokens::listIterator)

    fun isEmpty() = tokens.isEmpty()

    fun addToken(type: TokenType, line: Int, start: Int, end: Int) = addToken(type, line, start, end, type.pretty)

    fun addToken(type: TokenType, line: Int, start: Int, end: Int, pretty: String) {
        Token(type, Position(line, start, end), pretty).let(tokens::add)
    }

    fun next(): Token? = if (tokenIterator.hasNext()) tokenIterator.next() else null
}