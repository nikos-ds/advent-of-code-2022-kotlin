package org.example.adventofcode.puzzle

import org.example.adventofcode.util.FileLoader

object Day03 {
    private const val FILE_PATH = "/day-03-input.txt"

    fun printSolution() {
        println("- part 1: ${part1()}")
        println("- part 2: ${part2()}")
    }

    private fun part1(): Int {
        val allLines = FileLoader.loadFromFile<String>(FILE_PATH)
        val duplicateItems: MutableList<Char> = mutableListOf()
        for (line in allLines) {
            duplicateItems.add(
                line.substring(0, line.length / 2).toSet()
                    .intersect(line.substring(line.length / 2, line.length).toSet())
                    .first()
            )
        }

        return duplicateItems.sumOf { c -> priority(c) }
    }

    private fun part2(): Int {
        val allLines = FileLoader.loadFromFile<String>(FILE_PATH)
        val duplicateItems: MutableList<Char> = mutableListOf()
        for (i in allLines.indices step 3) {
            duplicateItems.add(
                allLines[i].toSet()
                    .intersect(allLines[i+1].toSet())
                    .intersect(allLines[i+2].toSet())
                    .first()
            )
        }

        return duplicateItems.sumOf { c -> priority(c) }
    }

    private fun priority(input: Char): Int {
        return if (input in 'A'..'Z') {
            input.code - 38
        } else {
            input.code - 96
        }
    }

}