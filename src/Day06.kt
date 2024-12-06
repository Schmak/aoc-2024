object Day06 : Task<List<CharArray>> {
    override fun parse(input: List<String>): List<CharArray> = input.map { it.toCharArray() }

    fun getStartPosition(input: List<CharArray>): Vector {
        val startY = input.indexOfFirst { '^' in it }
        val startX = input[startY].indexOf('^')
        return Vector(startX, startY)
    }

    fun process(input: List<CharArray>): List<CharArray>? {
        fun get(v: Vector) = input.getOrNull(v.y)?.getOrNull(v.x)

        var position = getStartPosition(input)
        var vector = Vector(0, -1)

        val input = input.map { it.copyOf() }

        val visited = mutableSetOf<Pair<Vector, Vector>>()
        while (true) {
            if (position to vector in visited) return null
            visited.add(position to vector)

            input[position.y][position.x] = 'X'
            val newPosition = position + vector
            when (get(newPosition)) {
                null -> break
                '#' -> vector = vector.rotateRight()
                else -> position = newPosition
            }
        }
        return input
    }

    fun part1(input: List<CharArray>): Int = process(input)?.sumOf { line -> line.count { it == 'X' } } ?: 0

    fun part2(input: List<CharArray>): Int {
        val processed = process(input) ?: return -1
        val startPosition = getStartPosition(input)
        processed[startPosition.y][startPosition.x] = '^'

        return processed.indices.sumOf { y ->
            processed[y].indices.sumOf { x ->
                var result = 0
                if (processed[y][x] == 'X') {
                    input[y][x] = '#'
                    if (process(input) == null) result = 1
                    input[y][x] = '.'
                } else 0
                result
            }
        }
    }

    fun solve() {
        val testInput = readInput("Day06_test")
        assertThat(part1(testInput)).isEqualTo(41)
        assertThat(part2(testInput)).isEqualTo(6)

        val input = readInput("Day06")
        assertThat(part1(input)).isEqualTo(5199)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(1915)
        println(part2(input))
    }
}

data class Vector(val x: Int, val y: Int) {
    fun rotateRight() = Vector(-y, x)

    operator fun plus(v: Vector) = Vector(x + v.x, y + v.y)
}

fun main() = Day06.solve()
