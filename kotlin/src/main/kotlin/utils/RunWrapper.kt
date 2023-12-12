package utils

import java.math.RoundingMode
import kotlin.system.measureNanoTime

fun <R> runWrapper(expectedOutput: R? = null, fce: (Unit) -> R): R {
    val out: R
    val timeInMillis = measureNanoTime {
        out = fce(Unit)
    }.let {
        (it / 1000000.0).toBigDecimal().setScale(2, RoundingMode.HALF_DOWN)
    }
    if (out != Unit) {
        if (out != expectedOutput && expectedOutput != null) {
            println("$out BUT expected $expectedOutput")
        } else {
            println(out)
        }
    }
    println("Duration $timeInMillis ms")
    println()
    return out
}

suspend fun <R> runWrapperS(expectedOutput: R? = null, fce: suspend (Unit) -> R): R {
    val out: R
    val timeInMillis = measureNanoTime {
        out = fce(Unit)
    }.let {
        (it / 1000000.0).toBigDecimal().setScale(2, RoundingMode.HALF_DOWN)
    }
    if (out != Unit) {
        if (out != expectedOutput && expectedOutput != null) {
            println("$out BUT expected $expectedOutput")
        } else {
            println(out)
        }
    }
    println("Duration $timeInMillis ms")
    println()
    return out
}
