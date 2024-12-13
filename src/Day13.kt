import Day13.Round

object Day13 : Task<List<Round>> {
    private val regex = """\D+(\d+)\D+(\d+)""".toRegex()

    private fun parse(line: String): Pair<Long, Long> {
        val (x, y) = requireNotNull(regex.matchEntire(line)).destructured
        return x.toLong() to y.toLong()
    }

    override fun parse(input: List<String>): List<Round> =
        input.chunked(4).map { (a, b, p, _) ->
            val (a1, a2) = parse(a)
            val (b1, b2) = parse(b)
            val (c1, c2) = parse(p)
            Round(a1, a2, b1, b2, c1, c2)
        }


    fun solve(round: Round): Long = with(round) {
        val d = a2 * b1 - a1 * b2
        val dx = a2 * c1 - a1 * c2
        val dy = b1 * c2 - b2 * c1

        if (dx % d != 0L || dy % d != 0L) return 0
        return (dx + 3 * dy) / d
    }

    fun part1(rounds: List<Round>): Long = rounds.sumOf(::solve)

    private const val DX = 10_000_000_000_000L
    fun part2(rounds: List<Round>): Long =
        rounds.map { it.copy(c1 = it.c1 + DX, c2 = it.c2 + DX) }.sumOf(::solve)

    fun solve() {
        val testInput = readInput("Day13_test")
        assertThat(part1(testInput)).isEqualTo(480)
        assertThat(part2(testInput)).isEqualTo(875318608908)

        val input = readInput("Day13")
        assertThat(part1(input)).isEqualTo(33209)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(83102355665474)
        println(part2(input))
    }

    data class Round(
        val a1: Long, val a2: Long,
        val b1: Long, val b2: Long,
        val c1: Long, val c2: Long,
    )
}

fun main() = Day13.solve()
