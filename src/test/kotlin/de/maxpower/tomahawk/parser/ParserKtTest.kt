package de.maxpower.tomahawk.parser

import de.maxpower.tomahawk.ast.*
import de.maxpower.tomahawk.lexer.TokenType
import de.maxpower.tomahawk.lexer.lex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*

class ParserKtTest {

    @Test
    fun `it should parse a const statement`() {
        val program = parse(lex("const x := y"))
        expectThat(program.statements.first()) {
            isA<ConstStatement>() and {
                get { name.token.type } isEqualTo TokenType.Identifier
                get { name.name } isEqualTo "x"
                get { value.token.type } isEqualTo TokenType.Identifier
            }
        }
    }

    @Test
    fun `it should parse a var statement`() {
        val program = parse(lex("var x := y"))
        expectThat(program.statements.first()) {
            isA<VarStatement>() and {
                get { name.token.type } isEqualTo TokenType.Identifier
                get { name.name } isEqualTo "x"
                get { value.token.type } isEqualTo TokenType.Identifier
            }
        }
    }

    @Test
    fun `it should parse a return statement`() {
        val program = parse(lex("return x"))
        expectThat(program.statements.first()) {
            isA<ReturnStatement>() and {
                get { value.token.type } isEqualTo TokenType.Identifier
            }
        }
    }

    @Test
    fun `it should parse a simple expression`() {
        val program = parse(lex("return true"))
        expectThat(program.statements.first()) {
            isA<ReturnStatement>() and {
                get { token.type } isEqualTo TokenType.Return
                get { value.token.type } isEqualTo TokenType.True
                get { value }.isA<BooleanLiteral>() and {
                    get { value }.isTrue()
                }
            }
        }
    }


    @Test
    fun `it should parse a number expression`() {
        val program = parse(lex("return 13.0"))
        expectThat(program.statements.first()) {
            isA<ReturnStatement>() and {
                get { token.type } isEqualTo TokenType.Return
                get { value.token.type } isEqualTo TokenType.Number
                get { value }.isA<NumberLiteral>() and {
                    get { value } isEqualTo 13.0
                }
            }
        }
    }

    @Test
    fun `it should parse a simple addition`() {
        val program = parse(lex("return 10 + 3"))
        expectThat(program.statements.first()) {
            isA<ReturnStatement>() and {
                get { token.type } isEqualTo TokenType.Return
                get { value }.isA<Expression>()
            }
        }
    }

    @Test
    fun `it should parse numbers with underscores`() {
        val program = parse(lex("return 1_000.0"))
        expectThat(program.statements.first()) {
            isA<ReturnStatement>() and {
                get { value }.isA<NumberLiteral>() and {
                    get { value } isEqualTo 1000.0
                }
            }
        }
    }

    @Test
    fun `it should parse not equals`() {
        val program = parse(lex("true != false"))
        expectThat(program.statements.first()) {
            isA<ExpressionStatement>() and {
                get { expression }.isA<PrefixExpression>() and {
                    get { token.type } isEqualTo TokenType.Bang
                    get { value }.isA<InfixExpression>() and {
                        get { left }.isA<BooleanLiteral>() and { get { value }.isTrue() }
                        get { right }.isA<BooleanLiteral>() and { get { value }.isFalse() }
                    }
                }
            }
        }
    }

    @Test
    fun `it should parse prefix expressions`() {
        val program = parse(
            lex(
                """
            !true
            return -10
        """.trimIndent()
            )
        )
        expectThat(program.statements.first()) {
            isA<ExpressionStatement>() and {
                get { expression }.isA<PrefixExpression>() and {
                    get { token.type } isEqualTo TokenType.Bang
                    get { value }.isA<BooleanLiteral>() and { get { value }.isTrue() }
                }
            }
        }
        expectThat(program.statements.last()) {
            isA<ReturnStatement>() and {
                get { value }.isA<PrefixExpression>() and {
                    get { token.type } isEqualTo TokenType.Minus
                    get { value }.isA<NumberLiteral>() and { get { value } isEqualTo 10 }
                }
            }
        }
    }
}
