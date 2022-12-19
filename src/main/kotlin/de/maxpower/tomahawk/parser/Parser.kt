package de.maxpower.tomahawk.parser

import de.maxpower.tomahawk.ast.*
import de.maxpower.tomahawk.lexer.Position
import de.maxpower.tomahawk.lexer.Token
import de.maxpower.tomahawk.lexer.TokenList
import de.maxpower.tomahawk.lexer.TokenType

fun parse(tokens: TokenList): Program {
    val statements = mutableListOf<Statement>()
    println(tokens)
    while (tokens.hasNext()) {
        val next = tokens.next()!!
        parseStatement(next, tokens).let(statements::add)
    }
    return Program(Token(TokenType.Return, Position(-1, -1, -1), "program"), statements)
}

private object Precendence {
    const val LOWEST = 0
    const val EQUALS = 1
    const val LESS_GREATER = 2
    const val SUM = 3
    const val PRODUCT = 4
    const val PREFIX = 5
    const val CALL = 6
}

private val precedences = mapOf(
    TokenType.Equals to Precendence.EQUALS,
    TokenType.GreaterThan to Precendence.LESS_GREATER,
    TokenType.LessThan to Precendence.LESS_GREATER,
    TokenType.Plus to Precendence.SUM,
    TokenType.Minus to Precendence.SUM,
    TokenType.Multiply to Precendence.PRODUCT,
    TokenType.Divide to Precendence.PRODUCT,
    TokenType.LeftParen to Precendence.CALL
)

typealias PrefixParseFunction = (token: Token) -> Expression
typealias InfixParseFunction = (token: Token, tokens: TokenList, other: Expression, precedence: Int) -> Expression

private fun precedence(token: Token?): Int {
    return precedences[token?.type ?: Precendence.LOWEST] ?: Precendence.LOWEST
}

private fun parseIdentifier(token: Token): Expression {
    return Identifier(token, token.pretty)
}

private fun parseNumber(token: Token): Expression {
    // TODO: remove special chars like "_"
    val number = token.pretty.toDoubleOrNull()
    // TODO: number should probably always be parsable.
    //       maybe return an error if not
    return NumberLiteral(token, number!!)
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

private fun parseStatement(token: Token, tokens: TokenList): Statement =
    when (token.type) {
        TokenType.Const -> parseConstStatement(token, tokens)
        TokenType.Var -> parseVarStatement(token, tokens)
        TokenType.Return -> parseReturnStatement(token, tokens)
        else -> parseExpressionStatement(token, tokens)
    }

private fun parseConstStatement(token: Token, tokens: TokenList): Statement {
    val identifier = expect(tokens, TokenType.Identifier)
    expect(tokens, TokenType.Colon, TokenType.Equals)
    val next = tokens.next() // start of expression
    val expression = parseExpression(next, tokens)
    return ConstStatement(
        token,
        Identifier(identifier, identifier.pretty),
        expression
    )
}

private fun parseVarStatement(token: Token, tokens: TokenList): Statement {
    val identifier = expect(tokens, TokenType.Identifier)
    expect(tokens, TokenType.Colon, TokenType.Equals)
    val next = tokens.next() // start of expression
    val expression = parseExpression(next, tokens)
    return VarStatement(
        token,
        Identifier(identifier, identifier.pretty),
        expression
    )
}

private fun parseReturnStatement(token: Token, tokens: TokenList): Statement {
    val next = tokens.next() // start of expression
    val expression = parseExpression(next, tokens)
    return ReturnStatement(token, expression)
}

private fun parseExpressionStatement(token: Token, tokens: TokenList): Statement {
    val expression = parseExpression(token, tokens, Precendence.LOWEST)
    return ExpressionStatement(token, expression)
}

private fun parseExpression(token: Token?, tokens: TokenList, precedence: Int = Precendence.LOWEST): Expression {
    if (token == null) throw RuntimeException("parseExpression on null")
    val prefixParserFn = prefixParser(token.type)
    var leftExpression = prefixParserFn(token)
    while (tokens.peek() != null && precedence < precedence(tokens.peek())) {
        val infixParseFn = infixParser(tokens.peek()!!.type) ?: return leftExpression
        val next = tokens.next()!! // TODO: handle next = null
        leftExpression = infixParseFn(next, tokens, leftExpression, precedence(next))
    }

    return leftExpression
}

// return the last expected token if the path matches
// otherwise throw exception
private fun expect(tokens: TokenList, vararg type: TokenType): Token {
    return type.foldIndexed(tokens.next()) { index, token, tokenType ->
        if (token != null && token.type == tokenType)
            if (index == type.lastIndex) token
            else tokens.next()
        else throw RuntimeException("expected ${tokenType.pretty} but got ${token?.pretty}")
    } ?: throw RuntimeException("expected $")
}
