package y2023.day18

import utils.FileReader
import utils.Matrix
import utils.runWrapper

fun main() {
    val testLines = FileReader.readResourceFile("/day18/test_data.txt")
    val lines = FileReader.readResourceFile("/day18/data.txt")
    runWrapper(62) { part1(testLines) }
//    runWrapper(47675) { part1(lines) }
//    runWrapper(47675) { part2(lines) }
//    runWrapper { part2(testLines) }
//    runWrapper { part2(lines) }
}

fun Pair<Int, Int>.add(direction: String, length: Int): Pair<Int, Int> {
    return if (direction in listOf("R", "L")) {
        Pair(
            this.first + if (direction == "R") length else -length,
            this.second
        )
    } else {
        Pair(
            this.first,
            this.second + if (direction == "D") length else -length
        )
    }
}

fun Pair<Long, Long>.add(direction: String, length: Long): Pair<Long, Long> {
    return if (direction in listOf("R", "L")) {
        Pair(
            this.first + if (direction == "R") length else -length,
            this.second
        )
    } else {
        Pair(
            this.first,
            this.second + if (direction == "D") length else -length
        )
    }
}

private operator fun Pair<Int, Int>.plus(point : Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + point.first, second + point.second)
}

fun sign(a: Int, b: Int): Int = if (a - b > 0) 1 else if (a == b) 0 else -1

fun Matrix<Int>.addLine(from: Pair<Int, Int>, to: Pair<Int, Int>, color: Int) {
    val diff = Pair(sign(to.first, from.first), sign(to.second, from.second))
    var current = from.copy()
    while (current != to) {
        this[current] = color
        current += diff
    }
}

fun part1(lines: List<String>): Long {
    val size = 10L
    var start = Pair(0L, 0L)
    var area = 0L
    lines.forEach {
        val (direction, length, _) = it.split(' ')
        // only horizontal lines affect area
        if (direction == "R") {
            val l = length.toLong() - 1
            val h = size - start.second
            area += l*h
            println("$l @ $h = ${l*h}")
        } else if (direction == "L") {
            val l = length.toLong()
            val h = size - start.second - 1
            area -= l*h
            println("$l @ $h = -${l*h}")
        } else {
            area += start.second - 1
        }
        start = start.add(direction, length.toLong())
    }
    return area
}

fun part2(lines: List<String>): Long {
    var start = Pair(100L, 100L)
    var area = 0L
    lines.forEach {
        val (_, _, colorRGBHex) = it.split(' ')
        val input = colorRGBHex.removeSurrounding("(#", ")")
        val direction = listOf('R', 'D', 'L', 'U')[input[5].digitToInt()].toString()
        val length = input.substring(0, 5).toLong(radix = 16)
        start = start.add(direction, length)
        // only horizontal lines affect area
        if (direction == "R") {
            area += length*start.second
        } else if (direction == "L") {
            area -= length*start.second
        }
    }
    return area
}
