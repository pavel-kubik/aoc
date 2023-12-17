package y2023.day17

import utils.*
import java.util.*

fun main() {
    val testLines = FileReader.readResourceFile("/day17/test_data.txt")
    val testLines2= FileReader.readResourceFile("/day17/test_data2.txt")
    val lines = FileReader.readResourceFile("/day17/data.txt")
    runWrapper(102) { part1(testLines) }
    runWrapper(742) { part1(lines) }
    runWrapper(94) { part2(testLines) }
    runWrapper(71) { part2(testLines2) }
    runWrapper(918) { part2(lines) }
}

val UP = Pair(0, -1)
val DOWN = Pair(0, 1)
val RIGHT = Pair(1, 0)
val LEFT = Pair(-1, 0)

val order = listOf(UP, DOWN, RIGHT, LEFT)

val turn = mapOf(
    UP to listOf(LEFT, RIGHT),
    DOWN to listOf(LEFT, RIGHT),
    LEFT to listOf(UP, DOWN),
    RIGHT to listOf(UP, DOWN),
)

private operator fun Pair<Int, Int>.plus(point : Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + point.first, second + point.second)
}

data class Step(
    val xy: Pair<Int, Int>,
    val direction: Pair<Int, Int>,
    val sameDirection: Int,
    var loss: Int,
    val history: List<Pair<Int, Int>>
)

fun solve(lines: List<String>, maxStepsStraight: Int, minStepsTurn: Int): Int {
    val matrix = createMatrix(lines, 0, MatrixType.ARRAY_MATRIX) {
        it.digitToInt()
    }
    val visited = createMatrix(matrix.width, matrix.height, Array(4*maxStepsStraight) { Int.MAX_VALUE }, MatrixType.ARRAY_MATRIX)
    //println(matrix.toStringHEX())
    val queue = PriorityQueue<Step>() { s1, s2 ->
        s1.loss - s2.loss // comparator - 842 steps
        //s2.history.size - s1.history.size // comparator - 38137 steps with cut higher than best,  66591 steps
        //s2.xy.first + s2.xy.second - s1.xy.first + s1.xy.second // comparator - 5321 steps
    }.also {
        it.add(Step(Pair(1, 0), RIGHT, 0, 0, mutableListOf(Pair(0, 0))))
    }
    var steps = 0
    var best = Int.MAX_VALUE
    while (queue.isNotEmpty()) {
        if (++steps > 100000000) {
            println("Stop cycling")
            break
        }
        val step = queue.poll()
        if (matrix[step.xy] != null) { // inside matrix
            step.loss += matrix[step.xy]!!
            // cut wrong solutions
            if (step.loss > best) {
                continue
            }

            //println("Steps for $step")
            if (step.sameDirection >= minStepsTurn - 1 && step.xy.first == matrix.width - 1 && step.xy.second == matrix.height - 1) {
                println("Solution: ${step.loss} with path ${step}")
                if (step.loss < best) {
                    best = step.loss
                }
                continue
            }

            // don't visit point two times
            val directionOrder = order.indexOf(step.direction)
            val visitedArray = visited[step.xy]!!
            if (visitedArray[maxStepsStraight*directionOrder+step.sameDirection] <= step.loss) {
                continue
            }
            visited[step.xy] = visitedArray.clone().also { it[maxStepsStraight*directionOrder+step.sameDirection] = step.loss }

            if (step.sameDirection < maxStepsStraight - 1) {
                // same direction
                val newStep = step.copy(
                    xy = step.xy + step.direction,
                    sameDirection = step.sameDirection + 1,
                    history = step.history + step.xy
                )
                //println(" - new $newStep")
                queue.add(newStep)
            }
            if (step.sameDirection >= minStepsTurn - 1) {
                turn[step.direction]!!.forEach {
                    // change direction
                    val newStep = step.copy(
                        xy = step.xy + it,
                        sameDirection = 0,
                        direction = it,
                        history = step.history + step.xy
                    )
                    //println(" - new $newStep")
                    queue.add(newStep)
                }
            }
        }
    }
    println("Found in $steps")
    return best
}

fun part1(lines: List<String>): Int {
    return solve(lines, 3, 1)
}

fun part2(lines: List<String>): Int {
    return solve(lines, 10, 4)
}
