package y2023

import utils.FileReader
import utils.runWrapper

fun main() {
    val testLines = FileReader.readResourceFile("/day00/test_data.txt")
    val lines = FileReader.readResourceFile("/day00/data.txt")
    runWrapper { part1(testLines) }
    runWrapper { part1(lines) }
    runWrapper { part2(testLines) }
    runWrapper { part2(lines) }
}

fun part1(lines: List<String>) {

}

fun part2(lines: List<String>) {

}
