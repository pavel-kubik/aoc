package y2023.day18

import utils.FileReader
import utils.Matrix
import utils.createMatrix
import utils.runWrapper

fun main() {
    val testLines = FileReader.readResourceFile("/day18/test_data.txt")
    val lines = FileReader.readResourceFile("/day18/data.txt")
    //runWrapper(62) { part1(testLines) }
    runWrapper { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
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

const val SIZE = 1000 // TODO parse data to count it

fun part1(lines: List<String>): Int {
    var start = Pair(SIZE/2, SIZE/2)
    val matrix = createMatrix(SIZE, SIZE, 0)
    lines.forEach {
        val (direction, length, colorRGBHex) = it.split(' ')
        val destination = start.add(direction, length.toInt())
        val color = colorRGBHex.removeSurrounding("(#", ")").toInt(radix = 16)
        matrix.addLine(start, destination, color)
        start = destination
    }
//    println(matrix.toString() {
//        if (it?.let {it > 0} == true) "#" else "."
//    })
//    println()

    (0 until matrix.height).forEach { row ->
        var inside = false
        var edge = false
        var up = false
        var down = false
        (0 until matrix.width).forEach { col ->
            val item = matrix[row, col]
            if (item == 0) { // '.'
                if (edge) {
                    edge = false
                    if (down && (matrix[row - 1, col - 1]!! <= 1 && matrix[row + 1, col - 1]!! > 1) ||
                            up && (matrix[row + 1, col - 1]!! <= 1 && matrix[row - 1, col - 1]!! > 1)) {
                        // don't change inside
                    } else {
                        inside = !inside
                    }
                }
                if (inside) {
                    matrix[row, col] = 1
                }
            } else { // '#'
                if (!edge) {
                    edge = true
                    up = false
                    down = false
                    if (matrix[row - 1, col]!! <= 1 && matrix[row + 1, col]!! > 1) down = true
                    if (matrix[row + 1, col]!! <= 1 && matrix[row - 1, col]!! > 1) up = true
                }
            }
        }
    }
    println(matrix.toString() {
        if (it?.let {it > 0} == true) "#" else "."
    })
    return matrix.map { it }.filter { it > 0 }.count()
    // 56165 too high
}

fun part2(lines: List<String>) {

}
