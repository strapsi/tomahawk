package de.maxpower.tomahawk.repl

import de.maxpower.tomahawk.evaluator.evaluate
import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.logging.Log
import de.maxpower.tomahawk.parser.parse

object REPL {

    fun prompt() = print("> ")

    fun eval(line: String) {
        try {
            val program = parse(lex(line))
            val result = evaluate(program, null)
            when (result.hasError()) {
                true -> Log.red(result.errorMessage())
                false -> Log.yellow(result.value)
            }
        } catch (ex: Exception) {
            Log.error(ex.message ?: "there was no error message")
        }
        prompt()
    }
}
