package de.maxpower.tomahawk

import de.maxpower.tomahawk.lexer.lex
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        println("kann das jetzt vllt sein. ${args.joinToString(",")}")
        var line = readln()
        while (line != "exit") {
            val tokens = lex(line)
            println(tokens)
            line = readln()
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TomahawkApplication>(*args)
}
