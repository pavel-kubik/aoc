package utils

import kotlin.system.measureTimeMillis

fun <R> runWrapper(expectedOutput: R? = null, fce: (Unit) -> R): R {
    val out: R
    val timeInMillis = measureTimeMillis {
        out = fce(Unit)
    }
    if (out != expectedOutput) {
        println("$out BUT expected $expectedOutput")
    } else {
        println(out)
    }
    println("Duration $timeInMillis ms")
    println()
    return out
}