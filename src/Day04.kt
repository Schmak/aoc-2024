object Day04 : Task<List<String>> {


    override fun parse(input: List<String>) = input

    fun part1(input: List<String>): Int {
        fun get(x: Int, y: Int) = input.getOrNull(y)?.getOrNull(x) ?: '.'

        var total = 0
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 'X') {
                    for (xd in -1..1) {
                        for (yd in -1..1) {
                            if (get(x + xd, y + yd) == 'M' &&
                                get(x + 2 * xd, y + 2 * yd) == 'A' &&
                                get(x + 3 * xd, y + 3 * yd) == 'S'
                            ) {
                                total++
                            }
                        }
                    }
                }
            }
        }
        return total
    }

    fun part2(input: List<String>): Int {
        fun get(x: Int, y: Int) = input.getOrNull(y)?.getOrNull(x) ?: '.'

        var total = 0
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == 'A') {
                    var count = 0
                    for (xd in setOf(-1, 1)) {
                        for (yd in setOf(-1, 1)) {
                            if (get(x + xd, y + yd) == 'M' && get(x - xd, y - yd) == 'S')
                                count++
                        }
                    }
                    if (count == 2) total++
                }
            }
        }
        return total
    }


    fun solve() {
        val testInput = readInput("Day04_test")
        assertThat(part1(testInput)).isEqualTo(18)
        assertThat(part2(testInput)).isEqualTo(9)

        val input = readInput("Day04")
        assertThat(part1(input)).isEqualTo(2618)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(2011)
        println(part2(input))
    }
}

fun main() = Day04.solve()
