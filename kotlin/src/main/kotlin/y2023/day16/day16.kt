package y2023.day16

import utils.*

fun main() {
    val testLines = FileReader.readResourceFile("/day16/test_data.txt")
    val lines = FileReader.readResourceFile("/day16/data.txt")
    runWrapper { part1(testLines) }
    runWrapper { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

val UP = Pair(0, -1)
val DOWN = Pair(0, 1)
val RIGHT = Pair(1, 0)
val LEFT = Pair(-1, 0)

val bitMask = mapOf(UP to 0b0001, DOWN to 0b0010, RIGHT to 0b0100, LEFT to 0b1000)

private operator fun Pair<Int, Int>.plus(point : Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + point.first, second + point.second)
}

fun getNewDirections(mirrorType: Char, direction: Pair<Int, Int>): List<Pair<Int, Int>> {
    val directionMap = when(mirrorType) {
        '/' -> mapOf(RIGHT to listOf(UP), LEFT to listOf(DOWN), DOWN to listOf(LEFT), UP to listOf(RIGHT))
        '\\' -> mapOf(RIGHT to listOf(DOWN), LEFT to listOf(UP), DOWN to listOf(RIGHT), UP to listOf(LEFT))
        '-' -> mapOf(
            UP to listOf(LEFT, RIGHT),
            DOWN to listOf(LEFT, RIGHT),
            LEFT to listOf(LEFT),
            RIGHT to listOf(RIGHT)
        )
        '|' -> mapOf(LEFT to listOf(UP, DOWN), RIGHT to listOf(UP, DOWN), UP to listOf(UP), DOWN to listOf(DOWN))
        '.' -> mapOf(LEFT to listOf(LEFT), RIGHT to listOf(RIGHT), UP to listOf(UP), DOWN to listOf(DOWN))
        else -> mapOf()
    }
    return directionMap[direction] ?: emptyList()
}

data class Beam(
    var position: Pair<Int, Int>,
    var direction: Pair<Int, Int>,
)

fun Matrix<Char>.beamStep(beam: Beam): List<Beam> {
    beam.position += beam.direction // move
    val item = this[beam.position]
    if (item != null) { // still in mirror maze
        val directions = getNewDirections(item, beam.direction)
        return directions.map {
            beam.copy(direction = it)
        }
    }
    return emptyList()
}

fun part1(lines: List<String>): Int {
    val matrix = createMatrix(lines, '.')
    val firstBeam = Beam(Pair(-1,0), RIGHT)
    var beams = listOf(firstBeam)
    val visited = createMatrix(matrix.width, matrix.height, 0)
    //visited[firstBeam.position] = visited[firstBeam.position]!! + bitMask[firstBeam.direction]!!
    var iterations = 0
    while (beams.isNotEmpty()) {
        beams = beams.map {
            if (visited[it.position + it.direction]?.and(bitMask[it.direction]!!) != 0) {
                //println("Beam repeat itself $it")
                return@map null
            }
            visited[it.position + it.direction] = visited[it.position + it.direction]!! or bitMask[it.direction]!!
            it
        }.filterNotNull()
        beams = beams.map { beam ->
            matrix.beamStep(beam)
        }.flatten()
        if (++iterations > 1000) error("Too much iterations")
    }
    println("Iterations: $iterations\n${visited.toStringHEX()}")
    return visited.map { if (it > 0) 1 else 0 }.sum()
}

fun part2(lines: List<String>) {

}
