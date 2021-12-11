package y21.d11

import util.AocInputReader
import util.Pair

fun main() {
    val energy = AocInputReader.readCharMatrix("y21/d11/input")
            .map { row -> row.map { it - '0' }.toMutableList() }.toMutableList()
// part 1
//    var flashes = 0
//    repeat(100) {
//        flashes += energy.step(it)
//    }
//    println(flashes)

    var step = 0
    do {
        val flashes = energy.step(step++)
    } while (flashes != energy.size * energy[0].size)
}

fun MutableList<MutableList<Int>>.step(s: Int): Int {
    val h = size
    val w = get(0).size
    forEach { it.forEachIndexed { index, _ -> it[index]++ } }
    val flashed = Array(h) { Array(w) { false } }
    do {
        var hadFlash = false
        for (r in 0 until h)
            for (c in 0 until w) {
                if (this[r][c] > 9 && flashed[r][c].not()) {
                    flashed[r][c] = true
                    hadFlash = true
                    Pair(r, c).neighbors(h, w).forEach {
                        if (this[it.first][it.second] <= 9) {
                            this[it.first][it.second]++
                        }
                    }
                }
            }
    } while (hadFlash)
    var res = 0
    for (r in 0 until h)
        for (c in 0 until w) {
            if (flashed[r][c]) {
                res++
                this[r][c] = 0
            }
        }

    println("Step ${s+1}-----------------------")
    println(map { it.joinToString(" ") }.joinToString("\n"))
    return res
}

fun Pair<Int, Int>.neighbors(height: Int, width: Int): List<Pair<Int, Int>> {
    val res = mutableListOf<Pair<Int, Int>>()
    if (first > 0) {
        res.add(Pair(first - 1, second))
    }
    if (first < height - 1) {
        res.add(Pair(first + 1, second))
    }
    if (second > 0) {
        res.add(Pair(first, second - 1))
    }
    if (second < width - 1) {
        res.add(Pair(first, second + 1))
    }
    if (first > 0 && second > 0) {
        res.add(Pair(first - 1, second - 1))
    }
    if (first < height - 1 && second < width - 1) {
        res.add(Pair(first + 1, second + 1))
    }
    if (first < height - 1 && second > 0) {
        res.add(Pair(first + 1, second - 1))
    }
    if (first > 0 && second < width - 1) {
        res.add(Pair(first - 1, second + 1))
    }

    return res
}