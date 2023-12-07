package y2023.day07

import utils.FileReader.Companion.readResourceFile
import utils.runWrapper

fun main() {
    val testLines = readResourceFile("/day07/test_data.txt")
    val lines = readResourceFile("/day07/data.txt")
    runWrapper(6440) { part1(testLines) }
    runWrapper(253910319) { part1(lines) }
    runWrapper(5905) { part2(testLines) }
    runWrapper(254083736) { part2(lines) }
}

fun compareHands(a: String, b: String, withJoker: Boolean): Int {
    val aValue = parseHand(a, withJoker)
    val bValue = parseHand(b, withJoker)
    return if (aValue == bValue) {  // same type
        compareCards(a, b, withJoker)
    } else {
        aValue - bValue
    }
}

fun compareCards(a: String, b: String, withJoker: Boolean): Int {
    for (i in 0 .. a.length) {
        val diff = compareCard(a[i], b[i], withJoker)
        if (diff != 0) {
            return diff
        }
    }
    error("Same deck")
}

fun compareCard(a: Char, b: Char, withJoker: Boolean): Int =
    cardLabelToNumber(a, withJoker) - cardLabelToNumber(b, withJoker)

fun cardLabelToNumber(card: Char, withJoker: Boolean): Int {
    return when {
        card.isDigit() -> card.toString().toInt()
        card == 'A' -> 14
        card == 'K' -> 13
        card == 'Q' -> 12
        card == 'J' -> if (withJoker) 1 else 11
        card == 'T' -> 10
        else -> error("Unknown card.")
    }
}

fun parseHand(cards: String, withJoker: Boolean = false): Int {
    val charByTypes = mutableMapOf<Char, Int>()
    cards.forEach {
        charByTypes.compute(it) { _, v -> if (v == null) 1 else v + 1 }
    }
    if (withJoker) {
        if (!cards.contains('J')) {
            parseHand(cards, false)
        } else {
            // find best usage of joker
            //   it make sense replace more joker only for same card
            var bestHand = 0
            ((2..9) + listOf('A', 'K', 'Q', 'T')).forEach {
                val value = parseHand(cards.replace('J', it.toString()[0]))
                if (value > bestHand) {
                    bestHand = value
                }
            }
            return bestHand
        }
    }
    var max = charByTypes.maxOf { it.value }
    return when (max) {
        5 -> 6  // five of a kind
        4 -> 5  // four of the kind
        3 -> if (charByTypes.size == 2)
                4 // full house
            else
                3 // three of kind
        2 -> if (charByTypes.size == 3)
                2 // two pairs
            else
                1 // one pair
        else -> 0
    }
}

fun solution(lines: List<String>, withJoker: Boolean = false): Int =
    lines.map {
        val (hand, bid) = it.split(" ")
        Pair(hand, bid.toInt())
    }.sortedWith { a, b ->
        compareHands(a.first, b.first, withJoker)
    }.mapIndexed { index, pair ->
        (index+1)*pair.second
    }.sum()

fun part1(lines: List<String>): Int = solution(lines)

fun part2(lines: List<String>): Int = solution(lines, withJoker = true)
