package de.maxpower.tomahawk

import de.maxpower.tomahawk.evaluator.evaluate
import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import de.maxpower.tomahawk.repl.REPL
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.suspendCoroutine




@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        // read file and execute
        if (args.firstOrNull()?.isNotBlank() == true) {
            // load file
        } else {
            // REPL
            REPL.prompt()
            var line = readln()
            while (line != "exit" && line != ".e") {
                REPL.eval(line)
                line = readln()
            }

        }
    }
}

fun main(args: Array<String>) {
    runApplication<TomahawkApplication>(*args)
}
