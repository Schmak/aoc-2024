import Day05.Input

object Day05 : Task<Input> {
    override fun parse(input: List<String>): Input {
        var rules = mutableMapOf<Int, Set<Int>>()
        var lines = mutableListOf<List<Int>>()
        for (line in input) {
            if ('|' in line) {
                val (first, second) = line.split("|").map { it.toInt() }
                rules.compute(first) { _, set -> set.orEmpty() + second }
            } else if (',' in line)
                lines += line.split(',').map { it.toInt() }
        }
        return Input(rules, lines)
    }

    val List<Int>.middle: Int get() = this[size/2]

    fun List<Int>.isSorted(greaterThan: Map<Int, Set<Int>>): Boolean {
        val previous = mutableSetOf<Int>()
        this.forEach { num ->
            if (!greaterThan[num]?.intersect(previous).isNullOrEmpty()) return false
            previous += num
        }
        return true
    }

    fun part1(input: Input): Int =
        input.lines
            .filter { it.isSorted(input.greaterThan) }
            .sumOf { it.middle }

    fun List<Int>.sort(greaterThan: Map<Int, Set<Int>>): List<Int>  =
        this.sortedWith { o1, o2 ->
            when {
                o2 in greaterThan[o1].orEmpty() -> -1
                o1 in greaterThan[o2].orEmpty() -> 1
                else -> 0
            }
        }

    fun part2(input: Input): Int =
        input.lines
            .filterNot{ it.isSorted(input.greaterThan)}
            .map { it.sort(input.greaterThan) }
            .sumOf { it.middle }

    fun solve() {
        val testInput = readInput("Day05_test")
        assertThat(part1(testInput)).isEqualTo(143)
        assertThat(part2(testInput)).isEqualTo(123)

        val input = readInput("Day05")
        assertThat(part1(input)).isEqualTo(6612)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(4944)
        println(part2(input))
    }

    data class Input(
        val greaterThan: Map<Int, Set<Int>>,
        val lines: List<List<Int>>,
    )
}

fun main() = Day05.solve()
