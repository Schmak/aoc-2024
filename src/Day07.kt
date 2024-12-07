import Day07.OperatorType.*
import kotlin.math.pow


object Day07 : Task<List<List<Long>>> {
    override fun parse(input: List<String>): List<List<Long>> =
        input.map { line ->
            line.split(" ").map { it.trim(':').toLong() }
        }

    fun trySolve(expectedResult: Long, operands: List<Long>, operators: Array<OperatorType>): Boolean {
        var result = operands.first()
        for (i in 1 until operands.size) {
            when (operators[i - 1]) {
                ADD -> result += operands[i]
                MUL -> result *= operands[i]
                CONCAT -> result = (result.toString() + operands[i]).toLong()
            }
            if (result > expectedResult) return false
        }
        return result == expectedResult
    }

    fun isSolvable(list: List<Long>, operatorTypes: List<OperatorType>): Boolean {
        val expectedResult = list.first()
        val operators = list.size - 2
        operatorSequence(operatorTypes, operators).forEach {
            if (trySolve(expectedResult, list.drop(1), it))
                return true
        }
        return false
    }

    fun part1(input: List<List<Long>>): Long =
        input.filter { isSolvable(it, listOf(ADD, MUL)) }.sumOf { it.first() }

    fun part2(input: List<List<Long>>): Long =
        input.filter { isSolvable(it, OperatorType.entries) }.sumOf { it.first() }

    fun solve() {
        val testInput = readInput("Day07_test")
        assertThat(part1(testInput)).isEqualTo(3749)
        assertThat(part2(testInput)).isEqualTo(11387)

        val input = readInput("Day07")
        assertThat(part1(input)).isEqualTo(465126289353)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(70597497486371)
        println(part2(input))
    }

    fun operatorSequence(operators: List<OperatorType>, size: Int): Sequence<Array<OperatorType>> = sequence {
        val total = operators.size.toDouble().pow(size).toInt()
        repeat(total) { i ->
            var idx = i
            val sequence = Array(size) { operators[idx % operators.size].also { idx /= operators.size } }
            yield(sequence)
        }
    }

    enum class OperatorType { ADD, MUL, CONCAT }
}

fun main() = Day07.solve()
