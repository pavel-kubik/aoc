package y2023.day07

import utils.FileReader.Companion.readResourceFile

fun main() {
    val testLines = readResourceFile("/day07/test_data.txt")
    val lines = readResourceFile("/day07/data.txt")
    part1(testLines)
    part1(lines)
    part2(testLines)
    part2(lines)
}

fun compareHand(a: String, b: String, withJoker: Boolean = false): Int {
    val aValue = parseHand(a, withJoker)
    val bValue = parseHand(b, withJoker)
    return if (aValue == bValue) {
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
        card == 'J' -> 1
        card == 'T' -> 10
        else -> error("Unknown card.")
    }
}

/**
 * first:
 *  6 - five of a kind
 *  5 - four of the kind
 *  4 - full house
 *  3 - three of kind
 *  2 - two pairs
 *  1 - one pairs
 */
fun parseHand(a: String, withJoker: Boolean = false): Int {
    val charByTypes = mutableMapOf<Char, Int>()
    a.forEach {
        val count = charByTypes.computeIfAbsent(it) { 0 }
        charByTypes[it] = count + 1
    }
    var max = 0
    charByTypes.forEach {
        if (it.value > max) max = it.value
    }
    if (withJoker) {
        if (!a.contains('J')) {
            parseHand(a, false)
        } else {
            // find best usage of joker
            //   it make sense replace more joker only for same card
            var bestHand = 0
            (2..9).forEach {
                val value = parseHand(a.replace('J', it.toString()[0]))
                if (value > bestHand) {
                    bestHand = value
                }
            }
            listOf('A', 'K', 'Q', 'T').forEach {
                val value = parseHand(a.replace('J', it))
                if (value > bestHand) {
                    bestHand = value
                }
            }
            return bestHand
        }
    }
    return when (max) {
        5 -> 6
        4 -> 5
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

fun part1(lines: List<String>) {
    val out = lines.map {
        val (hand, bid) = it.split(" ")
        Pair(hand, bid.toInt())
    }.sortedWith { a, b ->
        compareHand(a.first, b.first)
    }.mapIndexed { index, pair ->
        //println("${pair.first} ${pair.second}")
        (index+1)*pair.second
    }.sum()
    println(out)
    // 253816004 too low
    // 253816004 too low
    // 253910319
}

fun part2(lines: List<String>) {
    val out = lines.map {
        val (hand, bid) = it.split(" ")
        Pair(hand, bid.toInt())
    }.sortedWith { a, b ->
        compareHand(a.first, b.first, true)
    }.mapIndexed { index, pair ->
        //println("${pair.first} ${pair.second}")
        (index+1)*pair.second
    }.sum()
    println(out)
}
