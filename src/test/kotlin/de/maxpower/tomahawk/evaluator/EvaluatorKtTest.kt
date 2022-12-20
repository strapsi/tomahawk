package de.maxpower.tomahawk.evaluator

import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.*

class EvaluatorKtTest {
    @Test
    fun `it should evaluate to nothing`() {
        val result = evaluate(parse(lex("")), null)
        expectThat(result).isA<Objects.Nothing>()
    }

    @Test
    fun `it should evaluate "return true"`() {
        val result = evaluate(parse(lex("return true")), null)
        expectThat(result).isA<Objects.Bool>().and { get { value }.isTrue() }
    }

    @Test
    fun `it should evaluate a expression statement`() {
        val result = evaluate(parse(lex("true")), null)
        expectThat(result).isA<Objects.Bool>() and { get { value }.isTrue() }
    }

    @Test
    fun `it should evaluate a "return 13"`() {
        val result = evaluate(parse(lex("return 13")), null)
        expectThat(result).isA<Objects.Number>() and { get { value } isEqualTo 13 }
    }

    @Test
    fun `it should evaluate a "return 13,0" (with a dot)`() {
        val result = evaluate(parse(lex("return 13.0")), null)
        expectThat(result).isA<Objects.Number>() and { get { value } isEqualTo 13.0 }
    }

    @Test
    fun `it should evaluate a "!true"`() {
        val result = evaluate(parse(lex("!true")), null)
        expectThat(result).isA<Objects.Bool>() and { get { value }.isFalse() }
    }

    @Test
    fun `it should evaluate a "-13"`() {
        val result = evaluate(parse(lex("-13")), null)
        expectThat(result).isA<Objects.Number>() and { get { value } isEqualTo -13 }
    }

    @Test
    fun `it should evaluate 10 + 3`() {
        val result = evaluate(parse(lex("10 + 3")), null)
        expectThat(result).isA<Objects.Number>() and { get { value } isEqualTo 13 }
    }


    @Test
    fun `it should evaluate -10 + 3`() {
        val result = evaluate(parse(lex("-10 + 3")), null)
        expectThat(result).isA<Objects.Number>() and { get { value } isEqualTo -7 }
    }


    @Test
    fun `it should evaluate true = false`() {
        val result = evaluate(parse(lex("true = false")), null)
        expectThat(result).isA<Objects.Bool>() and { get { value }.isFalse() }
    }

    @Test
    fun `it should evaluate true = !false`() {
        val result = evaluate(parse(lex("true = !false")), null)
        expectThat(result).isA<Objects.Bool>() and { get { value }.isTrue() }
    }

    @Test
    fun `it should evaluate !true = !false`() {
        val result = evaluate(parse(lex("!true = !false")), null)
        expectThat(result).isA<Objects.Bool>() and { get { value }.isFalse() }
    }

    @Test
    fun `it should evaluate !true != !false`() {
        val result = evaluate(parse(lex("!true != !false")), null)
        expectThat(result).isA<Objects.Bool>() and { get { value }.isTrue() }
    }
}