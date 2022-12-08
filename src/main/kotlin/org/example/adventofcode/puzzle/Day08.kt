package org.example.adventofcode.puzzle

import org.example.adventofcode.util.FileLoader
import java.util.stream.IntStream

object Day08 {
    private const val FILE_PATH = "/day-08-input.txt"

    fun printSolution() {
        println("- part 1: ${getResult(Day08::part1Calculator)}")
        println("- part 2: ${getResult(Day08::part2Calculator)}")
    }

    private fun getResult(calculator: (Set<String>, List<List<Int>>) -> Int): Int {
        val allLines = FileLoader.loadFromFile<String>(FILE_PATH).stream()
            .map { it.toCharArray() }
            .map { it -> it.map { it.digitToInt() } }
            .toList()

        val allColumns = IntStream.range(0, allLines[0].size)
            .mapToObj { i -> allLines.stream().map { l -> l[i] }.toList() }
            .toList()

        val visibleInteriorTrees: Set<String> = getVisibleInteriorTrees(allLines, allColumns)

        return calculator(visibleInteriorTrees, allLines)
    }

    private fun part1Calculator(
        visibleInteriorTrees: Set<String>,
        allLines: List<List<Int>>
    ) = visibleInteriorTrees.size + 2 * allLines[0].size + 2 * allLines.size - 4

    private fun part2Calculator(
        visibleInteriorTrees: Set<String>,
        allLines: List<List<Int>>
    ): Int = visibleInteriorTrees.stream()
        .map { treeCoords -> calculateVisibilityScore(treeCoords, allLines) }
        .toList().max()

    private fun calculateVisibilityScore(treeCoords: String, allLines: List<List<Int>>): Int {
        val (x, y) = treeCoords.split(",").map { it.toInt() }
        val currentTreeHeight = allLines[x][y]
        var viewingDistanceLeft = 0
        var viewingDistanceRight = 0
        var viewingDistanceTop = 0
        var viewingDistanceBottom = 0

        for (i in y - 1 downTo 0) {
            viewingDistanceLeft += 1
            if (allLines[x][i] >= currentTreeHeight) {
                break
            }
        }

        for (i in y + 1 until allLines.size) {
            viewingDistanceRight += 1
            if (allLines[x][i] >= currentTreeHeight) {
                break
            }
        }

        for (i in x - 1 downTo 0) {
            viewingDistanceTop += 1
            if (allLines[i][y] >= currentTreeHeight) {
                break
            }
        }

        for (i in x + 1 until allLines[0].size) {
            viewingDistanceBottom += 1
            if (allLines[i][y] >= currentTreeHeight) {
                break
            }
        }

        return viewingDistanceLeft * viewingDistanceRight * viewingDistanceTop * viewingDistanceBottom
    }

    private fun getVisibleInteriorTrees(
        allLines: MutableList<List<Int>>,
        allColumns: MutableList<MutableList<Int>>
    ): Set<String> {
        val maxRowIndex = allLines[0].size - 1
        val maxColumnIndex = allLines.size - 1

        val visibleInteriorTrees: MutableSet<String> = mutableSetOf()
        for (x in 1 until maxColumnIndex) {
            for (y in 1 until maxRowIndex) {
                val currentRow = allLines[x]
                val currentColumn = allColumns[y]
                val currentTreeHeight = currentRow[y]

                val maxHeightLeft = currentRow.subList(0, y).max()
                val maxHeightRight = currentRow.subList(y + 1, currentRow.size).max()
                val maxHeightTop = currentColumn.subList(0, x).max()
                val maxHeightBottom = currentColumn.subList(x + 1, currentColumn.size).max()

                val minimumHeightForVisibility =
                    listOf(maxHeightLeft, maxHeightRight, maxHeightTop, maxHeightBottom).min()
                if (currentTreeHeight > minimumHeightForVisibility) {
                    visibleInteriorTrees.add("${x},${y}")
                }
            }
        }
        return visibleInteriorTrees
    }
}