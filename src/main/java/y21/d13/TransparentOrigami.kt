package y21.d13

import util.AocInputReader
import util.Location
import kotlin.math.max

fun main() {
    val locations = mutableSetOf<Location>()
    val folds = mutableListOf<Fold>()
    AocInputReader.readLines("y21/d13/input").forEach { line ->
        if (line.contains(',')) {
            locations.add(
                    Location(line.split(",").map { it.toInt() })
            )
        } else if (line.contains('=')) {
            val index = line.indexOf('=')
            folds.add(
                    Fold(line[index - 1] == 'x', line.substring(index + 1).toInt())
            )
        }
    }

    folds.forEachIndexed { index, fold ->
        locations.map {
            if (fold.x) {
                Location(
                        if (it.r <= fold.value) it.r else 2 * fold.value - it.r,
                        it.c
                )
            } else {
                Location(
                        it.r,
                        if (it.c <= fold.value) it.c else 2 * fold.value - it.c
                )
            }
        }.toSet().apply {
            println("Fold #$index: $size")
            print()
            locations.clear()
            locations.addAll(this)
        }
    }
}

data class Fold(
        val x: Boolean,
        val value: Int
)

fun Set<Location>.print() {
    var mx = 0
    var my = 0
    forEach {
        mx = max(mx, it.r)
        my = max(my, it.c)
    }
    for (y in 0..my) {
        for (x in 0..mx) {
            if (contains(Location(x, y))) {
                print('#')
            } else {
                print(' ')
            }
        }
        println()
    }
}
