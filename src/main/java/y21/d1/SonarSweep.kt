package y21.d1

import util.AocInputReader

fun main() {
    part1()
    part2()
}

private fun part1() {
    val lines = AocInputReader.readLines("y21/d1/input")
    var inc = 0
    var prev = Integer.parseInt(lines[0])
    for (i in 1 until lines.size) {
        val cur = Integer.parseInt(lines[i])
        if (cur > prev) {
            inc++
        }
        prev = cur
    }
    println(inc)
}

private fun part2() {
    val lines = AocInputReader.readLines("y21/d1/input")
    var inc = 0
    for (i in 3 until lines.size) {
        if (Integer.parseInt(lines[i]) > Integer.parseInt(lines[i - 3])) {
            inc++
        }
    }
    println(inc)
}

