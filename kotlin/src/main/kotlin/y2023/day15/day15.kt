package y2023.day15

import utils.FileReader
import utils.runWrapper
import java.util.*

fun main() {
    val testLines = FileReader.readResourceFile("/day15/test_data.txt")
    val lines = FileReader.readResourceFile("/day15/data.txt")
    runWrapper(1320) { part1(testLines) }
    runWrapper(512950) { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

fun hash(input: String): Int {
    var out = 0
    input.forEach {
        out = (out + it.code)*17 % 256
    }
    return out
}

fun part1(lines: List<String>): Any {
    if (hash("HASH") != 52) error("Hash error")
    return lines.first().split(",").map {
        hash(it)
    }.sum()
}

fun part2(lines: List<String>): Any {
    val boxes = Array<LinkedList<Pair<String, Int>>>(256) {
        LinkedList()
    }
    lines.first().split(",").forEach{
        val nameDash = it.split("-")?.first() ?: ""
        if (it.endsWith("-")) {
            // dash
            val idx = hash(nameDash)
            boxes[idx].removeIf { it.first == nameDash }
        } else {
            // equals
            val (nameEquals, value) = it.split("=")
            val idx = hash(nameEquals)
            val lens = Pair(nameEquals, value.toInt())
            val toReplaceIdx = boxes[idx].indexOfFirst {
                it.first == nameEquals
            }
            if (toReplaceIdx != -1) {
                boxes[idx][toReplaceIdx] = lens
            } else {
                boxes[idx].addLast(lens)
            }
        }
    }
    return boxes.mapIndexed { box, linkedList ->
        linkedList.mapIndexed { slot, pair ->
            (box + 1)*(slot +1)*pair.second
        }.sum()
    }.sum()
}
