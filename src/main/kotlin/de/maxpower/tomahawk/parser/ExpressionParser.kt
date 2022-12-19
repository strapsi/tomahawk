package de.maxpower.tomahawk.parser

import de.maxpower.tomahawk.ast.*
import de.maxpower.tomahawk.lexer.Token
import de.maxpower.tomahawk.lexer.TokenList
import de.maxpower.tomahawk.lexer.TokenType


typealias PrefixParseFunction = (token: Token) -> Expression
typealias InfixParseFunction = (token: Token, tokens: TokenList, other: Expression, precedence: Int) -> Expression

object Precedence {
    const val Lowest = 0
    const val Equals = 1
    const val LessGreater = 2
    const val PlusMinus = 3
    const val MultiplyDivide = 4
    const val Call = 5
}

fun parseExpression(token: Token?, tokens: TokenList, precedence: Int = Precedence.Lowest): Expression {
    if (token == null) throw RuntimeException("parseExpression on null")
    val prefixParserFn = prefixParser(token.type)
    var leftExpression = prefixParserFn(token)
    while (tokens.peek() != null && precedence < precedence(tokens.peek())) {
        val infixParseFn = infixParser(tokens.peek()!!.type) ?: return leftExpression
        val next = tokens.next() ?: throw RuntimeException("expecting expression but got: null")
        leftExpression = infixParseFn(next, tokens, leftExpression, precedence(next))
    }

    return leftExpression
}

// return the last expected token if the path matches
// otherwise throw exception
fun expect(tokens: TokenList, vararg type: TokenType): Token {
    return type.foldIndexed(tokens.next()) { index, token, tokenType ->
        if (token != null && token.type == tokenType)
            if (index == type.lastIndex) token
            else tokens.next()
        else throw RuntimeException("expected ${tokenType.pretty} but got ${token?.pretty}")
    } ?: throw RuntimeException("expected $")
}

private val precedences = mapOf(
    TokenType.Equals to Precedence.Equals,
    TokenType.Bang to Precedence.Equals,
    TokenType.GreaterThan to Precedence.LessGreater,
    TokenType.LessThan to Precedence.LessGreater,
    TokenType.Plus to Precedence.PlusMinus,
    TokenType.Minus to Precedence.PlusMinus,
    TokenType.Multiply to Precedence.MultiplyDivide,
    TokenType.Divide to Precedence.MultiplyDivide,
    TokenType.LeftParen to Precedence.Call
)

private fun precedence(token: Token?): Int {
    return precedences[token?.type ?: Precedence.Lowest] ?: Precedence.Lowest
}

private fun parseIdentifier(token: Token): Expression {
    return Identifier(token, token.pretty)
}

private fun parseNumber(token: Token): Expression {
    val number = token.pretty
        .replace("_", "")
        .toDoubleOrNull()
        ?: throw RuntimeException("could not parse value <${token.pretty}> as a number")

    return NumberLiteral(token, number)
}

private fun parseBooleanExpression(token: Token): Expression {
    return BooleanLiteral(token, token.type == TokenType.True)
}

//private fun parsePrefixExpression(token: Token, tokens: TokenList): Expression {
//
//}

private fun parseInfixExpression(token: Token, tokens: TokenList, left: Expression, precedence: Int): Expression {
    // check if it is != (not equals)
    // if so, wrap the expression in a !-PrefixExpression
    val operator = if (token.type == TokenType.Bang) expect(tokens, TokenType.Equals)
    else token
    val right = parseExpression(tokens.next(), tokens, precedence)
    val infix = InfixExpression(operator, left, right)
    return if (token.type == TokenType.Bang) PrefixExpression(token, infix)
    else infix
}

private fun prefixParser(type: TokenType): PrefixParseFunction = when (type) {
    TokenType.Identifier -> ::parseIdentifier
    TokenType.Number -> ::parseNumber
    TokenType.True,
    TokenType.False -> ::parseBooleanExpression

    else -> TODO("$type not implemented")
}

private fun infixParser(type: TokenType): InfixParseFunction? = when (type) {
    TokenType.Equals,
    TokenType.Bang,
    TokenType.Plus,
    TokenType.Minus,
    TokenType.Multiply,
    TokenType.Divide -> ::parseInfixExpression

    else -> TODO("$type not implemented")
}