package org.example.adventofcode.puzzle

import org.example.adventofcode.util.FileLoader

object Day04 {
    private const val FILE_PATH = "/day-04-input.txt"

    fun printSolution() {
        println("- part 1: ${getResult(Day04::part1OverlapChecker)}")
        println("- part 2: ${getResult(Day04::part2OverlapChecker)}")
    }

    private fun getResult(overlaps: (Int, Int, Int, Int) -> Boolean): Int {
        val allLines = FileLoader.loadFromFile<String>(FILE_PATH)
        var overlappingPairs = 0

        for (line in allLines) {
            val (a, b, x, y) = line.split("""[-,]""".toRegex()).map { it.toInt() }
            if (overlaps(a, b, x, y)) {
                overlappingPairs += 1
            }
        }

        return overlappingPairs
    }

    private fun part1OverlapChecker(a: Int, b: Int, x: Int, y: Int): Boolean {
        return x >= a && y <= b || a >= x && b <= y
    }

    private fun part2OverlapChecker(a: Int, b: Int, x: Int, y: Int): Boolean {
        return x in a..b || y in a..b || a in x..y || b in x..y
    }

}