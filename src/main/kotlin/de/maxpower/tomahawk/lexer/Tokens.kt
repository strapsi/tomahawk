package de.maxpower.tomahawk.lexer

enum class TokenType(val pretty: kotlin.String = "") {
    String,
    Number,
    Identifier,
    True("true"),
    False("false"),
    Const("const"),
    Var("var"),
    Return("return"),
    Pipe("|"),
    Equals("="),
    Comma(","),
    Plus("+"),
    Minus("-"),
    Multiply("*"),
    Divide("/"),
    LeftParen("("),
    RightParen(")"),
    LeftBracket("["),
    RightBracket("]"),
    LeftCurly("{"),
    RightCurly("]"),
    LessThan("<"),
    GreaterThan(">"),
    Colon(":"),
    Period("."),
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

    override fun toString(): String {
        return tokens.joinToString("\n")
    }
}