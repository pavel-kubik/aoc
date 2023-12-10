package y2023.day10

import utils.FileReader
import utils.runWrapper
import java.util.*

fun main() {
    val testLines = FileReader.readResourceFile("/day10/test_data.txt")
    val lines = FileReader.readResourceFile("/day10/data.txt")
    //runWrapper { part1(testLines) }
    //runWrapper { part1(lines) }
    runWrapper(8) { part2(testLines) }
    //runWrapper { part2(lines) }
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
//    dist.map {
//        it.map { print(" ${if (it == -1) "   ." else it.toString().padStart(4, '0')} ") }
//        println()
//    }
    // remove dead end pipes
    val max = dist.map { it.maxOf { it } }.maxOf { it }
    // 6819 too low
    // 13637 too high
    return max
}

fun part2(lines: List<String>): Int {
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
    val queue2 = LinkedList<Pair<Int, Int>>()
    queue2.add(Pair(1, 1))
    var iters = 0
    while (queue2.isNotEmpty()) {
        val node = queue2.removeLast()
        val (x, y) = node
        if (dist[y][x] == -1) {
            dist[y][x] = -2
        }
        for (d in listOf(DOWN, RIGHT, UP, LEFT)) {
            val (newX, newY) = Pair(x + d.first, y + d.second)
            if (newX >= 0 && newY >= 0 && newY < dist.size && newX < dist[0].size) {
                if (dist[newY][newX] != -1) continue
                queue2.add(Pair(newX, newY))
            }
        }
    }
    val max = dist.map { it.maxOf { it } }.maxOf { it }
    println(max)
    val neigbours = listOf(Pair(-1, -1), Pair(0, -1), Pair(1, -1),
                            Pair(-1, 0), Pair(1, 0),
                            Pair(-1, 1), Pair(0, 1), Pair(1, 1))
    for (i in 0 until dist.size) {
        for (j in 0 until dist[0].size) {
            if (dist[i][j] == -1) {
                if (neigbours.any {
                        val newX = j + it.first
                        val newY = i + it.second
                        if (newX >= 0 && newY >= 0 && newY < dist.size && newX < dist[0].size) {
                            dist[newY][newX] == -2
                        } else false
                }) {
                    dist[i][j] = -3
                }
            }
        }
    }
    // one more removal of fake points inside
    for (i in 0 until dist.size) {
        var inside = false
        for (j in 0 until dist[0].size) {
            if (listOf('|', 'S', 'L', 'J', 'F', '7').contains(map[i][j])) {
                inside = !inside
            }
            if (dist[i][j] == -1 && !inside) {
                //dist[i][j] = -4
            }
        }
    }

    dist.map {
        it.map { print(" ${if (it == -1) "   ." else it.toString().padStart(4, '0')} ") }
        println()
    }
    return dist.map { it.map { if (it == -1) 1 else 0 }.sum() }.sum()// - 2*max
    // 6524 too high
    // 630
    // 586 - 4
    // 346
    // 574
    // 14214 -
}
