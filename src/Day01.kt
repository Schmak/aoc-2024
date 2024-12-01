import kotlin.math.abs

object Day01 : Task<List<List<Int>>> {
    override fun parse(input: List<String>): List<List<Int>> =
        input.map { it.split("\\s+".toRegex()).map(String::toInt) }
            .fold(listOf(emptyList(), emptyList())) { acc, pair -> listOf(acc[0] + pair[0], acc[1] + pair[1]) }

    fun part1(input: List<List<Int>>): Int =
        input[0].sorted()
            .zip(input[1].sorted())
            .sumOf { abs(it.first - it.second) }

    fun part2(input: List<List<Int>>): Int {
        val freq = input[1].groupingBy { it }.eachCount()
        return input[0].sumOf { it * (freq[it] ?: 0) }
    }

    fun solve() {
        val testInput = readInput("Day01_test")
        assertThat(part1(testInput)).isEqualTo(11)
        assertThat(part2(testInput)).isEqualTo(31)

        val input = readInput("Day01")
        assertThat(part1(input)).isEqualTo(2815556)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(23927637)
        println(part2(input))
    }
}

fun main()  = Day01.solve()
