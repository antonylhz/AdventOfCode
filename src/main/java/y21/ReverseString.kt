package y21

fun main() {
    val str = ".cte ,worg ynapmoc a sa ecnaDetyB dna maet ruo pleh nac uoy woh ,ytinutroppo txen ruoy ni rof gnikool era uoy tahwsallewsa,krowruoydnauoytuobaeromnraelotnoitasrevnocaekileromebdluow sihT .detubirtnoc uoy woh dna no dekrow ev'uoy stcejorp gnitseretni deksa eb yam uoY"
    var i = 0
    var j = str.length - 1
    val arr = str.toCharArray()
    while (i < j) {
        val tmp = arr[i]
        arr[i] = arr[j]
        arr[j] = tmp
        i++
        j--
    }
    println(String(arr))
}