package y2023.day12

import utils.FileReader
import utils.runWrapper
import kotlin.math.max

fun main() {
    val testLines = FileReader.readResourceFile("/day12/test_data.txt")
    val lines = FileReader.readResourceFile("/day12/data.txt")
    runWrapper(21) { part1(testLines) }
    runWrapper(7084) { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

fun possibleMatch(hotSprings: String, groups: List<Int>, solutions: Int): Int {
    val idx = hotSprings.indexOfFirst { it == '?' }
    if (idx == -1) {
        val g = hotSprings.split(".").filter { it.isNotEmpty() }.map { it.length }
        //println("$g ? $groups (${g == groups})")
        return solutions + if (g == groups) 1 else 0
    } else {
        val broken =  hotSprings.replaceFirst('?', '#')
        val functional = hotSprings.replaceFirst('?', '.')
        return solutions + possibleMatch(broken, groups, solutions) + possibleMatch(functional, groups, solutions)
    }
}

fun part1(lines: List<String>): Int {
    val out = lines.map {
        val (hotSprings, damagedGroups) = it.split(" ")
        possibleMatch(hotSprings, damagedGroups.split(",").map { it.toInt() }, 0)
    }
    println(out)
    return out.sum()
}

fun unfold(a: String, c: Char): String {
    return "$a$c$a$c$a$c$a$c$a"
}

fun part2(lines: List<String>): Int {
    val out = lines.map {
        val (hotSprings, damagedGroups) = it.split(" ")
        possibleMatch(unfold(hotSprings, '?'), unfold(damagedGroups, ',').split(",").map { it.toInt() }, 0)
    }
    println(out)
    return out.sum()
}