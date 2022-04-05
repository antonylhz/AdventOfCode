package y21.d21

fun main() {
    val dice = Dice()
    val d1 = DiracDice(7)
    var d2 = DiracDice(6)
    while (d1.won().not() && d2.won().not()) {
        d1.step(dice)
        if (d1.won()) {
            println(dice.rolls * d2.score)
        }
        d2.step(dice)
        if (d2.won()) {
            println(dice.rolls * d1.score)
        }
    }
}

class Dice {
    var dice = 1
    var rolls = 0
    fun roll(): Int = ((dice++) % 1_000).also { rolls++ }
}

class DiracDice(initialSpace: Int) {
    var score = 0
    var space = initialSpace

    fun step(dice: Dice) {
        repeat(3) {
            space += dice.roll()
        }
        space = (space % 10).takeIf { it > 0 } ?: 10
        score += space
    }

    fun won(): Boolean = score >= 1000
}