package de.maxpower.tomahawk

import de.maxpower.tomahawk.evaluator.evaluate
import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    private val logger = LoggerFactory.getLogger("Tomahawk")
    override fun run(vararg args: String?) {
        print("> ")
        var line = readln()
        while (line != "exit") {
            try {
                val program = parse(lex(line))
                val result = evaluate(program, null)
                println(result.value)
            } catch (ex: Exception) {
                logger.error(ex.message)
            }
            print("> ")
            line = readln()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TomahawkApplication>(*args)
}
