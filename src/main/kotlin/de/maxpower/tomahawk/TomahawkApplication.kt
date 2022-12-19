package de.maxpower.tomahawk

import de.maxpower.tomahawk.evaluator.evaluate
import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        var line = readln()
        while (line != "exit") {
            val program = parse(lex(line))
            val result = evaluate(program, null)
            println(result.value)
            line = readln()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TomahawkApplication>(*args)
}
