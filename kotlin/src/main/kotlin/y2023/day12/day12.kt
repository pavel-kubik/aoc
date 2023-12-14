package y2023.day12

import utils.FileReader
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import utils.runWrapperS

fun main() {
    val testLines = FileReader.readResourceFile("/day12/test_data.txt")
    val lines = FileReader.readResourceFile("/day12/data.txt")
    //runWrapper(21) { part1(testLines) }
    //runWrapper(7084) { part1(lines) }
    //runWrapper(525152) { part2(testLines) }
    //runWrapper { part2(lines) }
//    runBlocking {
//        runWrapperS(525152) { part2(testLines) }
//    }
    runBlocking {
        runWrapperS { part2(lines) }
    }
}

data class Key(
    val springs: String,
    val groups: List<Int>,
)

val matchCache = mutableMapOf<Key, Long>()

suspend fun possibleMatch(hotSprings: String, groups: List<Int>, solutions: Long, traceId: String? = null): Long {
    val key = Key(hotSprings, groups)
    if (matchCache.containsKey(key)) {
        println("Cache used")
        return matchCache[key]!!
    }
    val idx = hotSprings.indexOfFirst { it == '?' }
    val out = if (idx == -1) {
        val g = hotSprings.split(".").filter { it.isNotEmpty() }.map { it.length }
        //println("$g ? $groups (${g == groups})")
        solutions + if (g == groups) 1L else 0L
    } else {
        val g = hotSprings.substringBefore("?").split(".").filter { it.isNotEmpty() }.map { it.length }
        if (g.size > groups.size) {
            return solutions
        }
        if (g.sum() > groups.sum()) {
            return solutions
        }

        val broken =  hotSprings.replaceFirst('?', '#')
        val functional = hotSprings.replaceFirst('?', '.')
        if (traceId != null) println("Iteration $traceId")
        solutions + possibleMatch(broken, groups, solutions) + possibleMatch(functional, groups, solutions)
    }
    matchCache[key] = out
    //println("Cache size: ${matchCache.size}")
    return out
}

suspend fun part1(lines: List<String>): Long {
    val out = lines.map {
        val (hotSprings, damagedGroups) = it.split(" ")
        possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    }
    println(out)
    return out.sum()
}

suspend fun diff(hotSprings: String, damagedGroups: String, traceId: String): Long {
    println("Compute diff $traceId")
    val tuple = possibleMatch("$hotSprings?$hotSprings", "$damagedGroups,$damagedGroups".split(",").map { it.toInt() }, 0, traceId)
    val simple = possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0, traceId)
    val out = tuple.toDouble() / (simple*simple)
    return (simple*out).toLong()
}

suspend fun compute(it: String, traceId: String) {
    println("Computing for $traceId")
    val (hotSprings, damagedGroups) = it.split(" ")
    val first = possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0, traceId)
    val next = if (hotSprings.last() == '#' || hotSprings.first() == '#')
        possibleMatch(".$hotSprings", damagedGroups.split(",").map { it.toInt() }, 0, traceId)
    else if (hotSprings.last() == '.' || hotSprings.first() == '.')
        possibleMatch("?$hotSprings", damagedGroups.split(",").map { it.toInt() }, 0, traceId)
    else diff(hotSprings, damagedGroups, traceId)
    println("Sending for $traceId")
    channel.send(first * next * next * next * next)
    println("DONE for $traceId")
}

val channel = Channel<Long>()

suspend fun part2(lines: List<String>): Long {
    var out = 0L
    var sendCount = 0
    var receivedCount = 0
    val backgroundDispatcher = newFixedThreadPoolContext(4, "App Background")
    coroutineScope {
        launch {
            lines.forEach {
                launch(backgroundDispatcher) {
                    sendCount++
                    compute(it, sendCount.toString())
                }
            }
        }
        println("All threads started")
        launch {
            repeat(lines.size) {
                val result = channel.receive()
                println("Received $result ${++receivedCount}/${lines.size}")
                out += result
            }
        }
    }
    return out
}
