object Day10 : Task<Array<IntArray>> {
    override fun parse(input: List<String>) =
        Array(input.size) { y -> IntArray(input[y].length) { input[y][it].digitToInt() } }

    fun part1(input: Array<IntArray>): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        return solve(
            input,
            onEachPosition = { visited.clear() },
            scoreFinal = { x, y ->
                if ((x to y) in visited) {
                    0
                } else {
                    visited += x to y
                    1
                }
            }
        )
    }

    fun part2(input: Array<IntArray>): Int = solve(input)

    fun solve() {
        val testInput = readInput("Day10_test")
        assertThat(part1(testInput)).isEqualTo(36)
        assertThat(part2(testInput)).isEqualTo(81)

        val input = readInput("Day10")
        assertThat(part1(input)).isEqualTo(782)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(1694)
        println(part2(input))
    }


    fun solve(
        input: Array<IntArray>,
        onEachPosition: () -> Unit = {},
        scoreFinal: (x: Int, y: Int) -> Int = { _, _ -> 1 },
    ): Int {
        fun score(x: Int, y: Int, expected: Int): Int {
            fun get(x: Int, y: Int) = input.getOrNull(y)?.getOrNull(x) ?: -1

            val value = get(x, y)
            if (value != expected) return 0
            if (value == 9) return scoreFinal(x, y)

            val nextValue = value + 1
            return score(x - 1, y, nextValue) + score(x + 1, y, nextValue) +
                    score(x, y - 1, nextValue) + score(x, y + 1, nextValue)
        }

        val width = input.first().size
        return input.indices.sumOf { y ->
            (0 until width).sumOf { x -> onEachPosition(); score(x, y, 0) }
        }
    }

}

fun main() = Day10.solve()
