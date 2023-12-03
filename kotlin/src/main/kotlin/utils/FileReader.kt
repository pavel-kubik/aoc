package utils

class FileReader {
    companion object {
        fun readResourceFile(fileName: String): List<String> =
            this::class.java.getResourceAsStream(fileName).bufferedReader().readLines()
    }
}