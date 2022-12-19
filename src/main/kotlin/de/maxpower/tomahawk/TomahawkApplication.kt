package de.maxpower.tomahawk

import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        println("kann das jetzt vllt sein. ${args.joinToString(",")}")
        var line = readln()
        while (line != "exit") {
            val program = parse(lex(line))
            program.statements.forEach(::println)
            line = readln()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TomahawkApplication>(*args)
}
