package de.maxpower.tomahawk

import de.maxpower.tomahawk.evaluator.evaluate
import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


val logger = LoggerFactory.getLogger("Tomahawk")

object LOG {
    private const val RESET = "\u001B[0m"
    private const val BLACK = "\u001B[30m"
    private const val RED = "\u001B[31m"
    private const val GREEN = "\u001B[32m"
    private const val YELLOW = "\u001B[33m"
    private const val BLUE = "\u001B[34m"
    private const val PURPLE = "\u001B[35m"
    private const val CYAN = "\u001B[36m"
    private const val WHITE = "\u001B[37m"

    fun yellow(msg: Any?) {
        println("$YELLOW$msg$RESET")
    }
}

@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        print("> ")
        var line = readln()
        while (line != "exit") {
            try {
                val program = parse(lex(line))
                val result = evaluate(program, null)
                LOG.yellow(result.value)
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
