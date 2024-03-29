package y2023.day16

import utils.*
import com.squareup.gifencoder.GifEncoder
import com.squareup.gifencoder.ImageOptions
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.io.path.outputStream


fun main() {
    val testLines = FileReader.readResourceFile("/day16/test_data.txt")
    val lines = FileReader.readResourceFile("/day16/data.txt")
    runWrapper { part1(testLines) }
    runWrapper { part1(lines, false) }
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
    val color: Int = colorPlatte[(0..255).random()],
)

val colorPlatte = Array(256) { (0..255*255*255).random() }

const val ratio = 4

fun Matrix<Char>.beamStep(beam: Beam): List<Beam> {
    beam.position += beam.direction // move
    val item = this[beam.position]
    if (item != null) { // still in mirror maze
        val directions = getNewDirections(item, beam.direction)
        return directions.map {
                if (directions.size > 1) {
                    beam.copy(
                        direction = it,
                        color = colorPlatte[(0..255).random()]
                    )
                } else {
                    beam.copy(direction = it)
                }
            }
    }
    return emptyList()
}

fun countEnergizes(lines: List<String>, initialBeam: Beam, animate: Boolean = false): Int {
    var gif: GifEncoder? = null
    if (animate) {
        // https://genuinecoder.com/how-to-create-gif-from-multiple-images-in-java/
        val outputStream = Path("day16.gif").outputStream()

        gif = GifEncoder(outputStream, ratio*lines.first().length, ratio*lines.size, 0)
    }
    val options = ImageOptions() //.also { it.setDelay(10, TimeUnit.MILLISECONDS) }

    val matrix = createMatrix(lines, '.')
    var beams = listOf(initialBeam)
    val visited = createMatrix(matrix.width, matrix.height, 0)
    var image: Array<IntArray>? = null
    if (animate) {
        image = Array(ratio*matrix.height) { IntArray(ratio*matrix.width) { 0 } }
    }
    //visited[firstBeam.position] = visited[firstBeam.position]!! + bitMask[firstBeam.direction]!!
    var iterations = 0
    while (beams.isNotEmpty()) {
        beams = beams.map {
            val nextPosition = it.position + it.direction
            if (visited[nextPosition]?.and(bitMask[it.direction]!!) != 0) {
                //println("Beam repeat itself $it")
                return@map null
            }
            visited[nextPosition] = visited[nextPosition]!! or bitMask[it.direction]!!
            if (animate) {
                (1 until ratio).map { i ->
                    (1 until ratio).map { j ->
                        image!![ratio*nextPosition.second + i][ratio*nextPosition.first + j] = it.color
                    }
                }
            }
            it
        }.filterNotNull()
        beams = beams.map { beam ->
            matrix.beamStep(beam)
        }.flatten()
        if (animate) gif?.addImage(image, options)
        if (++iterations > 10000) error("Too much iterations")
    }
    //println("Iterations: $iterations") //\n${visited.toStringHEX()}")
    if (animate) gif?.finishEncoding() //Start the encoding
    return visited.map { if (it > 0) 1 else 0 }.sum()
}

fun part1(lines: List<String>, animate: Boolean = false): Int {
    return countEnergizes(lines, Beam(Pair(-1, 0), RIGHT), animate)
}

fun part2(lines: List<String>): Int {
    val allEnergies =
        lines.indices.map { countEnergizes(lines, Beam(Pair(-1, it), RIGHT)) } +
        lines.indices.map { countEnergizes(lines, Beam(Pair(lines.first().length, it), LEFT)) } +
        lines.first().indices.map { countEnergizes(lines, Beam(Pair(it, -1), DOWN)) } +
        lines.first().indices.map { countEnergizes(lines, Beam(Pair(it, lines.size), UP)) }
    return  allEnergies.maxOf { it }
}
