package d2

import util.AocInputReader

fun main() {
    val submarine = Submarine()
    AocInputReader.readLines("d2/input")
            .map { it.split(" ") }
            .map {
                submarine.exec2(
                        Submarine.Command.valueOf(it[0]),
                        it[1].toInt())
            }

    println(submarine.mult)
}

class Submarine {

    var h = 0
    var d = 0
    var aim = 0

    val mult
        get() = h * d

    fun exec(command: Command, x: Int) {
        when (command) {
            Command.forward -> h += x
            Command.down -> d += x
            Command.up -> d -= x
        }
    }

    fun exec2(command: Command, x: Int) {
        when (command) {
            Command.forward -> {
                h += x
                d += aim * x
            }
            Command.down -> aim += x
            Command.up -> aim -= x
        }
    }

    enum class Command {
        forward,
        down,
        up
    }
}