package y21.d7

import util.AocInputReader
import kotlin.math.abs
import kotlin.math.roundToInt

fun main() {
    val crabs = AocInputReader.readLines("y21/d7/input")[0].split(",").map { it.toInt() }
    val mean = crabs.average().roundToInt()
    for (c in mean downTo mean - 2) {
        print("Move to $c cost fuel:")
        val fuel = crabs.map { abs(c - it) }.sumOf { it * (it + 1) / 2 }
        println(fuel)
    }
}

fun part1(crabs: List<Int>) {
    val median = crabs.sorted()[crabs.size / 2]
    println("Move to $median")
    val fuel = crabs.sumOf { abs(median - it) }
    println(fuel)
}