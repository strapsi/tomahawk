package de.maxpower.tomahawk.evaluator

import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isTrue

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
}