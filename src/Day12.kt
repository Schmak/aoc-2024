import Day12.Field

object Day12 : Task<Field> {
    override fun parse(input: List<String>) = Field(input)

    fun solve(field: Field): Sequence<Result> = sequence {
        val gardens = (0 until field.width).flatMap { x ->
            (0 until field.height).map { y -> Point(x, y) }
        }.toMutableSet()

        val queue = ArrayDeque<Point>()
        while (gardens.isNotEmpty()) {
            var area = 0
            var perimeter = 0
            var sides = 0

            val start = gardens.first()
            val type = field[start]
            gardens -= start
            queue.add(start)
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                area++
                for (v in vectors) {
                    val next = current + v
                    if (field[next] != type) {
                        perimeter++
                        val neighbor = current + v.rotate()
                        if (field[neighbor] != type || field[neighbor + v] == type)
                            sides++
                    } else if (next in gardens) {
                        gardens -= next
                        queue.add(next)
                    }
                }
            }
            yield(Result(type = type, area = area, perimeter = perimeter, sides = sides))
        }
    }

    fun part1(field: Field): Int = solve(field).sumOf { it.area * it.perimeter }

    fun part2(field: Field): Int = solve(field).sumOf { it.area * it.sides }

    fun solve() {
        val testInput = readInput("Day12_test")
        assertThat(part1(testInput)).isEqualTo(1930)
        assertThat(part2(testInput)).isEqualTo(1206)

        val input = readInput("Day12")
        assertThat(part1(input)).isEqualTo(1485656)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(899196)
        println(part2(input))
    }

    data class Field(private val input: List<String>) {
        val height by lazy { input.size }
        val width by lazy { input[0].length }

        operator fun get(p: Point) = input.getOrNull(p.y)?.getOrNull(p.x) ?: '.'
    }

    data class Point(val x: Int, val y: Int) {
        operator fun plus(v: Vector) = Point(x + v.x, y + v.y)
    }

    data class Vector(val x: Int, val y: Int) {
        fun rotate() = Vector(-y, x)
    }

    data class Result(
        val type: Char,
        val area: Int,
        val perimeter: Int,
        val sides: Int
    )

    val vectors = listOf(Vector(1, 0), Vector(0, 1), Vector(-1, 0), Vector(0, -1))
}

fun main() = Day12.solve()
