package y2023.day03

import utils.FileReader.Companion.readResourceFile
import utils.runWrapper

fun main() {
    val testLines = readResourceFile("/day03/test_data.txt")
    val lines = readResourceFile("/day03/data.txt")
    runWrapper(4361) { part1(testLines) }
    runWrapper(535078) { part1(lines) }
    runWrapper(467835) { part2(testLines) }
    runWrapper(75312571) { part2(lines) }
}

private fun parseEngine(lines: List<String>): Map<String, String> {
    val map = mutableMapOf<String, String>()
    lines.forEachIndexed { rowIdx, line ->
        var idx = -1
        var number = ""
        line.forEachIndexed { i, c ->
            if (c.isDigit()) {
                number += c
                if (idx == -1) idx = i
            } else {
                if (idx != -1) {
                    // place numbers to all indexes
                    (idx..i - 1).forEach {
                        map["${rowIdx}_$it"] = "${number}_${rowIdx}_$idx"
                    }
                    idx = -1
                    number = ""
                }
            }
        }
        if (idx != -1) {
            // place numbers to all indexes
            (idx..idx + number.length - 1).forEach {
                map["${rowIdx}_$it"] = "${number}_${rowIdx}_$idx"
            }
            idx = -1
            number = ""
        }
    }
    return map
}

val directions = listOf(Pair(-1,-1), Pair(0,-1), Pair(1,-1),
                        Pair(-1, 0), Pair(1,0),
                        Pair(-1,1), Pair(0,1), Pair(1,1),)

fun part1(lines: List<String>) {
    val engine = parseEngine(lines).toMutableMap();
    val partIds = mutableSetOf<String>()
    lines.forEachIndexed { rowIdx, line ->
        line.forEachIndexed { i, c ->
            if (!c.isDigit() && c != '.') {
                directions.map {
                    val partId = engine["${rowIdx+it.first}_${i+it.second}"]
                    if (partId != null) {
                        partIds.add(partId)
                    }
                }
            }
        }
    }
    println(partIds.map { it.split("_")[0].toInt()}.sum())
}

fun part2(lines: List<String>) {
    val engine = parseEngine(lines).toMutableMap();
    val sumRatios = lines.mapIndexed { rowIdx, line ->
        line.mapIndexed { i, c ->
            val gearParts = mutableSetOf<String>()
            if (c == '*') {
                directions.forEach {
                    val partId = engine["${rowIdx+it.first}_${i+it.second}"]
                    if (partId != null) {
                        gearParts.add(partId)
                    }
                }
            }
            if (gearParts.size == 2)
                gearParts
                    .map { it.split("_")[0].toInt() }
                    .reduce { acc, i -> acc*i }
            else 0
        }.sum()
    }.sum()
    println(sumRatios)
}
