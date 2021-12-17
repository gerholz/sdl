package de.datalab

import de.datalab.sdl.example.AppDatabase
import de.datalab.sdl.example.ObjectService
import de.datalab.sdl.generator.JavaGenerator

fun main(args: Array<String>) {
    val appDatabase = AppDatabase()
    val objectService = ObjectService()
    val javaGenerator = JavaGenerator(appDatabase)
    javaGenerator.generate()
    println("Hello, World")
}

