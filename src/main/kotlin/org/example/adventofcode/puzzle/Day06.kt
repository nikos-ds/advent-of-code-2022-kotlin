package org.example.adventofcode.puzzle

import org.example.adventofcode.util.FileLoader

object Day06 {
    private const val FILE_PATH = "/day-06-input.txt"

    fun printSolution() {
        println("- part 1: ${getFirstMarker(4)}")
        println("- part 2: ${getFirstMarker(14)}")
    }

    private fun getFirstMarker(numberOfChars: Int): Int {
        val input = FileLoader.loadFromFile<String>(FILE_PATH)[0]
        for (i in numberOfChars..input.length) {
            val lastUniqueChars = input.substring(i - numberOfChars, i).toSet()
            if (lastUniqueChars.size == numberOfChars) {
                return i
            }
        }
        return -1
    }

}
