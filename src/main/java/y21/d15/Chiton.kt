package y21.d15

import util.AocInputReader
import util.Location
import java.util.*

fun main() {
    val tile =  AocInputReader.readCharMatrix("y21/d15/input")
            .map { row -> row.map { it - '0' }.toTypedArray() }.toTypedArray()
    // part 1
    findShortedPath(tile)

    // part 2
    val cavern = Array(tile.size * 5) {Array(tile[0].size * 5) {0} }
    for (r in cavern.indices)
        for (c in cavern[0].indices) {
            if (r < tile.size && c < tile[0].size) {
                cavern[r][c] = tile[r][c]
            } else if (c >= tile[0].size) {
                cavern[r][c] = (cavern[r][c - tile[0].size] + 1).takeIf { it < 10 } ?: 1
            } else {
                cavern[r][c] = (cavern[r - tile.size][c] + 1).takeIf { it < 10 } ?: 1
            }
        }
    findShortedPath(cavern)
}

fun findShortedPath(cavern: Array<Array<Int>>) {
    val h = cavern.size
    val w = cavern[0].size
    val visited = Array(h) { Array(w) { false } }
    val risk = Array(h) { Array(w) { Int.MAX_VALUE } }
    risk[0][0] = 0
    val heap = PriorityQueue<Location>(
            Comparator.comparing { risk[it.r][it.c] }
    )
    heap.add(Location(0, 0))
    while (heap.isNotEmpty()) {
        val location = heap.poll()
        println("Expanding on $location with risk = ${risk[location.r][location.c]}")
        if (location.r == h - 1 && location.c == w - 1) {
            break
        }
        visited[location.r][location.c] = true
        location.getNeighbors(h, w).forEach {
            if (visited[it.r][it.c].not()) {
                val newRisk = risk[location.r][location.c] + cavern[it.r][it.c]
                if (newRisk < risk[it.r][it.c]) {
                    risk[it.r][it.c] = newRisk
                    heap.remove(it)
                    heap.add(it)
                }
            }
        }
    }
    println("Lowest risk: ${risk[h - 1][w - 1]}")
}