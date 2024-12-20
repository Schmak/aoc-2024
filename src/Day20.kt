import Day20.Input
import kotlin.math.abs

object Day20 : Task<Input> {
    override fun parse(input: List<String>): Input {
        fun find(c: Char): Point {
            val y = input.indexOfFirst { c in it }
            val x = input[y].indexOf(c)
            return Point(x - 1, y - 1)
        }

        return Input(
            field = input.drop(1).dropLast(1).map { it.drop(1).dropLast(1) },
            start = find('S'),
            end = find('E')
        )
    }

    fun solve(input: Input, maxDistance: Int) = sequence {
        fun get(point: Point): Char = input.field.getOrNull(point.y)?.getOrNull(point.x) ?: '#'

        val map = mutableMapOf<Point, Int>()

        map[input.start] = 1
        var current = input.start
        var distance = 1
        while (current != input.end) {
            vectors.forEach { v ->
                val next = current + v
                if (next !in map && get(next) != '#') {
                    current = next
                    map[current] = ++distance
                }
            }
        }

        map.forEach { (point, mileage) ->
            map.forEach { (other, otherMileage) ->
                val dist = abs(point.x - other.x) + abs(point.y - other.y)
                if (dist <= maxDistance && mileage + dist < otherMileage)
                    yield(otherMileage - mileage - dist)
            }
        }
    }

    fun part1(input: Input): Int = solve(input, 2).count { it >= 100 }

    fun part2(input: Input): Int = solve(input, 20).count { it >= 100 }


    fun solve() {
        val testInput = readInput("Day20_test")

        val grouped = solve(testInput, 2).groupingBy { it }.eachCount()
        assertThat(grouped).isEqualTo(
            mapOf(
                2 to 14, 4 to 14, 6 to 2, 8 to 4, 10 to 2, 12 to 3,
                20 to 1, 36 to 1, 38 to 1, 40 to 1, 64 to 1
            )
        )
        val grouped2 = solve(testInput, 20).filter { it>=50 }.groupingBy { it }.eachCount()
        assertThat(grouped2).isEqualTo(
            mapOf(
                50 to 32,
                52 to 31,
                54 to 29,
                56 to 39,
                58 to 25,
                60 to 23,
                62 to 20,
                64 to 19,
                66 to 12,
                68 to 14,
                70 to 12,
                72 to 22,
                74 to 4,
                76 to 3,
            )
        )

        val input = readInput("Day20")
        assertThat(part1(input)).isEqualTo(1499)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(1027164)
        println(part2(input))
    }


    data class Point(val x: Int, val y: Int) {
        operator fun plus(v: Vector) = Point(x + v.x, y + v.y)
    }

    data class Vector(val x: Int, val y: Int) {
        operator fun times(n: Int) = Vector(x * n, y * n)
    }

    private val vectors = listOf(Vector(1, 0), Vector(0, 1), Vector(-1, 0), Vector(0, -1))

    data class Input(
        val field: List<String>,
        val start: Point,
        val end: Point,
    )
}

fun main() = Day20.solve()
