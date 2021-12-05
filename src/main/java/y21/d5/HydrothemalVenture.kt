package y21.d5

import util.AocInputReader
import java.lang.Integer.max
import kotlin.math.abs
import kotlin.math.min

fun main() {
    val lines = AocInputReader.readLines("y21/d5/input")
            .map { line ->
                line.split(" -> ")
                        .map { pair -> pair.split(",").map { it.toInt() } }
            }.map {
                Line(Point(it[0][0], it[0][1]), Point(it[1][0], it[1][1]))
            }
//            // for part 1
//            .filter {
//                it.p1.x == it.p2.x || it.p1.y == it.p2.y
//            }

    val pointMap = mutableMapOf<Point, Int>()
    lines.forEach { line ->
        line.listAllPoints().forEach { point ->
            pointMap.compute(point) { _, v ->
                (v ?: 0) + 1
            }
        }
    }

    var dangerous = pointMap.filter { it.value >= 2 }.size
    println(dangerous)

}

class Point(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        return if (other is Point) {
            this.x == other.x && this.y == other.y
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return x * 4321 + y
    }
}

class Line(val p1: Point, val p2: Point) {
    fun listAllPoints(): List<Point> {
        val res = mutableListOf<Point>()
        if (p1.x == p2.x) {
            for (y in min(p1.y, p2.y)..max(p1.y, p2.y)) {
                res.add(Point(p1.x, y))
            }
        } else if (p1.y == p2.y) {
            for (x in min(p1.x, p2.x)..max(p1.x, p2.x)) {
                res.add(Point(x, p1.y))
            }
        } else { // diagonal
            val dx = if (p2.x > p1.x) 1 else -1
            val dy = if (p2.y > p1.y) 1 else -1
            for (s in 0..abs(p1.x - p2.x)) {
                res.add(Point(
                    p1.x + dx * s,
                    p1.y + dy * s
                ))
            }
        }
        return res
    }
}

