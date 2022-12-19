package de.maxpower.tomahawk.ast

import de.maxpower.tomahawk.lexer.Position
import de.maxpower.tomahawk.lexer.Token
import de.maxpower.tomahawk.lexer.TokenType

interface Node {
    val token: Token
    override fun toString(): String
}

class EmptyNode(override val token: Token = Token(TokenType.Nothing, Position(-1, -1, -1), "nothing")) : Node {
    override fun toString(): String = "nothing"
}

interface Statement : Node
interface Expression : Node
class Program(override val token: Token, val statements: List<Statement>) : Node {
    override fun toString(): String = "program"
}

class Identifier(override val token: Token, val name: String) : Expression {
    override fun toString(): String {
        return """
            Identifier:
                token: $token
                name: $name
        """
    }
}

class ConstStatement(
    override val token: Token,
    val name: Identifier,
    val value: Expression
) : Statement {

    override fun toString(): String {
        return """
            ConstStatement:
                identifier: $name
                expression: $value
        """
    }
}

class VarStatement(
    override val token: Token,
    val name: Identifier,
    val value: Expression
) : Statement {

    override fun toString(): String {
        return """
            VarStatement:
                identifier: $name
                expression: $value
        """
    }
}

class ReturnStatement(
    override val token: Token,
    val value: Expression
) : Statement {

    override fun toString(): String {
        return """
            ReturnStatement:
                expression: $value
        """
    }
}

class ExpressionStatement(override val token: Token, val expression: Expression) : Statement {
    override fun toString(): String = "($expression)"
}

class InfixExpression(override val token: Token, val left: Expression, val right: Expression): Expression {
    override fun toString(): String {
        return """
            InfixExpression:
                token: $token
                left: $left
                right: $right
        """.trimIndent()
    }
}

class NumberLiteral(override val token: Token, val value: Number) : Expression {
    override fun toString(): String {
        return """
            NumberLiteral:
                token: $token
                value: $value
        """.trimIndent()
    }
}

class BooleanLiteral(override val token: Token, val value: Boolean) : Expression {
    override fun toString(): String {
        return """
            BooleanLiteral:
                token: $token
                value: $value
        """
    }
}