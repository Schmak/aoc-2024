import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): List<List<Long>> =
        input.map { it.split("\\s+".toRegex()).map(String::toLong) }
            .fold(listOf(emptyList(), emptyList())) { acc, pair -> listOf(acc[0] + pair[0], acc[1] + pair[1]) }

    fun part1(input: List<List<Long>>): Long =
        input[0].sorted()
            .zip(input[1].sorted())
            .sumOf { abs(it.first - it.second) }

    fun part2(input: List<List<Long>>): Long {
        val freq = input[1].groupingBy { it }.eachCount()
        return input[0].sumOf { it * (freq[it] ?: 0) }
    }

    val testInput = readInput("Day01_test")
    check(part1(parse(testInput)) == 11L)
    check(part2(parse(testInput)) == 31L)

    val input = readInput("Day01")
    println(part1(parse(input)))
    println(part2(parse(input)))
}
