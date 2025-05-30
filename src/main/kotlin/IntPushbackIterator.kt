import java.util.*
import java.util.function.IntConsumer
import java.util.stream.IntStream
import kotlin.math.min

class CodePointIterator(codepointStream: IntStream, memorySize:Int=128) : PrimitiveIterator.OfInt {
    private var index = -1
    private var memory = IntArray(memorySize)
    private val iterator: PrimitiveIterator.OfInt = codepointStream.iterator()

    override fun remove() {
        throw UnsupportedOperationException("remove")
    }

    override fun hasNext(): Boolean =
        index >= 0 || iterator.hasNext()

    fun isFinished(): Boolean =
        index == -1 && !iterator.hasNext()

    fun peek(): Int {
        val temp = next()
        unread(temp)
        return temp
    }

    override fun next(): Int {
        if (index >= 0) {
            val temp = memory[index]
            index--
            return temp
        }
        return if (iterator.hasNext()) iterator.nextInt() else -1
    }

    override fun nextInt(): Int {
        if (index >= 0) {
            val temp = memory[index]
            index--
            return temp
        }
        return if (iterator.hasNext()) iterator.nextInt() else -1
    }

    override fun forEachRemaining(p0: IntConsumer?) {
        while (hasNext()) p0!!.accept(nextInt())
    }

    fun unread(v: Int) {
        index++
        if (index == memory.size)
            memory = memory.copyOf(memory.size + min(memory.size/2, 8))
        memory[index] = v
    }

    f
}