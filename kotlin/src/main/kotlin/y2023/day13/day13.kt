package y2023.day13

import utils.FileReader
import utils.runWrapper
import utils.splitGroups
import kotlin.math.max
import kotlin.math.min

fun main() {
    val testLines = FileReader.readResourceFile("/day13/test_data.txt")
    val lines = FileReader.readResourceFile("/day13/data.txt")
    runWrapper(405) { part1(testLines) }
    runWrapper(35232) { part1(lines) }
    runWrapper(400) { part2(testLines) }
    runWrapper(37982) { part2(lines) }
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

fun findMirror(group: List<String>, ignore: Pair<Int, Int> = Pair(-1, -1)): Pair<Int, Int>? {
    for (i in 1 until group.first().length) {
        val isReflection = checkVerticalReflection(group, i)
        if (isReflection && i != ignore.second) {
            return Pair(0, i)
        }
    }
    for (i in 1 until group.size) {
        val isReflection = checkHorizontalReflection(group, i)
        if (isReflection && i != ignore.first) {
            return Pair(i, 0)
        }
    }
    return null
}

fun part1(lines: List<String>): Int {
    val mirrors = splitGroups(lines).map { group ->
        findMirror(group)!!
    }
    return mirrors.sumOf { 100*it.first + it.second }
}

fun part2(lines: List<String>): Int {
    val mirrors = splitGroups(lines).mapIndexed { idx, g ->
        val original = findMirror(g)!!
        g.indices.forEach { r ->
            g[r].indices.forEach { c ->
                val group = g.mapIndexed { ri, it ->
                    it.mapIndexed { ci, it ->
                        if (r == ri && c == ci) {
                            if (it == '#') '.' else '#'
                        } else {
                            it
                        }
                    }.joinToString("") { it.toString() }
                }
                val mirror = findMirror(group, original)
                if (mirror != null) {
                    return@mapIndexed mirror
                }
            }
        }
        error("Not found")
    }
    return mirrors.sumOf { 100*it.first + it.second }
}
