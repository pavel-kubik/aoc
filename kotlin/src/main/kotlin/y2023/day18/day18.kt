package y2023.day18

import utils.FileReader
import utils.runWrapper

fun main() {
    val testLines = FileReader.readResourceFile("/day18/test_data.txt")
    val testLines2 = FileReader.readResourceFile("/day18/test_data2.txt")
    val testLines3 = FileReader.readResourceFile("/day18/test_data3.txt")
    val testLines4 = FileReader.readResourceFile("/day18/test_data4.txt")
    val testLines5 = FileReader.readResourceFile("/day18/test_data5.txt")
    val lines = FileReader.readResourceFile("/day18/data.txt")
    runWrapper(23) { part1(testLines3) }
    runWrapper(25) { part1(testLines2) }
    runWrapper(23) { part1(testLines4) }
    runWrapper(37) { part1(testLines5) }
    runWrapper(62) { part1(testLines) }
    runWrapper(47675) { part1(lines) }
    runWrapper(952408144115) { part2(testLines) }
    runWrapper(122103860427465) { part2(lines) }
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


/**
 * test_data3 - R 4, D 1, R 4, D 1, L 8, U 2
 *
 *           |
 *    - R 4 - - - - -
 *    - - - - - R 4 -
 *  S # # # # - - - -
 *  # . . . # # # # #
 *  # # # # # # # # #
 *  + + + + + + + +
 *  + + + + + + + +
 *  + + + L 8 + + +
 *  + + + + + + + +
 *  + + + + + + + +
 */
fun solve(input: List<Pair<String, Long>>): Long {
    var start = Pair(0L, 0L)
    var area = 0L
    input.forEach {
        val (direction, length) = it
        // only horizontal lines affect area
        if (direction == "L") {
            val d = length * (start.second + 1) // area with line included
            area += d
        } else if (direction == "R") {
            val d = - length * start.second // area under line
            area += d
        } else {
            if (direction == "U") { // missing side area
                area += length
            }
        }
        start = start.add(direction, length)
    }
    return 1 + area // +1 for starting point
}

fun part1(lines: List<String>): Long {
    val input = lines.map {
        val (direction, length, _) = it.split(' ')
        Pair(direction, length.toLong())
    }
    return solve(input)
}

fun part2(lines: List<String>): Long {
    val input = lines.map {
        val (_, _, colorRGBHex) = it.split(' ')
        val input = colorRGBHex.removeSurrounding("(#", ")")
        val direction = listOf('R', 'D', 'L', 'U')[input[5].digitToInt()].toString()
        val length = input.substring(0, 5).toLong(radix = 16)
        Pair(direction, length)
    }
    return solve(input)
}

