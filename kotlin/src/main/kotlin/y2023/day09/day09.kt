package y2023.day09

import utils.FileReader
import utils.runWrapper

fun main() {
    val testLines = FileReader.readResourceFile("/day09/test_data.txt")
    val lines = FileReader.readResourceFile("/day09/data.txt")
    runWrapper(114) { part1(testLines) }
    runWrapper { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

fun diffItems(seq: List<Int>): MutableList<Int> {
    var a = seq[0]
    val out = seq.mapIndexed { index, i ->
        if (index > 0) {
            val out = i - a
            a = i
            out
        } else {
            null
        }
    }.filterNotNull()
    return out.toMutableList()
}

fun findNext(seq: List<Int>): Int {
    var diffs = mutableListOf(seq.toMutableList())
    do {
        val diff = diffItems(diffs.last())
        diffs.add(diff)
    } while (diff.any { it != 0})
    for (i in diffs.size - 2 downTo 0) {
        diffs[i].add(diffs[i].last() + diffs[i+1].last())
    }
    return diffs[0].last()
}

fun part1(lines: List<String>): Int {
    return lines.map {
        val numbers = it.split(" ").map { it.toInt() }
        findNext(numbers)
    }.sum()
}

fun part2(lines: List<String>): Int {
    return lines.map {
        val numbers = it.split(" ").map { it.toInt() }
        findNext(numbers.reversed())
    }.sum()
}
