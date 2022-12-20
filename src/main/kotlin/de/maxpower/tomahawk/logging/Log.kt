package de.maxpower.tomahawk.logging

import org.slf4j.LoggerFactory

object Log {
    private val logger = LoggerFactory.getLogger("Tomahawk")
    private const val Reset = "\u001B[0m"
    private const val Black = "\u001B[30m"
    private const val Red = "\u001B[31m"
    private const val Green = "\u001B[32m"
    private const val Yellow = "\u001B[33m"
    private const val Blue = "\u001B[34m"
    private const val Purple = "\u001B[35m"
    private const val Cyan = "\u001B[36m"
    private const val White = "\u001B[37m"

    fun error(message: String) = logger.error(message)
    fun write(message: Any?, color: String) = println("$color$message$Reset")
    fun yellow(message: Any?) = write(message, Yellow)
    fun red(message: Any?) = write(message, Red)
}