package y2023.day05

import utils.FileReader.Companion.readResourceFile

fun main() {
    val testLines = readResourceFile("/day05/test_data.txt")
    val lines = readResourceFile("/day05/data.txt")
    //part1(testLines)
    //part1(lines)
    part2(testLines)
    //part2(lines)
}

val path = listOf("seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water", "water-to-light", "light-to-temperature", "temperature-to-humidity", "humidity-to-location")

fun part1(lines: List<String>) {
    maps = mutableMapOf()
    val seeds = lines[0].split(": ")[1].split(" ").map { it.toLong() }
    println(seeds)
    var idx = 2
    do {
        val mapName = lines[idx++].split(" map:")[0]
        while (idx < lines.size && lines[idx].isNotBlank()) {
            addRange(mapName, lines[idx].split(" ").map { it.toLong() })
            idx++
        }
        idx++
    } while (idx < lines.size)
    println(maps)
    val locations = seeds.map { seed ->
        var i = seed
        print("$i -> ")
        path.forEach { mapName ->
            i = findMap(mapName, i)
            print("$i -> ")
        }
        println()
        return@map i
    }
    println(locations.minByOrNull { it })
}

fun part2(lines: List<String>) {
    maps = mutableMapOf()
    val seedsPattern = lines[0].split(": ")[1].split(" ").map { it.toLong() }
    val seeds = (0 until seedsPattern.size/2).map {
        Pair(seedsPattern[2*it], seedsPattern[2*it] + seedsPattern[2*it+1] - 1)
    }
    println(seeds)
    var idx = 2
    do {
        val mapName = lines[idx++].split(" map:")[0]
        while (idx < lines.size && lines[idx].isNotBlank()) {
            addRange(mapName, lines[idx].split(" ").map { it.toLong() })
            idx++
        }
        idx++
    } while (idx < lines.size)
    println(maps)
    var stack = ArrayDeque(seeds)
    var stackNext = ArrayDeque<Pair<Long, Long>>()
    path.forEach { mapName ->
        while (stack.isNotEmpty()) {
            val i = stack.removeFirst()
            stackNext.addAll(findMap(mapName, i))
        }
        stack = stackNext
        stackNext = ArrayDeque()
        println(stack)
    }
    println(stack.map { it.first }.sorted())//.minByOrNull { it })
    // 40398634 too low
    // 110055332 too high
    // 75281325 not
}

data class Range(
    val to: Long,
    val from: Long,
    val length: Long,
)

fun Range.contains(value: Long) = value >= from && value < from + length
fun Range.compute(value: Long): Long {
    assert(contains(value))
    return to - from + value
}

fun Range.contains(value: Pair<Long, Long>) =
    contains(value.first) || contains(value.second) ||
            (value.first < from && value.second >= from + length) ||
            (value.first > from && value.second < from + length)

var maps = mutableMapOf<String, MutableList<Range>>()

fun addRange(mapName: String, map: List<Long>) {
    val ranges = maps.computeIfAbsent(mapName) { _ -> mutableListOf() }
    ranges.add(Range(map[0], map[1], map[2]))
}

fun findMap(mapName: String, value: Long): Long {
    return maps[mapName]?.firstOrNull {
        it.contains(value)
    }?.compute(value) ?: value
}

/**
 * Broke interval [value] by all matching ranges on [mapName].
 */
fun findMap(mapName: String, value: Pair<Long, Long>): List<Pair<Long, Long>> {
    val ranges = maps[mapName]!!.filter {
        it.contains(value)
    }
    if (ranges.isEmpty()) return listOf(value)
    var points = mutableListOf(value.first, value.second)
    ranges.forEach {
        points.add(it.from-1)   // out
        points.add(it.from)
        points.add(it.from + it.length)
        points.add(it.from + it.length + 1) // out
    }
    points.sort()
    points = points.filter { it >= value.first && it <= value.second }.toMutableList()
    val newIntervals = (0 until points.size - 1).map {
        Pair(points[it], points[it+1])
    }
    return newIntervals.map {
        Pair(findMap(mapName, it.first), findMap(mapName, it.second))
    }
}