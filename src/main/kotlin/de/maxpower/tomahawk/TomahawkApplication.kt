package de.maxpower.tomahawk

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TomahawkApplication : CommandLineRunner {
    override fun run(vararg args: String?) {
        println("kann das jetzt vllt sein. ${args.joinToString(",")}")
        val line = readln()
        println("someday i can run this... $line")
    }
}

fun main(args: Array<String>) {
    runApplication<TomahawkApplication>(*args)
}
