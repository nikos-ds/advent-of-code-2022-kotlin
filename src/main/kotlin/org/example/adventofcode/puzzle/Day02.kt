package org.example.adventofcode.puzzle

import org.apache.commons.lang3.StringUtils.isBlank
import org.example.adventofcode.puzzle.RockPaperScissors.PAPER
import org.example.adventofcode.puzzle.RockPaperScissors.ROCK
import org.example.adventofcode.puzzle.RockPaperScissors.SCISSORS
import org.example.adventofcode.util.FileLoader

private const val FILE_PATH = "/day-02-input.txt"

private val MAPPINGS: Map<String, RockPaperScissors> = mapOf("A" to ROCK, "B" to PAPER, "C" to SCISSORS)

enum class RockPaperScissors {
    ROCK {
        override val superior: RockPaperScissors
            get() = PAPER
        override val inferior: RockPaperScissors
            get() = SCISSORS
        override val score: Int
            get() = 1
    },
    PAPER {
        override val superior: RockPaperScissors
            get() = SCISSORS
        override val inferior: RockPaperScissors
            get() = ROCK
        override val score: Int
            get() = 2
    },
    SCISSORS {
        override val superior: RockPaperScissors
            get() = ROCK
        override val inferior: RockPaperScissors
            get() = PAPER
        override val score: Int
            get() = 3
    };

    abstract val superior: RockPaperScissors
    abstract val inferior: RockPaperScissors
    abstract val score: Int

    fun scoreAgainst(other: RockPaperScissors) = when (other) {
        superior -> 0
        inferior -> +6
        else -> +3
    }

}

object Day02 {

    fun printSolution() {
        println("- part 1: ${getTotalScore(Day02::part1Selector)}")
        println("- part 2: ${getTotalScore(Day02::part2Selector)}")
    }

    private fun getTotalScore(playerSelector: (String, RockPaperScissors) -> RockPaperScissors): Int {
        val allLines = FileLoader.loadFromFile<String>(FILE_PATH)
        var totalScore = 0;
        for (line in allLines) {
            if (isBlank(line)) {
                continue
            }
            val opponent: RockPaperScissors = MAPPINGS[line[0].toString()]!!
            val player: RockPaperScissors = playerSelector(line[2].toString(), opponent)
            totalScore += player.score
            totalScore += player.scoreAgainst(opponent)
        }

        return totalScore
    }

    private fun part1Selector(encodedSelection: String, opponent: RockPaperScissors): RockPaperScissors {
        return when (encodedSelection) {
            "X" -> ROCK
            "Y" -> PAPER
            else -> SCISSORS
        }
    }

    private fun part2Selector(encodedSelection: String, opponent: RockPaperScissors): RockPaperScissors {
        return when (encodedSelection) {
            "X" -> loseAgainst(opponent)
            "Y" -> opponent // draw
            else -> winAgainst(opponent)
        }
    }

    private fun loseAgainst(opponent: RockPaperScissors): RockPaperScissors {
        return when (opponent) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            else -> PAPER
        }
    }

    private fun winAgainst(opponent: RockPaperScissors): RockPaperScissors {
        return when (opponent) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            else -> ROCK
        }
    }
}
