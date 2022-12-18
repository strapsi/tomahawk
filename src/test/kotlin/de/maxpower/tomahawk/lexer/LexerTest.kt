package de.maxpower.tomahawk.lexer

import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.*

class LexerTest {

    @Test
    fun `it should return a list of tokens`() {
        val tokens = Lexer.lex("")
        expectThat(tokens) {
            get(TokenList::isEmpty).isTrue()
        }
    }

    @Test
    fun `it should lex all single char special characters`() {
        val tokens = Lexer.lex(" | = , + - * / ( ) [ ] { } < > :")
        expect {
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Pipe }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Equals }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Comma }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Plus }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Minus }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Multiply }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Divide }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.LeftBrace }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.RightBrace }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.LeftBracket }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.RightBracket }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.LeftCurly }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.RightCurly }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.LessThan }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.GreaterThan }
            that(tokens.next()).isNotNull().and { get(Token::type) isEqualTo TokenType.Colon }
            that(tokens.next()).isNull()
        }
    }

    @Test
    fun `it should provide infos about the position of a token`() {

    }
}