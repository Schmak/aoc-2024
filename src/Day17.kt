import Day17.Input

object Day17 : Task<Input> {
    override fun parse(input: List<String>): Input {
        fun data(idx: Int) = input[idx].split(" ").last()
        return Input(
            a = data(0).toLong(),
            b = data(1).toLong(),
            c = data(2).toLong(),
            program = data(4).split(",").map { it.toLong() }
        )
    }

    private fun execute(input: Input, shouldHalt: (Long) -> Boolean = { false }): List<Long> = buildList {
        var ip = 0
        var a = input.a
        var b = input.b
        var c = input.c

        fun combo(id: Long): Long = when (id) {
            in 0L..3L -> id
            4L -> a
            5L -> b
            6L -> c
            else -> error("Unknown id $id")
        }

        while (ip < input.program.size) {
            val op = input.program[ip]
            val operand = input.program[ip + 1]
            when (op) {
                0L -> a /= (1 shl combo(operand).toInt())
                1L -> b = b xor operand
                2L -> b = combo(operand) and 0b111
                3L -> {
                    if (a != 0L) {
                        ip = operand.toInt()
                        continue
                    }
                }

                4L -> b = b xor c
                5L -> {
                    val newElement = combo(operand) and 0b111
                    add(newElement)
                    if (shouldHalt(newElement))
                        break
                }

                6L -> b = a / (1 shl combo(operand).toInt())
                7L -> c = a / (1 shl combo(operand).toInt())
            }
            ip += 2
        }
    }

    fun part1(input: Input): String = execute(input).joinToString(",")

    fun part2(input: Input, transform: (Long) -> List<Long> = { listOf(it) }): Long =
        Solver(input, transform = transform).findAll().first()

    private class Solver(
        val input: Input,
        val endIdx: Int = input.program.size,
        val transform: (Long) -> List<Long> = { listOf(it) }
    ) {
        fun findAll() = sequence {
            generateSequence(0L) { it + 1 }.flatMap(transform).forEach {
                var idx = 0
                val result = execute(
                    input = input.copy(a = it),
                    shouldHalt = { newElement ->
                        input.program.getOrNull(idx++) != newElement || idx == endIdx
                    }
                )
                if (result == input.program.subList(0, endIdx))
                    yield(it)
            }
        }
    }

    fun Sequence<Long>.onEachDump() =
        onEach {
            println(
                "${it.toString().padStart(16, ' ')}  | ${it.toString(16).padStart(16, ' ')}"
            )
        }

    fun solve() {
        val testInput = readInput("Day17_test")
        assertThat(part1(testInput)).isEqualTo("4,6,3,5,6,3,5,2,1,0")

        val input = readInput("Day17")
        assertThat(part1(input)).isEqualTo("7,3,0,5,7,1,4,0,5")
        println(part1(input))

        val testInput2 = readInput("Day17_test2")
        assertThat(part2(testInput2)).isEqualTo(117440)

        fun list(x: Long) = listOf(x + 0xa, x + 0xd, x + 0xf)

//        val list1 = Solver(input, endIdx = 7).findAll().take(100).toList()
//        val list2 = Solver(input, endIdx = 7, transform = { list((it shl 16) + 0x2a20 )}).findAll().take(100).toList()
//        assertThat(list1).isEqualTo(list2)
//
//        val list3 = Solver(input, endIdx = 11, transform = { list((it shl 16) + 0x2a20) }).findAll().take(100).toList()
//        val list4 = Solver(input, endIdx = 11, transform = { list((it shl 24) + 0x682a20) }).findAll().take(100).toList()
//        assertThat(list3).isEqualTo(list4)
//
//        val list5 = Solver(input, endIdx = 14, transform = { list((it shl 24) + 0x682a20) }).findAll().take(100).toList()
//        val list6 = Solver(input, endIdx = 14, transform = { list((it shl 40) + 0x9a24682a20) }).findAll().take(100).toList()
//        assertThat(list5).isEqualTo(list6)

        val part2Result = part2(input, transform = { list((it shl 40) + 0x9a24682a20) })
        assertThat(part2Result).isEqualTo(202972175280682)
        println(part2Result)
    }

    data class Input(
        val a: Long,
        val b: Long,
        val c: Long,
        val program: List<Long>,
    )
}

fun main() = Day17.solve()
