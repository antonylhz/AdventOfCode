package y21.d4

import util.AocInputReader

fun main() {
    part2()
}

private fun part1() {
    val (nums, boards) = read()
    nums.forEach { num ->
        boards.forEachIndexed { index, board ->
            board.mark(num).also { won ->
                if (won) {
                    println("Board #$index won!")
                    println(board.calcScore())
                    return
                }
            }
        }
    }
}

private fun part2() {
    val (nums, boards) = read()
    val wonBoards = mutableSetOf<Int>()
    nums.forEach { num ->
        boards.forEachIndexed { index, board ->
            if (wonBoards.contains(index).not()) {
                board.mark(num).also { won ->
                    if (won) {
                        wonBoards.add(index)
                        if (wonBoards.size == boards.size) {
                            println("Board #$index won last!")
                            println(board.calcScore())
                            return
                        }
                    }
                }
            }
        }
    }
}

private fun read(): Pair<List<Int>, List<Board>> {
    val lines = AocInputReader.readLines("y21/d4/input")
    val nums = lines[0].split(",").map { Integer.parseInt(it) }

    val boards = mutableListOf<Board>()
    for (i in 2 until lines.size step 6) {
        boards.add(
                Board(listOf(lines[i], lines[i + 1], lines[i + 2], lines[i + 3], lines[i + 4]))
        )
    }
    return Pair(nums, boards)
}

class Board(strings: List<String>) {
    private val rowMap = mutableMapOf<Int, MutableList<Int>>()
    private val colMap = mutableMapOf<Int, MutableList<Int>>()

    private val rowMarks = IntArray(5)
    private val colMarks = IntArray(5)

    private val marked = mutableSetOf<Int>()
    private var lastMarked: Int? = null

    private val matrix = Array(5) { IntArray(5) }

    init {
        strings.forEachIndexed { row, s ->
            s.split("\\s++".toRegex())
                    .filter { it.isNotEmpty() }
                    .map { Integer.parseInt(it) }
                    .forEachIndexed { col, num ->
                rowMap.putIfAbsent(num, mutableListOf())
                rowMap[num]?.add(row)
                colMap.putIfAbsent(num, mutableListOf())
                colMap[num]?.add(col)
                matrix[row][col] = num
            }
        }
    }

    fun mark(num: Int): Boolean {
        marked.add(num)
        lastMarked = num
        rowMap[num]?.forEach {
            rowMarks[it]++
            if (rowMarks[it] >= 5) {
                return true
            }
        }
        colMap[num]?.forEach {
            colMarks[it]++
            if (colMarks[it] >= 5) {
                return true
            }
        }
        return false
    }

    fun calcScore(): Int {
        var sum = 0
        matrix.forEach { r ->
            r.forEach { num ->
                if (marked.contains(num).not()) {
                    sum += num
                }
            }
        }
        return sum * (lastMarked ?: 0)
    }

}