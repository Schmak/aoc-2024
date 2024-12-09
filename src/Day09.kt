object Day09 : Task<List<Int>> {
    override fun parse(input: List<String>) =
        input.first().map(Char::digitToInt)

    fun IntArray.checksum(): Long =
        this.foldIndexed(0L) { index, acc, value -> acc + (if (value == -1) 0 else value * index) }

    private fun List<Block>.checksum(): Long {
        var offset = 0
        return this.sumOf { block ->
            when (block) {
                is Block.File -> block.id.toLong() * block.size * (2 * offset + block.size - 1) / 2
                is Block.Empty -> 0
            }.also {
                offset += block.size
            }
        }
    }

    private fun List<Int>.toMap(): IntArray {
        val length = sum()
        val map = IntArray(length) { -1 }
        var idx = 0
        var id = 0
        val chunked = chunked(2)
        chunked.forEach {
            repeat(it.first()) {
                map[idx++] = id
            }
            id++
            idx += it.getOrNull(1) ?: 0
        }
        return map
    }

    fun part1(input: List<Int>): Long {
        val map = input.toMap()
        var to = 0
        var from = map.lastIndex
        while (true) {
            while (map[to] != -1) to++
            while (map[from] == -1) from--
            if (to >= from) break
            map[to++] = map[from]
            map[from--] = -1
        }
        return map.checksum()
    }

    fun part2(input: List<Int>): Long {
        val blocks = input.chunked(2).flatMapIndexed { id, list ->
            listOfNotNull(
                Block.File(id, list.first()),
                list.getOrNull(1)?.let(Block::Empty)
            )
        }.toMutableList()
        val maxId = (blocks.last() as Block.File).id
        for (i in maxId downTo 1) {
            val fileIdx = blocks.indexOfFirst { block -> block is Block.File && block.id == i }
            val file = blocks[fileIdx]
            val emptyIdx = blocks.asSequence().take(fileIdx).indexOfFirst { it is Block.Empty && it.size >= file.size }
            if (emptyIdx != -1) {
                val sizeLeft = blocks[emptyIdx].size - file.size
                blocks[fileIdx] = Block.Empty(file.size)
                blocks.removeAt(emptyIdx)
                if (sizeLeft > 0) blocks.add(emptyIdx, Block.Empty(sizeLeft))
                blocks.add(emptyIdx, file)
            }
        }
        return blocks.checksum()
    }

    fun solve() {
        val testInput = readInput("Day09_test")
        assertThat(part1(testInput)).isEqualTo(1928)
        assertThat(part2(testInput)).isEqualTo(2858)

        val input = readInput("Day09")
        assertThat(part1(input)).isEqualTo(6259790630969)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(6289564433984)
        println(part2(input))
    }
}

sealed interface Block {
    val size: Int

    data class Empty(override val size: Int) : Block
    data class File(val id: Int, override val size: Int) : Block
}

fun main() = Day09.solve()
