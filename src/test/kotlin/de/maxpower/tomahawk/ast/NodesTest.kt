package de.maxpower.tomahawk.ast

import de.maxpower.tomahawk.lexer.Position
import de.maxpower.tomahawk.lexer.Token
import de.maxpower.tomahawk.lexer.TokenType
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class NodesTest {

    @Test
    fun `it should pretty print a const statement`() {
        val stmt = ConstStatement(
            Token(TokenType.Const, Position(0, 0, 3), "const"),
            Identifier(Token(TokenType.Identifier, Position(0, 5, 5), "x"), "x"),
            Identifier(Token(TokenType.Identifier, Position(0, 5, 5), "y"), "y")
        )
        expectThat(stmt.toString()) isEqualTo "const x = y"
    }

}