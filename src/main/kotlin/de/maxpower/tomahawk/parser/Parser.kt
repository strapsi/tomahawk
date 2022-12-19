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
    return Program(Token(TokenType.Nothing, Position(-1, -1, -1), "program"), statements)
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
    val expression = parseExpression(token, tokens, Precedence.Lowest)
    return ExpressionStatement(token, expression)
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
