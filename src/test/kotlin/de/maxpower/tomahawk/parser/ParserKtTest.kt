package de.maxpower.tomahawk.parser

import de.maxpower.tomahawk.ast.*
import de.maxpower.tomahawk.lexer.TokenType
import de.maxpower.tomahawk.lexer.lex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isTrue

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
        val program = parse(lex("return 13"))
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
        val program = parse(lex("return 1_000"))
        expectThat(program.statements.first()) {
            isA<ReturnStatement>() and {
                get { value }.isA<NumberLiteral>() and {
                    get { value } isEqualTo 1000.0
                }
            }
        }
    }
}
