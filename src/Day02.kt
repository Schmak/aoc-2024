object Day02 : Task<List<List<Int>>> {
    override fun parse(input: List<String>) =
        input.map { it.split("\\s+".toRegex()).map(String::toInt) }

    fun isSafe(list: List<Int>): Boolean {
        val diffs = list.zipWithNext { a, b -> b - a }
        return when {
            diffs[0] > 0 -> diffs.all { it in 1..3 }
            diffs[0] < 0 -> diffs.all { it in -3..-1 }
            else -> false
        }
    }

    fun List<Int>.without(index: Int) = take(index) + drop(index + 1)

    fun isAlmostSafe(list: List<Int>): Boolean =
        list.indices.any { idx -> isSafe(list.without(idx)) }

    fun part1(input: List<List<Int>>) = input.count(::isSafe)

    fun part2(input: List<List<Int>>) = input.count(::isAlmostSafe)

    fun solve() {
        val testInput = readInput("Day02_test")
        assertThat(part1(testInput)).isEqualTo(2)
        assertThat(part2(testInput)).isEqualTo(4)

        val input = readInput("Day02")
        assertThat(part1(input)).isEqualTo(224)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(293)
        println(part2(input))
    }
}

fun main() = Day02.solve()
