package y2023.day11

import utils.FileReader
import utils.runWrapper
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

fun main() {
    val testLines = FileReader.readResourceFile("/day11/test_data.txt")
    val lines = FileReader.readResourceFile("/day11/data.txt")
    //runWrapper(374) { part1(testLines) }
    //runWrapper { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

fun expand(lines: List<String>): List<String> {
    val rowToExpand = mutableListOf<Int>()
    for (i in lines.indices) {
        if (lines[i].all { it == '.' }) {
            // expand row
            rowToExpand.add(i)
        }
    }
    val columnToExpand = mutableListOf<Int>()
    for (j in lines[0].indices) {
        if (lines.all { it[j] == '.' }) {
            // expand column
            columnToExpand.add(j)
        }
    }
    println(lines)
    var map = lines.toMutableList()
    val rowTemplate = map[0].map { "." }.reduce { acc, c -> "$acc$c" }
    rowToExpand.reversed().forEach {
        map.add(it, rowTemplate)
    }
    columnToExpand.reversed().forEach { col ->
        map = map.map { line ->
            line.substring(0, col) + '.' + line.substring(col)
        }.toMutableList()

    }
    println(map)
    return map
}


fun getPoints(map: List<String>, v: Char): List<Pair<Int, Int>> {
    var out = mutableListOf<Pair<Int, Int>>()
    for (i in map.indices) {
        for (j in map[i].indices) {
            if (map[i][j] == v) {
                out.add(Pair(j, i))
            }
        }
    }
    return out.toList()
}

fun dist(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
    return abs(a.first - b.first) + abs(a.second - b.second)
}

fun part1(lines: List<String>): Int {
    val map = expand(lines)
    val stars = getPoints(map, '#')
    var sum = 0
    for (i in stars.indices) {
        for (j in i+1 until stars.size) {
            val dist = dist(stars[i], stars[j])
            println("$i to $j = $dist")
            sum += dist
        }
    }
    return sum
}

fun distFar(a: Pair<Int, Int>, b: Pair<Int, Int>, rowsSpace: List<Int>, colsSpace: List<Int>, dist: Long): Long {
    var dx = abs(a.first - b.first)
    var dy = abs(a.second - b.second)
    val rowSpaces = rowsSpace.filter {
        min(a.second, b.second) < it && it < max(a.second, b.second)
    }.map { 1 }.sum()

    val colSpaces = colsSpace.filter {
        min(a.first, b.first) < it && it < max(a.first, b.first)
    }.map { 1 }.sum()
    return dx + dy + rowSpaces*(dist-1) + colSpaces*(dist-1)
}

fun part2(lines: List<String>): Long {
    val stars = getPoints(lines, '#')
    val rowToExpand = mutableListOf<Int>()
    for (i in lines.indices) {
        if (lines[i].all { it == '.' }) {
            // expand row
            rowToExpand.add(i)
        }
    }
    val columnToExpand = mutableListOf<Int>()
    for (j in lines[0].indices) {
        if (lines.all { it[j] == '.' }) {
            // expand column
            columnToExpand.add(j)
        }
    }
    var sum = 0L
    for (i in stars.indices) {
        for (j in i+1 until stars.size) {
            val dist = distFar(stars[i], stars[j], rowToExpand, columnToExpand, 1000000L)
            //println("$i to $j = $dist")
            sum += dist
        }
    }
    return sum
}
