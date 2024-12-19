import Day19.Input

object Day19 : Task<Input> {
    override fun parse(input: List<String>): Input =
        Input(
            words = input.first().split(",").map { it.trim() },
            lines = input.drop(2),
        )

    fun solve(words: List<String>, line: String): Long {
        val dp = LongArray(line.length + 1)
        dp[0] = 1
        for (i in line.indices)
            for (word in words)
                if (line.startsWith(word, i))
                    dp[i + word.length] += dp[i]
        return dp.last()
    }

    fun part1(input: Input): Int = input.lines.count { solve(input.words, it) != 0L }

    fun part2(input: Input): Long = input.lines.sumOf { solve(input.words, it) }

    fun solve() {
        val testInput = readInput("Day19_test")
        assertThat(part1(testInput)).isEqualTo(6)
        assertThat(part2(testInput)).isEqualTo(16)

        val input = readInput("Day19")
        assertThat(part1(input)).isEqualTo(272)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(1041529704688380)
        println(part2(input))
    }

    data class Input(
        val words: List<String>,
        val lines: List<String>,
    )
}

fun main() = Day19.solve()
