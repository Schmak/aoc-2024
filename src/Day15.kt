import Day15.Input

typealias Field = Array<CharArray>

object Day15 : Task<Input> {
    override fun parse(input: List<String>): Input {
        val field = input.takeWhile { it.isNotEmpty() }
        val steps = input.dropWhile { it.isNotEmpty() }.drop(1).joinToString("")
        return Input(field, steps)
    }

    fun part1(input: Input): Int {
        val field = input.field.map { it.toCharArray() }.toTypedArray()

        object : StepProcessor(field) {
            override fun move(dx: Int, dy: Int) {
                var i = 1
                while (field[y + i * dy][x + i * dx] == 'O') i++
                if (field[y + i * dy][x + i * dx] == '#') return

                field[y + i * dy][x + i * dx] = 'O'
                field[y + dy][x + dx] = '@'
                field[y][x] = '.'

                x += dx
                y += dy
            }
        }.process(input.steps)
        return field.sum()
    }

    private fun Array<CharArray>.sum(): Int {
        var sum = 0
        forEachIndexed { py, line ->
            line.forEachIndexed { px, char ->
                if (char == 'O' || char == '[')
                    sum += 100 * py + px
            }
        }
        return sum
    }

    fun part2(input: Input): Int {
        val field = Array(input.field.size) { y ->
            input.field[y].asIterable().joinToString("") {
                when (it) {
                    '#', '.' -> "$it$it"
                    'O' -> "[]"
                    '@' -> "@."
                    else -> error("Unknown symbol '$it'")
                }
            }.toCharArray()
        }

        object : StepProcessor(field) {
            fun offset(char: Char): Int =
                if (char=='[') 1 else -1

            fun invert(char: Char): Char =
                if (char=='[') ']' else '['

            private fun tryMoveBox(x: Int, fromY: Int, dy: Int): Boolean {
                val toY = fromY + dy
                return when (val from = field[fromY][x]) {
                    '#' -> false
                    '.' -> true
                    '[', ']' -> tryMoveBox(x, toY, dy) && tryMoveBox(x + offset(from), toY, dy)
                    else -> error("Unknown symbol '$from'")
                }
            }

            private fun moveBox(x: Int, fromY: Int, dy: Int) {
                val from = field[fromY][x]
                val toY = fromY + dy
                when (from) {
                    '.' -> return
                    '@' -> {
                        moveBox(x, toY, dy)
                        field[fromY][x] = '.'
                        field[toY][x] = '@'
                        y += dy
                    }

                    '[', ']' -> {
                        val x2 = x + offset(from)
                        moveBox(x, toY, dy)
                        moveBox(x2, toY, dy)
                        field[fromY][x2] = '.'
                        field[toY][x] = from
                        field[toY][x2] = invert(from)
                    }
                }
            }

            override fun move(dx: Int, dy: Int) {
                if (dy == 0) {
                    var i = 1
                    while (field[y][x + i * dx] in setOf('[',']')) i++
                    if (field[y][x + i * dx] == '#') return

                    for (j in i downTo 1)
                        field[y][x + j * dx] = field[y][x + (j - 1) * dx]
                    field[y][x] = '.'
                    x += dx
                } else {
                    if (tryMoveBox(x, y + dy, dy = dy))
                        moveBox(x, y, dy = dy)
                }
            }
        }.process(input.steps)

        return field.sum()
    }

    abstract class StepProcessor(field: Field) {
        protected var y: Int = field.indexOfFirst { '@' in it }
        protected var x: Int = field[y].indexOfFirst { it == '@' }

        abstract fun move(dx: Int, dy: Int)

        fun process(steps: String) {
            steps.forEach {
                when (it) {
                    'v' -> move(0, 1)
                    '^' -> move(0, -1)
                    '<' -> move(-1, 0)
                    '>' -> move(1, 0)
                }
            }
        }
    }

    fun solve() {
        val testInput1 = readInput("Day15_test1")
        assertThat(part1(testInput1)).isEqualTo(2028)


        val testInput2 = readInput("Day15_test2")
        assertThat(part1(testInput2)).isEqualTo(10092)
        assertThat(part2(testInput2)).isEqualTo(9021)


        val input = readInput("Day15")
        assertThat(part1(input)).isEqualTo(1471826)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(1457703)
        println(part2(input))
    }

    data class Input(
        val field: List<String>,
        val steps: String,
    )
}

fun main() = Day15.solve()
