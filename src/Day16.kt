import Day16.Input
import java.util.*

object Day16 : Task<Input> {
    override fun parse(input: List<String>): Input {
        fun find(c: Char): Point {
            val y = input.indexOfFirst { it.contains(c) }
            val x = input[y].indexOf(c)
            return Point(x, y)
        }

        val start = find('S')
        val end = find('E')

        return Input(input.map { it.replace("[SE]".toRegex(), ".") }, start, end)
    }

    fun part1(input: Input): Int = solve(input).first

    fun part2(input: Input): Int = solve(input).second

    fun solve(input: Input): Pair<Int, Int> {
        fun get(point: Point): Char = input.field[point.y][point.x]

        val previous = mutableMapOf<State, Set<State>>()
        val startState = State(input.start, Vector(-1, 0))
        val distances = mutableMapOf(startState to 0)
        val queue = PriorityQueue<Pair<State, Int>>(compareBy { it.second })
        queue.add(startState to 0)

        fun addIfLess(state: State, distance: Int, fromState: State) {
            val prevDistance = distances[state] ?: Int.MAX_VALUE
            if (distance == prevDistance) {
                previous[state] = previous[state].orEmpty() + fromState
            } else if (distance < prevDistance) {
                previous[state] = setOf(fromState)
                distances[state] = distance
                queue.add(state to distance)
            }
        }

        while (queue.isNotEmpty()) {
            val (state, distance) = queue.poll()
            if (state.point == input.end) {
                val visited = mutableSetOf<State>()
                fun dfs(state: State) {
                    if (state in visited) return
                    visited += state
                    previous[state]?.forEach { dfs(it) }
                }
                dfs(state)
                val count = visited.distinctBy { it.point }.count()
                return distance to count
            }

            val neighbor = State(state.point + state.direction, state.direction)
            if (get(neighbor.point) == '.')
                addIfLess(neighbor, distance + 1, state)
            addIfLess(state.copy(direction = state.direction.rotateClockwise()), distance + 1000, state)
            addIfLess(state.copy(direction = state.direction.rotateAnticlockwise()), distance + 1000, state)
        }
        error("No path found")
    }

    fun solve() {
        val testInput1 = readInput("Day16_test1")
        assertThat(part1(testInput1)).isEqualTo(7036)


        val testInput2 = readInput("Day16_test2")
        assertThat(part1(testInput2)).isEqualTo(11048)

        val input = readInput("Day16")
        assertThat(part1(input)).isEqualTo(75416)
        println(part1(input))

        assertThat(part2(testInput1)).isEqualTo(45)
        assertThat(part2(testInput2)).isEqualTo(64)

        assertThat(part2(input)).isEqualTo(476)
        println(part2(input))
    }

    data class Input(
        val field: List<String>,
        val start: Point,
        val end: Point,
    )

    data class State(
        val point: Point,
        val direction: Vector,
    )

    data class Point(val x: Int, val y: Int) {
        operator fun plus(v: Vector) = Point(x + v.x, y + v.y)
    }

    data class Vector(val x: Int, val y: Int) {
        fun rotateClockwise() = Vector(y, -x)

        fun rotateAnticlockwise() = Vector(-y, x)
    }
}

fun main() = Day16.solve()
