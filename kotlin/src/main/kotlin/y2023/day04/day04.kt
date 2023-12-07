package y2023.day04

import utils.FileReader.Companion.readResourceFile

fun main() {
    val testLines = readResourceFile("/day04/test_data.txt")
    val lines = readResourceFile("/day04/data.txt")
    //part1(testLines)
    //part1(lines)
    part2(testLines)
    part2(lines)
}

fun part1(lines: List<String>) {
    val points = lines.map {
        val cards = it.split(":")[1].split("|")
        val winningNumbers = cards[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val numbers = cards[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val matches = numbers.filter { winningNumbers.contains(it) }
        if (matches.isNotEmpty()) Math.pow(2.0, matches.size.toDouble() - 1).toInt() else 0
    }.sum()
    println(points)
}

fun part2(lines: List<String>) {
    val match = lines.map {
        val cards = it.split(":")[1].split("|")
        val winningNumbers = cards[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val numbers = cards[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val matches = numbers.filter { winningNumbers.contains(it) }
        if (matches.isNotEmpty()) matches.size else 0
    }
    val score = match.map { Math.pow(2.0, it - 1.0).toInt() }
    // copy score based on match
    val scoreRatio = match.map { 1 }.toMutableList()
    println(match)
    println(score)
    println(scoreRatio)
    (0..match.size - 1).forEach { i ->
        if (match[i] > 0) {
            (i + 1..Math.min(match.size - 1, i + match[i])).forEach {
                scoreRatio[it] += scoreRatio[i]
            }
            println(scoreRatio)
        }
    }
    println(scoreRatio.sum())
}