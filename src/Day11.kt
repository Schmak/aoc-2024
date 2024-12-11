import kotlin.math.log10
import kotlin.math.pow

typealias Stones = Map<Long, Long>

object Day11 : Task<Stones> {
    override fun parse(input: List<String>) =
        input.first().split(" ").map(String::toLong).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    val Long.length: Int
        get() = log10(this.toDouble()).toInt() + 1

    fun Long.split(length: Int): Pair<Long, Long> {
        val d = 10.0.pow(length.toDouble()).toLong()
        return this / d to this % d
    }

    fun Stones.count() = values.sum()

    fun process(stones: Stones) : Stones = buildMap {
        fun add(value: Long, count: Long) = compute(value) { _, old -> (old ?: 0) + count }

        stones.forEach { (value, count) ->
            if (value == 0L) {
                add(1, count)
            } else {
                val len = value.length
                if (len % 2 == 0) {
                    val (a,b) = value.split(len / 2)
                    add(a, count)
                    add(b, count)
                } else {
                    add(value * 2024, count)
                }
            }
        }
    }

    private fun solve(stones: Stones, iterations: Int): Long {
        var stones = stones
        repeat(iterations) {
            stones = process(stones)
        }
        return stones.count()
    }

    fun part1(stones: Stones) = solve(stones, 25)

    fun part2(stones: Stones) = solve(stones, 75)

    fun solve() {
        val testInput = readInput("Day11_test")
        assertThat(part1(testInput)).isEqualTo(55312)

        val input = readInput("Day11")
        assertThat(part1(input)).isEqualTo(199946)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(237994815702032)
        println(part2(input))
    }
}

fun main() = Day11.solve()
