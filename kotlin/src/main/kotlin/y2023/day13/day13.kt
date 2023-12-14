package y2023.day13

import utils.FileReader
import utils.runWrapper
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testLines = FileReader.readResourceFile("/day13/test_data.txt")
    val lines = FileReader.readResourceFile("/day13/data.txt")
    runWrapper(405) { part1(testLines) }
    runWrapper { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

fun checkVerticalReflection(lines: List<String>, idx: Int): Boolean {
    val reflectionTo = min(2*idx, lines[0].length)
    val reflectionFrom = max(0, idx-(reflectionTo-idx))
    return lines.all { line ->
        line.substring(reflectionFrom, idx) == line.substring(idx, reflectionTo).reversed()
    }
}

fun checkHorizontalReflection(lines: List<String>, idx: Int): Boolean {
    val reflectionTo = min(2*idx, lines.size)
    val reflectionFrom = max(0, idx-(reflectionTo-idx))
    val up = lines.subList(reflectionFrom, idx).toString()
    val down = lines.subList(idx, reflectionTo).reversed().toString()
    return up == down
}

fun part1(lines: List<String>): Int {
    // split by new lines
    val groups = lines.fold(mutableListOf(mutableListOf<String>())) { acc, i ->
        if (i.isEmpty()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(i)
        }
        acc
    }
    val mirrors = groups.mapIndexed { idx, group ->
        println("Group $idx:\n" + group.joinToString("\n") { it } + "\n")
        for (i in 1 until group.first().length) {
            val isReflection = checkVerticalReflection(group, i)
            if (isReflection) {
                return@mapIndexed Pair(0, i)
            }
        }
        for (i in 1 until group.size) {
            val isReflection = checkHorizontalReflection(group, i)
            if (isReflection) {
                return@mapIndexed Pair(i, 0)
            }
        }
        error("Not found")
        Pair(0, 0)
    }
    return mirrors.sumOf { 100*it.first + it.second }
}

fun part2(lines: List<String>) {

}
