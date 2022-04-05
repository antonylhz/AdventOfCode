package y21.d16

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister
import util.AocInputReader
import java.lang.Exception

fun main() {
    val decoder = PackerDecoder(AocInputReader.readLines("y21/d16/test")[0])
}

class PackerDecoder(s: String) {
    val encoded = StringBuilder().run {
        s.toCharArray().forEach {
            val real = it.digitToInt(16).toString(2)
            repeat(4 - real.length) {
                append("0")
            }
            append(real)
        }
        toString()
    }

    private var p = 0

    fun readPackets(): List<Packet> {
        val res = mutableListOf<Packet>()
        do {
            val p = readPacket()?.apply {
                res.add(this)
            }
        } while (p != null)
        return res
    }

    fun readPacket(): Packet? = try {
        val version = read(3)
        val type = read(3)
        Packet(version, type)
    } catch (e: Exception) {
        null
    }


    fun read(n: Int): Int =
            encoded.substring(p, p + n).toInt(2).also {
                p += n
            }

}

open class Packet(
        version: Int,
        type: Int
)

