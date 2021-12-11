package y21.d9

import util.AocInputReader
import util.Pair
import java.util.*

fun main() {
    val map = AocInputReader.readCharMatrix("y21/d9/input")
            .map { row -> row.map { it - '0' } }
    val height = map.size
    val width = map[0].size
    val visited = Array(height) { Array(width) { false } }
    val basins = mutableListOf<Int>()
    for (r in 0 until height)
        for (c in 0 until width) {
            if (map[r][c] < 9 && visited[r][c].not()) {
                val queue = LinkedList<Pair<Int, Int>>()
                queue.add(Pair(r, c))
                val points = mutableSetOf<Pair<Int, Int>>()
                while (queue.isNotEmpty()) {
                    val pair = queue.poll()
                    if (visited[pair.first][pair.second]) {
                        continue
                    } else {
                        visited[pair.first][pair.second] = true
                    }
                    points.add(pair)
                    pair.neighbors(width, height).forEach {
                        if (visited[it.first][it.second].not() && map[it.first][it.second] < 9) {
                            queue.add(it)
                        }
                    }
                }
                basins.add(points.size)
                println(points)
            }
        }
    val top3 = basins.sorted().takeLast(3)
    println(top3)
    println(top3.reduce { acc, i -> acc * i })
}

fun part1() {
    val map = AocInputReader.readCharMatrix("y21/d9/input")
            .map { row -> row.map { it - '0' } }
    val height = map.size
    val width = map[0].size

    val lows = mutableListOf<Int>()
    for (r in 0 until height)
        for (c in 0 until width) {
            if (r > 0 && map[r][c] >= map[r - 1][c]) {
                continue
            } else if (r < height - 1 && map[r][c] >= map[r + 1][c]) {
                continue
            } else if (c > 0 && map[r][c] >= map[r][c - 1]) {
                continue
            } else if (c < width - 1 && map[r][c] >= map[r][c + 1]) {
                continue
            } else {
                println(map[r][c])
                lows.add(map[r][c])
            }
        }
    val risk = lows.sumOf { it + 1 }
    println("Risk: $risk")
}

fun Pair<Int, Int>.neighbors(width: Int, height: Int): List<Pair<Int, Int>> {
    val res = mutableListOf<Pair<Int, Int>>()
    if (this.first > 0) {
        res.add(Pair(first - 1, second))
    }
    if (first < height - 1) {
        res.add(Pair(first + 1 , second))
    }
    if (second > 0) {
        res.add(Pair(first, second - 1))
    }
    if (second < width - 1) {
        res.add(Pair(first, second + 1))
    }
    return res
}