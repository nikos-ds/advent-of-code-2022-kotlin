package org.example.adventofcode.puzzle

import org.apache.commons.lang3.StringUtils.isBlank
import org.example.adventofcode.util.FileLoader

object Day01 {

    fun printSolution() {
        println("- part 1: ${part1("/day-01-input.txt")}")
        println("- part 2: ${part2("/day-01-input.txt")}")
        println("---")
    }

    private fun getTop3Descending(filePath: String): List<Int> {
        val allLines = FileLoader.loadFromFile<String>(filePath)
        val sums: MutableList<Int> = ArrayList()
        var sum = 0
        for (line in allLines) {
            if (isBlank(line)) {
                sums.add(sum)
                sum = 0
            } else {
                sum += line.toInt()
            }
        }

        return sums.stream()
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .mapToInt { obj: Int -> obj }
            .boxed().toList()
    }

    private fun part1(filePath: String): Int {
        return getTop3Descending(filePath)[0]
    }

    private fun part2(filePath: String): Int {
        return getTop3Descending(filePath).stream().mapToInt { obj: Int -> obj }.sum()
    }}
