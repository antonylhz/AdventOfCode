package y21.d3

import util.AocInputReader

fun main() {
    part2()
}

private fun part1() {
    val lines = AocInputReader.readLines("y21/d3/input")
    val n = lines[0].length
    val positives = IntArray(n)
    lines.forEach { it.toCharArray().forEachIndexed { index, c ->
        positives[index] += if (c == '0') -1 else 1
    }}
    var weight = 1
    var gamma = 0
    var epsilon = 0
    for (i in (n - 1).downTo(0)) {
        if (positives[i] > 0) {
            gamma += weight
        } else {
            epsilon += weight
        }
        weight *= 2
    }
    println("gamma $gamma")
    println("epsilon $epsilon")
    println(gamma * epsilon)
}

private fun part2() {
    val lines = AocInputReader.readLines("y21/d3/input").toList<String>()
    val n = lines[0].length
    var oxygenLines = lines
    for (i in 0 until n) {
        oxygenLines = oxygenLines.filterMostCommon(i)
        if (oxygenLines.size == 1) {
            break
        }
    }
    val oxygen = oxygenLines[0].toInt(2)
    var co2Lines = lines
    for (i in 0 until n) {
        co2Lines = co2Lines.filterLeastCommon(i)
        if (co2Lines.size == 1) {
            break
        }
    }
    val co2 = co2Lines[0].toInt(2)
    println(oxygen * co2)
}

private fun List<String>.filterMostCommon(d: Int): List<String> {
    var positive = 0
    forEach { s ->
        positive += if (s[d] == '1') 1 else -1
    }
    return if (positive >= 0) {
        filter { it[d] == '1' }
    } else {
        filter { it[d] == '0' }
    }
}

private fun List<String>.filterLeastCommon(d: Int): List<String> {
    var positive = 0
    forEach { s ->
        positive += if (s[d] == '1') 1 else -1
    }
    return if (positive >= 0) {
        filter { it[d] == '0' }
    } else {
        filter { it[d] == '1' }
    }
}

