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

private val precedences = mapOf(
    TokenType.Equals to Precedence.Equals,
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

private fun parseInfixExpression(token: Token, tokens: TokenList, left: Expression, precedence: Int): Expression {
    val right = parseExpression(tokens.next(), tokens, precedence)
    return InfixExpression(token, left, right)
}

private fun prefixParser(type: TokenType): PrefixParseFunction = when (type) {
    TokenType.Identifier -> ::parseIdentifier
    TokenType.Number -> ::parseNumber
    TokenType.True,
    TokenType.False -> ::parseBooleanExpression

    else -> TODO("$type not implemented")
}

private fun infixParser(type: TokenType): InfixParseFunction? = when (type) {
    TokenType.Plus,
    TokenType.Minus,
    TokenType.Multiply,
    TokenType.Divide -> ::parseInfixExpression

    else -> TODO("$type not implemented")
}