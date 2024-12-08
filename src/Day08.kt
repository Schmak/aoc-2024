import Day08.Input

object Day08 : Task<Input> {
    override fun parse(input: List<String>): Input =
        Input(
            width = input.first().length,
            height = input.size,
            antennas = buildMap {
                input.forEachIndexed { y, line ->
                    line.forEachIndexed { x, c ->
                        if (c != '.')
                            compute(c) { c, list -> list.orEmpty() + Point(x, y) }
                    }
                }
            }
        )

    private fun process(input: Input, limited: Boolean): Int = buildSet<Point> {
        fun addAntinode(point: Point): Boolean {
            if (point !in input) return false

            add(point)
            return true
        }

        input.antennas.forEach { (_, points) ->
            for (p1 in points)
                for (p2 in points)
                    if (p1 != p2) {
                        val v = p2 - p1
                        if (limited) {
                            addAntinode(p2 + v)
                        } else {
                            var p = p2
                            while (addAntinode(p))
                                p += v
                        }
                    }
        }
    }.size

    fun part1(input: Input): Int = process(input, limited = true)

    fun part2(input: Input): Int = process(input, limited = false)

    fun solve() {
        val testInput = readInput("Day08_test")
        assertThat(part1(testInput)).isEqualTo(14)
        assertThat(part2(testInput)).isEqualTo(34)

        val input = readInput("Day08")
        assertThat(part1(input)).isEqualTo(364)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(1231)
        println(part2(input))
    }

    data class Input(
        val width: Int,
        val height: Int,
        val antennas: Map<Char, List<Point>>,
    ) {
        operator fun contains(point: Point) =
            point.x in 0 until width && point.y in 0 until height
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(v: Vector) = Point(x + v.x, y + v.y)

        operator fun minus(v: Vector) = Point(x - v.x, y - v.y)

        operator fun minus(p: Point) = Vector(x - p.x, y - p.y)
    }

    data class Vector(val x: Int, val y: Int)
}

fun main() = Day08.solve()
