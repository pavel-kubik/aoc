package utils

import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Greatest common divisor
 */
fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

/**
 * Greatest common divisor
 */
fun gcd(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = gcd(result, input[i])
    return result
}

/**
 * Least common multiple
 */
fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

/**
 * Least common multiple
 */
fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}

/**
 * Greatest common divisor
 */
fun gcd(a: Int, b: Int): Int {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

/**
 * Greatest common divisor
 */
fun gcd(input: List<Int>): Int {
    var result = input[0]
    for (i in 1 until input.size) result = gcd(result, input[i])
    return result
}

/**
 * Least common multiple
 */
fun lcm(a: Int, b: Int): Int {
    return a * (b / gcd(a, b))
}

/**
 * Least common multiple
 */
fun lcm(input: List<Int>): Int {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}

/**
 * Prime factors for [input].
 *
 * Example for input 24 = 2^3 + 3^1
 *
 * return {
 *   2 to 3
 *   3 to 1
 * }
 *
 */
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
