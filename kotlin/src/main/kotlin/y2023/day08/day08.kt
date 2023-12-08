package y2023.day08

import com.sun.org.apache.xalan.internal.lib.ExsltMath.power
import utils.FileReader
import utils.lcm
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

    // https://www.tiger-algebra.com/en/solution/least-common-multiple/lcm%2812%2C18%2C24%2C36%29/#:~:text=The%20least%20common%20multiple%20of,24%20and%2036%20is%2072.
    val primeNumbersNodes = nodeSteps.map {
        primeFactors(it)
    }
    val primes = primeNumbersNodes.reduce { acc, i -> acc + i } // jones maps
    val out = primes.filter { it.value > 0 }.map {
        power(it.key.toDouble(), it.value.toDouble())
    }.reduce { acc, d -> acc * d }
    return out.toLong()
}

fun primeFactors(input: Int): Map<Int, Int> {
    var n = input
    var out = mutableMapOf<Int, Int>()
    // Print the number of 2s that divide n
    while (n % 2 == 0) {
        out.compute(2) { _, v -> if (v == null) 1 else v + 1 };
        n = floor(n / 2.0).toInt();
    }

    // n must be odd at this point.
    // So we can skip one element
    // (Note i = i +2)
    //for(let i = 3; i <= Math.floor(Math.sqrt(n)); i = i + 2) {
    for (i in 3..floor(sqrt(n.toDouble())).toInt() step 2) {

        // While i divides n, print i
        // and divide n
        while (n % i == 0) {
            out.compute(i) { _, v -> if (v == null) 1 else v + 1 };
            n = floor(n / i.toDouble()).toInt();
        }
    }

    // This condition is to handle the
    // case when n is a prime number
    // greater than 2
    if (n > 2)
        out.compute(n) { _, v -> if (v == null) 1 else v + 1};
    return out
}
