package y2023.day06

import utils.FileReader.Companion.readResourceFile

fun main() {
    val testLines = readResourceFile("/day06/test_data.txt")
    val lines = readResourceFile("/day06/data.txt")
    part1(testLines)
    part2(lines)
}

val timeDistance = listOf(
    Pair(56, 334),
    Pair(71, 1135),
    Pair(79, 1350),
    Pair(99, 2430)
)
//val timeDistance = listOf(
//    Pair(7, 9),
//    Pair(15, 40),
//    Pair(30, 200)
//)

fun part1(lines: List<String>) {
    val wins = timeDistance.map {
        val raceDuration = it.first.toLong()
        val maxDistance = it.second.toLong()
        (0..raceDuration).map {
            if (distance(it, raceDuration) > maxDistance) 1 else 0
        }.sum()
    }.reduce { acc, i -> acc * i }
    println(wins)
}

val timeDistance2 = listOf(
    Pair(56717999L, 334113513502430L),
)

fun part2(lines: List<String>) {
    val wins = timeDistance2.map {
        val raceDuration = it.first
        val maxDistance = it.second
        (0..raceDuration).map {
            if (distance(it, raceDuration) > maxDistance) 1 else 0
        }.sum()
    }.reduce { acc, i -> acc * i }
    println(wins)
}

fun distance(accelerationTime: Long, duration: Long): Long {
    return (duration - accelerationTime) * accelerationTime
}