package de.maxpower.tomahawk.lexer

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*

class TokenListTest {

    @Test
    fun `isEmpty should return true if the TokenList is empty`() {
        expectThat(TokenList())
            .get(TokenList::isEmpty)
            .isTrue()
    }

    @Test
    fun `addToken should add a token to the the TokenList`() {
        val tokenList = TokenList().apply {
            addToken(TokenType.Pipe, 1, 0, 0)
        }
        expectThat(tokenList).get(TokenList::isEmpty).isFalse()
    }

    @Test
    fun `nextToken should yield "the next" element until all elements were returned`() {
        val tokenList = TokenList().apply {
            addToken(TokenType.Pipe, 1, 0, 0)
        }
        expectThat(tokenList) {
            get(TokenList::next).isNotNull()
            get(TokenList::next).isNull()
        }
    }

    @Test
    fun `addToken should add a token with a dynamic text`() {
        val tokenList = TokenList().apply {
            addToken(TokenType.Identifier, 1, 0, 2, "foo")
        }
        expectThat(tokenList.next()) {
            isNotNull().and { get(Token::pretty) isEqualTo "foo" }
        }
    }
}