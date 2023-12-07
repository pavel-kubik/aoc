package y2023.day05

import utils.FileReader.Companion.readResourceFile
import utils.runWrapper
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testLines = readResourceFile("/day05/test_data.txt")
    val lines = readResourceFile("/day05/data.txt")
    runWrapper(35) { part1(testLines) }
    runWrapper(157211394) { part1(lines) }
    runWrapper(46) { part2(testLines) }
    runWrapper(50855035) { part2(lines) }
}

val path = listOf("seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water", "water-to-light", "light-to-temperature", "temperature-to-humidity", "humidity-to-location")

var maps = mutableMapOf<String, MutableList<Range>>()

fun part1(lines: List<String>) {
    maps = mutableMapOf()
    val seeds = lines[0].split(": ")[1].split(" ").map { it.toLong() }
    var idx = 2
    do {
        val mapName = lines[idx++].split(" map:")[0]
        while (idx < lines.size && lines[idx].isNotBlank()) {
            addRange(mapName, lines[idx].split(" ").map { it.toLong() })
            idx++
        }
        idx++
    } while (idx < lines.size)
    val locations = seeds.map { seed ->
        var i = seed
        path.forEach { mapName ->
            i = findMap(mapName, i)
        }
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
    var idx = 2
    do {
        val mapName = lines[idx++].split(" map:")[0]
        while (idx < lines.size && lines[idx].isNotBlank()) {
            addRange(mapName, lines[idx].split(" ").map { it.toLong() })
            idx++
        }
        idx++
    } while (idx < lines.size)
    var stack = ArrayDeque(seeds)
    var stackNext = ArrayDeque<Pair<Long, Long>>()
    path.forEach { mapName ->
        while (stack.isNotEmpty()) {
            val i = stack.removeFirst()
            stackNext.addAll(findMap(mapName, i))
        }
        stack = stackNext
        stackNext = ArrayDeque()
    }
    println(stack.map { it.first }.sorted().minByOrNull { it })
}

data class Range(
    val mapTo: Long,
    val from: Long,
    val length: Long,
) {
    val to get() = from + length - 1

    override fun toString(): String {
        return "[$from, $to]"
    }
}

fun Range.contains(value: Long) = value >= from && value < from + length
fun Range.compute(value: Long): Long {
    assert(contains(value))
    return mapTo - from + value
}

fun Range.contains(value: Pair<Long, Long>) =
    contains(value.first) || contains(value.second) ||
            (value.first < from && value.second > from + length)

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
    // intersections with ranges
    val newIntervals = ranges.map {
        Pair(it.from, it.to).intersection(value)
    }.sortedBy { it.first }
    // add remaining intervals
    val notMatchedIntervals = notMatchedIntervals(value, newIntervals)

    val out = (newIntervals + notMatchedIntervals).map {
        val from = findMap(mapName, it.first)
        val to = findMap(mapName, it.second)
        Pair(from, to)
    }
    return out
}

// interval   |-------------|
// int A    |------|
// int B            |---------|
fun notMatchedIntervals(interval: Pair<Long, Long>, intervals: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
    val notMatchedIntervals = ArrayDeque(listOf(interval.copy()))
    intervals.forEach {
        // TODO probably not correct for complex cases
        val first = notMatchedIntervals.removeFirst()
        notMatchedIntervals.addAll(first.minus(it))
    }
    return notMatchedIntervals
}

/**
 * Remove [intervalB] from [intervalA]. it may return empty list, one interval or two intervals.
 */
private fun intervalDifference(intervalA: Pair<Long, Long>, intervalB: Pair<Long, Long>): List<Pair<Long, Long>> {
    if (intervalB.first <= intervalA.first && intervalB.second >= intervalA.second) {
        // intervalA    |----------|
        // intervalB  |--------------|
        return emptyList()
    }
    if (intervalB.first > intervalA.first && intervalB.second < intervalA.second) {
        // intervalA    |----------|
        // intervalB       |----|
        return listOf(Pair(intervalA.first, intervalB.first - 1), Pair(intervalB.second + 1, intervalA.second))
    }
    if (intervalA.first >= intervalB.first) {
        // intervalA    |----------|
        // intervalB  |----|
        return listOf(Pair(intervalB.second + 1, intervalA.second))
    }
    // intervalA    |----------|
    // intervalB             |----|
    // out          |-------|
    return listOf(Pair(intervalA.first, intervalB.first - 1))
}

fun Pair<Long, Long>.minus(i: Pair<Long, Long>) = intervalDifference(this, i)

fun Pair<Long, Long>.intersection(i: Pair<Long, Long>) = Pair(max(first, i.first), min(second, i.second))