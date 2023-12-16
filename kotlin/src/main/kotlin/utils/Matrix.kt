package utils

import y2023.day14.move

interface Matrix<T> {

    val width: Int
    val height: Int

    /**
     * Get value for [column] and [row].
     */
    operator fun get(row: Int, column:Int): T?

    /**
     * Get value for Pair<column, row>.
     */
    operator fun get(coords: Pair<Int, Int>): T? = get(coords.first, coords.second)

    /**
     * Set value for [column] and [row]
     */
    operator fun set(row: Int, column: Int, value: T)

    /**
     * Set value for Pair<column, row>.
     */
    operator fun set(coords: Pair<Int, Int>, value: T) = set(coords.first, coords.second, value)

    fun rotateRight()

    fun rotateLeft()
}

enum class MatrixType {
    SPARSE_MATRIX,
    ARRAY_MATRIX
}

fun <T> createMatrix(
    width: Int,
    height: Int,
    initValue: T,
    matrixType: MatrixType = MatrixType.SPARSE_MATRIX
): Matrix<T> {
    return when(matrixType) {
        MatrixType.SPARSE_MATRIX -> SparseMatrix(width, height, initValue)
        MatrixType.ARRAY_MATRIX -> ArrayMatrix(width, height, initValue)
    }
}

fun createMatrix(
    lines: List<String>,
    emptyValue: Char,
    matrixType: MatrixType = MatrixType.SPARSE_MATRIX
): Matrix<Char> {
    val width = lines.first().length
    val height = lines.size
    val matrix = when(matrixType) {
        MatrixType.SPARSE_MATRIX -> SparseMatrix(width, height, emptyValue)
        MatrixType.ARRAY_MATRIX -> ArrayMatrix(width, height, emptyValue)
    }
    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, c ->
            matrix[row, column] = c
        }
    }
    return matrix
}


class ArrayMatrix<T>(override val width: Int, override val height: Int, initValue: T) : Matrix<T> {
    val data = MutableList(height) {
        MutableList(width) { initValue }
    }

    override fun get(row: Int, column: Int): T? {
        TODO("Not yet implemented")
    }

    override fun set(row: Int, column: Int, value: T) {
        TODO("Not yet implemented")
    }

    override fun rotateRight() {
        TODO("Not yet implemented")
    }

    override fun rotateLeft() {
        TODO("Not yet implemented")
    }
}

class SparseMatrix<T>(override val width: Int, override val height: Int, val initValue: T) : Matrix<T> {
    var data = mutableMapOf<Pair<Int, Int>, T>()

    override fun get(row: Int, column: Int): T? {
        return if (column in (0 until width) && row in (0 until height)) {
            data[Pair(row, column)] ?: initValue
        } else {
            null
        }
    }

    // TODO make effective switch function
    //override fun switch(from: Pair<Int, Int>, to: Pair<Int, Int>) {

    override fun set(row: Int, column: Int, value: T) {
        data[Pair(row, column)] = value
        // TODO kep holes
//        if (value == initValue) {
//            data.remove(Pair(column, row))
//        } else {
//            data[Pair(row, column)] = value
//        }
    }

    override fun rotateRight() {
        val newData = mutableMapOf<Pair<Int, Int>, T>()
        data.forEach {
            newData[Pair(it.key.second, width - it.key.first - 1)] = it.value
        }
        data = newData
    }

    override fun rotateLeft() {
        val newData = mutableMapOf<Pair<Int, Int>, T>()
        data.forEach {
            newData[Pair(height - it.key.second - 1, it.key.first)] = it.value
        }
        data = newData
    }

    // TODO move to ancestor
    override fun toString(): String {
        var out = StringBuilder()
        (0 until this.height).forEach { row ->
            (0 until this.width).forEach { column ->
                //out.append(this[column, row])
                out.append(this[row, column])
            }
            out.append('\n')
        }
        return out.toString().trim()
    }
}