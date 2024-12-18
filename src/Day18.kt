import Day18.Point

object Day18 : Task<List<Point>> {
    override fun parse(input: List<String>): List<Point> =
        input.map { it.split(",").map(String::toInt).let { (x, y) -> Point(x, y) } }

    fun part1(input: List<Point>, steps: Int, finishCoordinate: Int): Int =
        solve(input, steps, finishCoordinate)

    private fun solve(input: List<Point>, steps: Int, finishCoordinate: Int): Int {
        val corrupted = input.take(steps).toSet()
        fun isCorrupted(point: Point) =
            point.x !in 0..finishCoordinate || point.y !in 0..finishCoordinate || point in corrupted

        val visited = mutableSetOf<Point>()
        val finish = Point(finishCoordinate, finishCoordinate)
        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.add(Point(0, 0) to 0)
        while (queue.isNotEmpty()) {
            val (point, distance) = queue.removeFirst()
            vectors.forEach { v ->
                val next = point + v
                if (!isCorrupted(next) && next !in visited) {
                    if (next == finish) return distance + 1
                    visited += next
                    queue.add(next to distance + 1)
                }
            }
        }
        return -1
    }

    fun part2(input: List<Point>, finishCoordinate: Int): Point {
        var left = 1
        var right = input.size
        while (left < right) {
            val middle = (left + right) / 2
            if (solve(input, middle, finishCoordinate) == -1) {
                right = middle
            } else {
                left = middle + 1
            }
        }
        return input[left - 1]
    }

    fun solve() {
        val testInput = readInput("Day18_test")
        assertThat(part1(testInput, 12, 6)).isEqualTo(22)
        assertThat(part2(testInput, 6)).isEqualTo(Point(6, 1))

        val input = readInput("Day18")
        val part1Input = part1(input, 1024, 70)
        assertThat(part1Input).isEqualTo(302)
        println(part1Input)

        val part2Result = part2(input, 70)
        assertThat(part2Result).isEqualTo(Point(24, 32))
        println(part2Result)
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(v: Vector) = Point(x + v.x, y + v.y)
        override fun toString(): String = "$x,$y"
    }

    data class Vector(val x: Int, val y: Int)

    private val vectors = listOf(Vector(1, 0), Vector(0, 1), Vector(-1, 0), Vector(0, -1))
}

fun main() = Day18.solve()
