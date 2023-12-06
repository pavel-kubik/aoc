package y2023

import org.junit.jupiter.api.Test
import y2023.day05.minus
import y2023.day05.notMatchedIntervals

class day05Test {

    @Test
    fun `interval difference`() {
        val intervalA = Pair(74L, 87L)
        val intervalB = Pair(77L, 99L)
        val out = intervalA.minus(intervalB)
        assert(out == listOf(Pair(74L, 76L)))
    }

    @Test
    fun `empty not matched intervals`() {
        val interval = Pair(74L, 87L)
        val intervals = listOf(Pair(77L, 99L), Pair(64L, 76L))
        val notMatchedIntervals = notMatchedIntervals(interval, intervals)
        assert(notMatchedIntervals.isEmpty())
    }
}