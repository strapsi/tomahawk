package de.maxpower.tomahawk.lexer

import de.maxpower.tomahawk.utils.must
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.*

class LexerTest {

    @Test
    fun `it should return a list of tokens`() {
        val tokens = lex("")
        expectThat(tokens) {
            get(TokenList::isEmpty).isTrue()
        }
    }


    @Test
    fun `it should lex all single char special characters`() {
        val tokens = lex(" | = , + - * / ( ) [ ] { } < > : . !")
        expect {
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Pipe }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Equals }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Comma }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Plus }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Minus }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Multiply }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Divide }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.LeftParen }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.RightParen }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.LeftBracket }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.RightBracket }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.LeftCurly }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.RightCurly }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.LessThan }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.GreaterThan }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Colon }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Period }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Bang }
            that(tokens.next()).isNull()
        }
    }

    @Test
    fun `it should provide infos about the position of a token`() {
        val tokens = lex(
            """
            ( )
            [ ]
        """.trimIndent()
        )
        expect {
            that(tokens.next()) must { get { position.line } isEqualTo 1 }
            that(tokens.next()) must { get { position.start } isEqualTo 2 }
            that(tokens.next()) must { get { position.end } isEqualTo 0 }
            that(tokens.next()) must { get { position.line } isEqualTo 2 }
        }
    }

    @Test
    fun `it should lex true and false identifiers`() {
        val tokens = lex(
            """
            true
            false
        """.trimIndent()
        )
        expect {
            that(tokens.next()) must {
                get { type } isEqualTo TokenType.True
                get { pretty } isEqualTo "true"
            }
            that(tokens.next()) must {
                get { type } isEqualTo TokenType.False
                get { pretty } isEqualTo "false"
                get { position.line } isEqualTo 2
                get { position.start } isEqualTo 0
                get { position.end } isEqualTo 4
            }
        }
    }

    @Test
    fun `it should lex identifiers`() {
        val tokens = lex("kann das sein_123")
        expect {
            that(tokens.next()) must {
                get { type } isEqualTo TokenType.Identifier
                get { pretty } isEqualTo "kann"
            }
            that(tokens.next()) must {
                get { type } isEqualTo TokenType.Identifier
                get { pretty } isEqualTo "das"
            }
            that(tokens.next()) must {
                get { type } isEqualTo TokenType.Identifier
                get { pretty } isEqualTo "sein_123"
                get { position } isEqualTo Position(1, 9, 16)
            }
        }
    }

    @Test
    fun `it should lex all kinds of numbers`() {
        val tokens = lex(
            """
            3
            1337
            3.14
            300_000
            -15
            -139.13
            -1_000_000
        """.trimIndent()
        )
        expect {
            repeat(4) { that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Number } }
            repeat(3) {
                that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Minus }
                that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Number }
            }
        }
    }

    @Test
    fun `it should lex const and var declarations`() {
        val tokens = lex(
            """
            const PI = 3.14
            var radius = 25
        """.trimIndent()
        )

        expect {
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Const }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Equals }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Number }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Var }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Equals }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Number }
        }
    }

    @Test
    fun `it should lex function declarations`() {
        val tokens = lex(
            """
            const PI := 3.14
            const area : number = { r |
                return r * r * PI
            }
            area(13)
        """.trimIndent()
        )

        expect {
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Const }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Colon }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Equals }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Number }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Const }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Colon }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Equals }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.LeftCurly }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Pipe }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Return }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Multiply }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Multiply }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.RightCurly }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Identifier }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.LeftParen }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.Number }
            that(tokens.next()) must { get(Token::type) isEqualTo TokenType.RightParen }
        }
    }
}