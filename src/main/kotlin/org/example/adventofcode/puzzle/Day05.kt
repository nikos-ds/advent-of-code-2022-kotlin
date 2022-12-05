package org.example.adventofcode.puzzle

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.StringUtils.isBlank
import org.example.adventofcode.util.FileLoader

object Day05 {
    private const val FILE_PATH = "/day-05-input.txt"

    fun printSolution() {
        println("- part 1: ${getTopOfEachStack(Day05::part1CraneOperator)}")
        println("- part 2: ${getTopOfEachStack(Day05::part2CraneOperator)}")
    }

    private fun getTopOfEachStack(craneOperator: (ArrayList<ArrayDeque<Char>>, Int, Int, Int) -> Unit): List<Char> {
        val allLines = FileLoader.loadFromFile<String>(FILE_PATH)
        var stackHeaderLine = 0
        for (i in 0..allLines.size) {
            if (StringUtils.startsWith(allLines[i], " 1")) {
                stackHeaderLine = i
                break
            }
        }

        val numberOfStacks = allLines[stackHeaderLine].split("""\s+""".toRegex()).last().toInt()
        val stacks = ArrayList<ArrayDeque<Char>>()
        for (i in 1..numberOfStacks) {
            stacks.add(ArrayDeque())
        }

        for (lineNumber in stackHeaderLine - 1 downTo 0) {
            val currentLine = allLines[lineNumber]
            for (characterIndex in currentLine.indices) {
                if (!listOf('[', ']', ' ').contains(currentLine[characterIndex])) {
                    stacks[(characterIndex - 1) / 4].add(currentLine[characterIndex])
                }
            }
        }

        val rearrangementCommand = """move (\d+) from (\d+) to (\d+)""".toRegex()
        for (line in allLines) {
            if (isBlank(line)) {
                continue
            }
            if (rearrangementCommand.matches(line)) {
                val (howMany, fromStack, toStack) = rearrangementCommand.find(line)!!.destructured
                craneOperator(stacks, fromStack.toInt() - 1, toStack.toInt() - 1, howMany.toInt())
            }
        }

        val topOfEachStack = stacks.stream().map { stack -> stack.removeLast() }.toList()
        return topOfEachStack
    }

    private fun part1CraneOperator(
        stacks: ArrayList<ArrayDeque<Char>>,
        fromStack: Int,
        toStack: Int,
        howMany: Int
    ) {
        for (i in 1..howMany) {
            stacks[toStack].addLast(stacks[fromStack].removeLast())
        }
    }

    private fun part2CraneOperator(
        stacks: ArrayList<ArrayDeque<Char>>,
        fromStack: Int,
        toStack: Int,
        howMany: Int
    ) {
        val toIndex = stacks[fromStack].size
        val fromIndex = toIndex - howMany
        val cratesToBeMoved = stacks[fromStack].subList(fromIndex, toIndex)
        stacks[toStack].addAll(cratesToBeMoved)
        stacks[fromStack].subList(fromIndex, toIndex).clear()
    }

}
