package y2023.day10

import utils.FileReader
import utils.runWrapper
import java.util.*

fun main() {
    val testLines = FileReader.readResourceFile("/day10/test_data.txt")
    val lines = FileReader.readResourceFile("/day10/data.txt")
    runWrapper { part1(testLines) }
    runWrapper { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

val UP = Pair(0, -1)
val DOWN = Pair(0, 1)
val RIGHT = Pair(1, 0)
val LEFT = Pair(-1, 0)

val direction = mapOf(
    '|' to listOf(UP, DOWN),
    '-' to listOf(LEFT, RIGHT),
    'L' to listOf(UP, RIGHT),
    'J' to listOf(UP, LEFT),
    '7' to listOf(DOWN, LEFT),
    'F' to listOf(DOWN, RIGHT),
    'S' to listOf(UP, DOWN /*, LEFT, RIGHT*/),
)

fun findAnimal(lines: List<String>): Pair<Int, Int> {
    for (y in 0 until lines.size) {
        for (x in 0 until lines[y].length) {
            if (lines[y][x] == 'S') {
                return Pair(x, y)
            }
        }
    }
    error("Animal not fond")
}

fun part1(lines: List<String>): Int {
    val dim = lines[0].length + 2
    val map = listOf(".".repeat(dim)) + lines.map { ".$it." } + listOf(".".repeat(dim))
    //println(map.reduce { acc, s -> "$acc\n$s" })
    val start = findAnimal(map)
    val dist = Array(map.size) { Array(dim) {-1} }
    val queue = LinkedList<Pair<Pair<Int, Int>, Int>>()
    queue.add(Pair(start, 0))
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        if (dist[node.first.second][node.first.first] != -1) {
            continue // visited
        }
        val (x, y) = node.first
        dist[y][x] = node.second
        // add next step
        val directions = direction[map[y][x]] ?: continue
        for (d in directions) {
            val (newX, newY) = Pair(x + d.first, y + d.second)
            if (dist[newY][newX] != -1) continue
            if (map[newY][newX] == '.') continue
            queue.add(Pair(Pair(newX, newY), node.second + 1))
        }
    }
    dist.map {
        it.map { print(" ${if (it == -1) "   ." else it.toString().padStart(4, '0')} ") }
        println()
    }
    // remove dead end pipes
    val max = dist.map { it.maxOf { it } }.maxOf { it }
    // 6819 too low
    // 13637 too high
    return max
}

fun part2(lines: List<String>) {

}
