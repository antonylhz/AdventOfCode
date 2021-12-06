package y21.d6

import util.AocInputReader

fun main() {
    val initial = AocInputReader.readLines("y21/d6/input")[0]
            .split(",")
            .map { it.toInt() }
    println(prawn(initial, 256))
}

fun prawn(initial: List<Int>, d: Int): Long {
    var ageGroups = LongArray(9)
    initial.forEach { age ->
        ageGroups[age]++
    }
    for (i in 1..d) {
        var next = LongArray(9)
        for (j in 0..7) {
            next[j] = ageGroups[j + 1]
        }
        next[6] += ageGroups[0]
        next[8] = ageGroups[0]
        ageGroups = next
    }
    return ageGroups.sum()
}