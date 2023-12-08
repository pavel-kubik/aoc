package y2023.day08

import com.sun.org.apache.xalan.internal.lib.ExsltMath.power
import utils.FileReader
import utils.runWrapper
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val testLines = FileReader.readResourceFile("/day08/test_data.txt")
    val testLinesPart2 = FileReader.readResourceFile("/day08/test_data_part2.txt")
    val lines = FileReader.readResourceFile("/day08/data.txt")
    runWrapper(6) { part1(testLines) }
    runWrapper(14257) { part1(lines) }
    runWrapper(6) { part2(testLinesPart2) }
    runWrapper(16187743689077) { part2(lines) }
}

fun move(node: String, direction: Char, nodesMap: Map<String, Pair<String, String>>): String {
    return if (direction == 'L') {
        nodesMap[node]!!.first
    } else {
        nodesMap[node]!!.second
    }
}

fun parse(lines: List<String>): Map<String, Pair<String, String>> {
    return (2 until lines.size).map {
        val (start, dest) = lines[it].split(" = ")
        val (left, right) = dest.substring(1, dest.length - 1).split(", ")
        start to Pair(left, right)
    }.toMap()
}

fun part1(lines: List<String>): Int {
    val way = lines[0]
    val nodesMap = parse(lines)
    var idx = 0
    var steps = 0
    var node = "AAA"
    while (node != "ZZZ") {
        val direction = way[idx++]
        node = move(node, direction, nodesMap)
        if (idx >= way.length) {
            idx = 0
        }
        steps++
    }
    return steps
}

fun part2(lines: List<String>): Long {
    val way = lines[0]
    val nodesMap = parse(lines)
    var idx = 0
    var steps = 1
    var nodes = nodesMap.map { it.key }.filter {
        it.endsWith("A")
    }
    var nodeSteps = nodes.map { -1 }.toMutableList()
    while (nodes.any { !it.endsWith("Z") }) {
        val direction = way[idx++]
        nodes = nodes.mapIndexed { i, it ->
            val node = move(it, direction, nodesMap)
            if (node.endsWith("Z")) {
                if (nodeSteps[i] != -1) error("Multiple Z on way")
                nodeSteps[i] = steps
            }
            node
        }
        if (idx >= way.length) {
            idx = 0
        }
        steps++
        if (nodeSteps.all { it != -1 }) break
    }

    val step = nodeSteps.maxOf { it }
    var i = step.toLong()
    while (!nodeSteps.all { i % it == 0L}) {
        i += step
    }
    return i
}
