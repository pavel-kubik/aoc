package y2023.day12

import utils.FileReader
import utils.runWrapper
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

suspend fun possibleMatch(hotSprings: String, groups: List<Int>, solutions: Long): Long {
    val idx = hotSprings.indexOfFirst { it == '?' }
    return if (idx == -1) {
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
        delay(1)
        solutions + possibleMatch(broken, groups, solutions) + possibleMatch(functional, groups, solutions)
    }
}

suspend fun part1(lines: List<String>): Long {
    val out = lines.map {
        val (hotSprings, damagedGroups) = it.split(" ")
        possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    }
    println(out)
    return out.sum()
}

suspend fun diff(hotSprings: String, damagedGroups: String): Long {
    val tuple = possibleMatch("$hotSprings?$hotSprings", "$damagedGroups,$damagedGroups".split(",").map { it.toInt() }, 0)
    val simple = possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    val out = tuple.toDouble() / (simple*simple)
    println("$tuple : $simple = $out")
    return (simple*out).toLong()
}

suspend fun compute(it: String) {
    val (hotSprings, damagedGroups) = it.split(" ")
    val first = possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    val next = if (hotSprings.last() == '#' || hotSprings.first() == '#')
        possibleMatch(".$hotSprings", damagedGroups.split(",").map { it.toInt() }, 0)
    else if (hotSprings.last() == '.' || hotSprings.first() == '.')
        possibleMatch("?$hotSprings", damagedGroups.split(",").map { it.toInt() }, 0)
    else diff(hotSprings, damagedGroups)
    channel.send(first * next * next * next * next)
}

val channel = Channel<Long>()

suspend fun part2(lines: List<String>): Long {
    var out = 0L
    var count = 0
    coroutineScope {
        launch {
            lines.forEach {
                launch {
                    compute(it)
                }
            }
        }
        launch {
            repeat(lines.size) {
                val result = channel.receive()
                println("Received $result ${count++}/${lines.size}")
                out += result
            }
        }
    }
    println(out)
    return out
}
