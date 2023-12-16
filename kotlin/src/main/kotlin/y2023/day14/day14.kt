package y2023.day14

import utils.FileReader
import utils.Matrix
import utils.createMatrix
import utils.runWrapper

fun main() {
    val testLines = FileReader.readResourceFile("/day14/test_data.txt")
    val lines = FileReader.readResourceFile("/day14/data.txt")
    runWrapper(136) { part1(testLines) }
    runWrapper(110128) { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

private operator fun Pair<Int, Int>.plus(point : Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + point.first, second + point.second)
}

// TODO make it nicer and move it to Matrix class
fun Matrix<Char>.move(movable: Char, empty: Char, direction: Pair<Int, Int>) {
    if (direction.first <= 0 && direction.second <= 0) {
        (0 until this.height).forEach { row ->
            (0 until this.width).forEach { column ->
                val item = this[row, column]
                if (item == movable) {
                    // rolling rock
                    val from = Pair(row, column)
                    var nextPosition = from
                    while (this[nextPosition + direction] == empty) {
                        nextPosition += direction
                    }
                    if (nextPosition != from) {
                        // move rock
                        this[from] = empty
                        this[nextPosition] = movable
                    }
                }
            }
        }
    } else if (direction.first >= 0 && direction.second >= 0) {
        (0 until this.width).reversed().forEach { column ->
            (0 until this.height).reversed().forEach { row ->
                val item = this[row, column]
                if (item == movable) {
                    // rolling rock
                    val from = Pair(row, column)
                    var nextPosition = from
                    while (this[nextPosition + direction] == empty) {
                        nextPosition += direction
                    }
                    if (nextPosition != from) {
                        // move rock
                        this[from] = empty
                        this[nextPosition] = movable
                    }
                }
            }
        }
    } else error("Not implemented")
}

fun part1(lines: List<String>): Any {
    val matrix = createMatrix(lines, '.')
    matrix.move('O', '.', Pair(-1, 0))
    return matrix.toString().split("\n").reversed().mapIndexed { index, line ->
        (index+1)*line.count { it == 'O' }
    }.sum()
}

fun part2(lines: List<String>): Int {
    val matrix = createMatrix(lines, '.')
    val series = mutableListOf<Int>()
    val samples = 1000
    repeat(samples) {
        matrix.move('O', '.', Pair(-1, 0))
        matrix.move('O', '.', Pair(0, -1))
        matrix.move('O', '.', Pair(1, 0))
        matrix.move('O', '.', Pair(0, 1))
        series.add(matrix.toString().split("\n").reversed().mapIndexed { index, line ->
            (index+1)*line.count { it == 'O' }
        }.sum())
        //println("$matrix\n")
    }
    println(series)
    val lastNum = series.removeLast()
    val previous = series.indexOfLast { it == lastNum }
    val period = samples - 1 - previous
    println("Period is $period")

    val index = (1000000000 - previous) % period
    return series[previous + index - 1]
}
