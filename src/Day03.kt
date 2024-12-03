object Day03 : Task<String> {
    override fun parse(input: List<String>) = input.joinToString(" ")

    val mul_regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    val do_regex = """don't\(\).+?(?:do\(\)|${'$'})""".toRegex()

    fun part1(input: String) =
        mul_regex.findAll(input).sumOf { it.groupValues[1].toLong() * it.groupValues[2].toLong() }

    fun part2(input: String) : Long =
        part1(do_regex.replace(input, "_"))

    fun solve() {
        val testInput = readInput("Day03_test")
        assertThat(part1(testInput)).isEqualTo(161)
        assertThat(part2(testInput)).isEqualTo(48)

        val input = readInput("Day03")
        assertThat(part1(input)).isEqualTo(174103751)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(100411201)
        println(part2(input))
    }
}

fun main() = Day03.solve()
