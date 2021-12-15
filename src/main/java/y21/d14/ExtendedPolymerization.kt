package y21.d14

import util.AocInputReader

fun main() {
    val lines = AocInputReader.readLines("y21/d14/input")
    for (i in 2 until lines.size) {
        val tokens = lines[i].split(" -> ")
        rules[tokens[0]] = listOf(
                tokens[0].substring(0..0).plus(tokens[1]),
                tokens[1].plus(tokens[0][1])
        )
    }
    val polymer = lines[0]
    for (i in 0..polymer.length - 2) {
        val pair = polymer.substring(i, i + 2)
        counter[pair] = counter.getOrDefault(pair, 0) + 1
    }
    counter[polymer.last().toString()] = 1
    repeat(40) { step() }
    val charCounter = Array<Long>(26) {0L}
    counter.forEach { (pair, cnt) ->
        charCounter[pair[0] - 'A'] += cnt
    }
    println(charCounter.maxOf { it } - charCounter.filter { it > 0 }.minOf { it })
}

var counter = mutableMapOf<String, Long>()
val rules = mutableMapOf<String, List<String>>()

fun step() {
    val newCounter= mutableMapOf<String, Long>()
    counter.forEach { (pair, cnt) ->
        rules[pair]?.let { generated ->
            generated.forEach {
                newCounter[it] = newCounter.getOrDefault(it, 0) + cnt
            }
        } ?: run {
            newCounter[pair] = newCounter.getOrDefault(pair, 0) + cnt
        }
    }
    counter = newCounter
}
