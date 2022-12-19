package de.maxpower.tomahawk

import de.maxpower.tomahawk.lexer.lex
import de.maxpower.tomahawk.parser.parse
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        var line = readln()
        println("the line was $line")
        while (line != "exit") {
            val program = parse(lex(line))
            program.statements.forEach {
                println(it)
            }
            line = readln()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TomahawkApplication>(*args)
}
