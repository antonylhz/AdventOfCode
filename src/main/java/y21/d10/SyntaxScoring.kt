package y21.d10

import util.AocInputReader
import java.util.*

fun main() {
    val res = AocInputReader.readLines("y21/d10/input")
            // part 1
            // .sumOf { it.scoreCorrupted() }
    // println(res)

            // part 2
            .filter { it.scoreCorrupted() == 0 }
            .map { it.scoreIncomplete() }
            .sorted()
    println(res)
    println(res[res.size / 2])
}

fun String.scoreCorrupted(): Int {
    val stack = LinkedList<Char>()
    toCharArray().forEach { c ->
        when(c) {
            ')' -> if (stack.isNotEmpty() && stack.peek() == '(') {
                stack.pop()
            } else return 3
            ']' -> if (stack.isNotEmpty() && stack.peek() == '[') {
                stack.pop()
            } else return 57
            '}' -> if (stack.isNotEmpty() && stack.peek() == '{') {
                stack.pop()
            } else return 1197
            '>' -> if (stack.isNotEmpty() && stack.peek() == '<') {
                stack.pop()
            } else return 25137
            else -> stack.push(c)
        }
    }
    return 0
}

fun String.scoreIncomplete(): Long {
    val stack = LinkedList<Char>()
    toCharArray().forEach { c ->
        when(c) {
            ')' -> if (stack.isNotEmpty() && stack.peek() == '(') {
                stack.pop()
            }
            ']' -> if (stack.isNotEmpty() && stack.peek() == '[') {
                stack.pop()
            }
            '}' -> if (stack.isNotEmpty() && stack.peek() == '{') {
                stack.pop()
            }
            '>' -> if (stack.isNotEmpty() && stack.peek() == '<') {
                stack.pop()
            }
            else -> stack.push(c)
        }
    }
    var score = 0L
    while (stack.isNotEmpty()) {
        when(stack.pop()) {
            '(' -> score = score * 5 + 1
            '[' -> score = score * 5 + 2
            '{' -> score = score * 5 + 3
            '<' -> score = score * 5 + 4
        }
    }
    return score
}