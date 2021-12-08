package y21.d8

import util.AocInputReader

fun main() {
    val part2= AocInputReader.readLines("y21/d8/input").sumOf { line ->
        val data = line.split(" | ").map { it.split(" ") }
        val mappings = findMappings(data[0])
        data[1].map { mappings[it.sorted()] }.joinToString("").toInt()
    }

    println(part2)
}

fun part1() {
    val segments = setOf(2, 3, 4, 7)

    val part1 = AocInputReader.readLines("y21/d8/input").sumOf {
        it.split(" | ")[1].split(" ").filter { segments.contains(it.length) }.size
    }
    println(part1)
}

fun findMappings(signals: List<String>): Map<String, Int> {
    val res = Array(10) { "" }
    val list5 = mutableListOf<String>()
    val list6 = mutableListOf<String>()
    signals.forEach {
        val sorted = it.sorted()
        when(it.length) {
            2 -> res[1] = sorted
            3 -> res[7] = sorted
            4 -> res[4] = sorted
            7 -> res[8] = sorted
            5 -> list5.add(sorted)
            6 -> list6.add(sorted)
        }
    }
    val bd = res[4].minus(res[1])

    res[0] = list6.filter { it.minus(bd).length == 5 }[0]
    list6.remove(res[0])
    res[9] = list6.filter { it.minus(res[1]).length == 4 }[0]
    list6.remove(res[9])
    res[6] = list6[0]

    res[3] = list5.filter { it.minus(res[1]).length == 3 }[0]
    list5.remove(res[3])
    res[5] = list5.filter { it.minus(bd).length == 3 }[0]
    list5.remove(res[5])
    res[2] = list5[0]

    val map = mutableMapOf<String, Int>()
    res.forEachIndexed { index, s ->
        map[s] = index
    }
    return map
}

private fun String.minus(s: String): String {
    val chars = mutableSetOf<Char>()
            .also { toCharArray().forEach { c -> it.add(c) } }
    s.toCharArray().forEach { chars.remove(it) }
    return chars.joinToString("")
}

private fun String.sorted() = toCharArray().sorted().joinToString("")