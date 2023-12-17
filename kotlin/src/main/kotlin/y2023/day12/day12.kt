package y2023.day12

import utils.FileReader
import utils.runWrapper

fun main() {
    val testLines = FileReader.readResourceFile("/day12/test_data.txt")
    val lines = FileReader.readResourceFile("/day12/data.txt")
    runWrapper(21) { part1(testLines) }
    runWrapper(7084) { part1(lines) }
    runWrapper(525152) { part2(testLines) }
    runWrapper { part2(lines) }
}

data class Key(
    val springs: String,
    val groups: List<Int>,
)

//val matchCache = mutableMapOf<Key, Long>()

fun possibleMatch(hotSprings: String, groups: List<Int>, solutions: Long, traceId: String? = null): Long {
//    val key = Key(hotSprings, groups)
//    if (matchCache.containsKey(key)) {
//        //println("Cache used")
//        return matchCache[key]!!
//    }
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
    //matchCache[key] = out
    //println("Cache size: ${matchCache.size}")
    return out
}

fun part1(lines: List<String>): Long {
    val out = lines.map {
        val (hotSprings, damagedGroups) = it.split(" ")
        possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    }
    //println(out)
    return out.sum()
}

fun diff(hotSprings: String, damagedGroups: String): Long {
    val tuple = possibleMatch("$hotSprings?$hotSprings", "$damagedGroups,$damagedGroups".split(",").map { it.toInt() }, 0)
    val simple = possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    val out = tuple.toDouble() / (simple*simple)
    return (simple*out).toLong()
}

fun compute(it: String): Long {
    val (hotSprings, damagedGroups) = it.split(" ")
    val first = possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    val next = if (hotSprings.last() == '#' || hotSprings.first() == '#')
        possibleMatch(".$hotSprings", damagedGroups.split(",").map { it.toInt() }, 0)
    else if (hotSprings.last() == '.' || hotSprings.first() == '.')
        possibleMatch("?$hotSprings", damagedGroups.split(",").map { it.toInt() }, 0)
    else diff(hotSprings, damagedGroups)
    return first * next * next * next * next
}

fun part2(lines: List<String>): Long {
    var out = 0L
    lines.forEach {
        out += compute(it)
    }
    return out
}
