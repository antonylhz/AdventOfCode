package y21.d12

import util.AocInputReader
import java.util.*

fun main() {
    val graph = CaveGraph(AocInputReader.readLines("y21/d12/input"))
    val paths = graph.findAllPaths("start", "end")
    println(paths.size)
    //println(paths.joinToString("\n"))
}

class Cave(val name: String) {
    val neighbors = mutableListOf<Cave>()
    override fun toString(): String = name
}

class Path() {
    val caves = mutableListOf<Cave>()
    private val visited = mutableSetOf<String>()
    private var twice = false

    constructor(caves: List<Cave>, visited: Set<String>, twice: Boolean) : this() {
        this.caves.addAll(caves)
        this.visited.addAll(visited)
        this.twice = twice
    }

    fun add(cave: Cave): Path? {
        return if (cave.name[0].isLowerCase() && visited.contains(cave.name)) {
            if (twice) {
                null
            } else {
                Path(caves, visited, true).also { it.doAdd(cave) }
            }
        } else {
            Path(caves, visited, twice).also { it.doAdd(cave) }
        }
    }

    private fun doAdd(cave: Cave) {
        caves.add(cave)
        if (cave.name[0].isLowerCase()) {
            visited.add(cave.name)
        }
    }

    override fun toString(): String =
            caves.joinToString { it.name }
}

class CaveGraph(private val input: Array<String>) {

    companion object {
        private val map = mutableMapOf<String, Cave>()
        private fun findCave(name: String): Cave = map.getOrPut(name) { Cave(name) }
    }

    init {
        input.forEach { line ->
            val tokens = line.split("-")
            findCave(tokens[0]).neighbors.add(findCave(tokens[1]))
            findCave(tokens[1]).neighbors.add(findCave(tokens[0]))
        }
    }

    fun findAllPaths(start: String, end: String): List<Path> {
        val q: Queue<Path> = LinkedList()
        q.add(Path().add(findCave(start)))
        val res = mutableListOf<Path>()
        while (q.isNotEmpty()) {
            val path = q.poll()
            if (path.caves.last().name == end) {
                res.add(path)
            } else {
                path.caves.last().neighbors.forEach { neighbor ->
                    if (neighbor.name != start) {
                        path.add(neighbor)?.let {
                            q.add(it)
                        }
                    }
                }
            }
        }
        return res
    }
}