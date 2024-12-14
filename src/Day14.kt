import Day14.Robot
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Day14 : Task<List<Robot>> {
    override fun parse(input: List<String>): List<Robot> =
        input.map {
            val list = it.split(',', '=', ' ')
            Robot(list[1].toLong(), list[2].toLong(), list[4].toLong(), list[5].toLong())
        }


    fun part1(robots: List<Robot>, width: Long, height: Long, iterations: Int = 100): Long {
        val cx = width / 2
        val cy = height / 2

        val endPositions = robots.mapNotNull {
            val x = (it.x + iterations * (width + it.vx)) % width
            val y = (it.y + iterations * (height + it.vy)) % height
            if (x == cx || y == cy) return@mapNotNull null
            x / (cx + 1) to y / (cy + 1)
        }
        val blocks = endPositions.groupingBy { it }.eachCount()
        return blocks.values.fold(1L) { acc, block -> acc * block }
    }


    private const val imageWidth = 3800
    private const val imageHeight = 15000
    private const val blockWidth = 110
    private const val blockHeight = 110
    private const val xBlocks = 30
    private const val yBlocks = 100

    fun part2(robots: List<Robot>, width: Long, height: Long) {
        generateSequence(1) { it + 1 }
            .map { iteration ->
                robots.map {
                    val x = (it.x + iteration * (width + it.vx)) % width
                    val y = (it.y + iteration * (height + it.vy)) % height
                    x to y
                }
            }
            .drop(7082)
            .take(1)
            .chunked(xBlocks * yBlocks)
            .forEachIndexed { fileIdx, lists ->
                val bitmap = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_BINARY)
                lists.forEachIndexed { index, points ->
                    val xOffset = index % xBlocks * blockWidth
                    val yOffset = index / xBlocks * blockHeight
                    points.forEach { point ->
                        try {
                            bitmap.setRGB(xOffset+point.first.toInt(), yOffset+point.second.toInt(), 0xffffffff.toInt())
                        } catch (e: Exception) {
                            throw e
                        }
                    }
                }
                ImageIO.write(bitmap, "png", File("$fileIdx.png"))
            }


    }

    fun solve() {
        val testInput = readInput("Day14_test")
        assertThat(part1(testInput, 11, 7)).isEqualTo(12)

        val input = readInput("Day14")
        assertThat(part1(input, 101, 103)).isEqualTo(224357412)
        println(part1(input, 101, 103))

        part2(input, 101, 103)
    }

    data class Robot(
        val x: Long,
        val y: Long,
        val vx: Long,
        val vy: Long,
    )
}

fun main() = Day14.solve()
