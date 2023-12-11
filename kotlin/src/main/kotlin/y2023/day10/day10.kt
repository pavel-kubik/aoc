package y2023.day10

import utils.FileReader
import utils.runWrapper
import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    val testLines = FileReader.readResourceFile("/day10/test_data.txt")
    val testLines_2_1 = FileReader.readResourceFile("/day10/test_data_2_1.txt")
    val testLines_2_2 = FileReader.readResourceFile("/day10/test_data_2_2.txt")
    val testLines_2_3 = FileReader.readResourceFile("/day10/test_data_2_3.txt")
    val lines = FileReader.readResourceFile("/day10/data.txt")
    runWrapper(4) { part1(testLines) }
    runWrapper(6820) { part1(lines) }
    runWrapper(4) { part2(testLines_2_1) }
    runWrapper(8) { part2(testLines_2_2) }
    runWrapper(10) { part2(testLines_2_3) }
    runWrapper(337) { part2(lines) }
}

private typealias Point2Di = Pair<Int, Int>

private operator fun Point2Di.plus(point : Point2Di): Point2Di {
    return Point2Di(first + point.first, second + point.second)
}

private fun List<String>.get(xy: Point2Di) = this[xy.second][xy.first]

val UP = Point2Di(0, -1)
val DOWN = Point2Di(0, 1)
val RIGHT = Point2Di(1, 0)
val LEFT = Point2Di(-1, 0)

val direction = mapOf(
    '|' to listOf(UP, DOWN),
    '-' to listOf(LEFT, RIGHT),
    'L' to listOf(UP, RIGHT),
    'J' to listOf(UP, LEFT),
    '7' to listOf(DOWN, LEFT),
    'F' to listOf(DOWN, RIGHT),
    'S' to null, // direction is different for each map based on neighbours
)

fun findAnimal(lines: List<String>): Point2Di {
    for (y in lines.indices) {
        for (x in 0 until lines[y].length) {
            if (lines[y][x] == 'S') {
                return Pair(x, y)
            }
        }
    }
    error("Animal not fond")
}

fun getDirectionsForStart(xy: Point2Di, map: List<String>): List<Point2Di> {
    val out = mutableListOf<Point2Di>()
    if (direction[map.get(xy + UP)]?.contains(DOWN) == true) {  // TODO compilation error without `== true`
        out.add(UP)
    }
    if (direction[map.get(xy + DOWN)]?.contains(UP) == true) {
        out.add(DOWN)
    }
    if (direction[map.get(xy + LEFT)]?.contains(RIGHT) == true) {
        out.add(LEFT)
    }
    if (direction[map.get(xy + RIGHT)]?.contains(LEFT) == true) {
        out.add(RIGHT)
    }
    return out
}

fun part1(lines: List<String>): Int {
    val dim = lines[0].length + 2
    val map = listOf(".".repeat(dim)) + lines.map { ".$it." } + listOf(".".repeat(dim))
    //println(map.reduce { acc, s -> "$acc\n$s" })
    val start = findAnimal(map)
    val dist = Array(map.size) { Array(dim) { -1 } }
    val queue = LinkedList(listOf(Pair(start, 0)))
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst() // bfs
        val (x, y) = node.first
        if (dist[y][x] != -1) {
            continue // visited
        }
        dist[y][x] = node.second
        // add next step
        val directions = direction[map[y][x]] ?: getDirectionsForStart(node.first, map)
        for (d in directions) {
            val (newX, newY) = Pair(x + d.first, y + d.second)
            if (dist[newY][newX] != -1) continue
            if (map[newY][newX] == '.') continue
            queue.add(Pair(Pair(newX, newY), node.second + 1))
        }
    }
    return  dist.map { it.maxOf { it } }.maxOf { it }
}

fun part2(lines: List<String>): Int {
    val dim = lines[0].length + 2
    val map = listOf(".".repeat(dim)) + lines.map { ".$it." } + listOf(".".repeat(dim))
    //println(map.reduce { acc, s -> "$acc\n$s" })
    val start = findAnimal(map)
    val dist = Array(map.size) { Array(dim) {-1} }
    val queue = LinkedList(listOf(Pair(start, 0)))
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        if (dist[node.first.second][node.first.first] != -1) {
            continue // visited
        }
        val (x, y) = node.first
        dist[y][x] = node.second
        // add next step
        val directions = direction[map[y][x]] ?: getDirectionsForStart(node.first, map)
        for (d in directions) {
            val (newX, newY) = Pair(x + d.first, y + d.second)
            if (dist[newY][newX] != -1) continue
            if (map[newY][newX] == '.') continue
            queue.add(Pair(Pair(newX, newY), node.second + 1))
        }
    }
    // flood fill
    val queue2 = LinkedList<Pair<Int, Int>>()
    queue2.add(Pair(1, 1))
    val processed = mutableSetOf<Point2Di>()
    while (queue2.isNotEmpty()) {
        val node = queue2.removeLast()
        if (processed.contains(node)) {
            //error("Loop $processed") // TODO it should not happen
        } else {
            processed.add(node)
        }
        val (x, y) = node
        if (dist[y][x] == -1) {
            dist[y][x] = -2
            for (direction in listOf(DOWN, RIGHT, UP, LEFT)) {
                val (newX, newY) = node + direction
                if (newX >= 0 && newY >= 0 && newY < dist.size && newX < dist[0].size) {
                    if (dist[newY][newX] != -1) continue
                    queue2.add(Pair(newX, newY))
                }
            }
        }
    }
    // count parity of tubes for each line to know if points are inside or outside
    for (i in dist.indices) {
        var inside = false
        for (j in dist[i].indices) {
            if (dist[i][j] >= 0 && listOf('|', 'L', 'J', 'S').contains(map[i][j])) {
                inside = !inside
            }
            if (dist[i][j] == -1 && !inside) {
                dist[i][j] = -2
            }
        }
    }
    return dist.map { it.map { if (it == -1) 1 else 0 }.sum() }.sum()// - 2*max
}
