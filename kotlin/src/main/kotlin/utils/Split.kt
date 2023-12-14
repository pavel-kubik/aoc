package utils

/**
 * Read all lines and split it to groups divided by new line
 */
fun splitGroups(lines: List<String>): List<List<String>> {
    return lines.fold(mutableListOf(mutableListOf<String>())) { acc, i ->
        if (i.isEmpty()) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(i)
        }
        acc
    }
}