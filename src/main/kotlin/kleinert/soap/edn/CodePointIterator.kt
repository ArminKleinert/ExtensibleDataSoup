package kleinert.soap.edn

import java.io.Closeable
import java.io.Reader
import java.util.*
import java.util.function.IntConsumer
import java.util.stream.IntStream
import kotlin.math.min


internal class CodePointIterator : PrimitiveIterator.OfInt, Closeable {
    private var index = -1
    private var memory: IntArray
    private val iterator: PrimitiveIterator.OfInt

    constructor(codepointStream: IntStream, memorySize: Int = 32) {
        memory = IntArray(memorySize)
        iterator = codepointStream.iterator()
    }

    constructor(reader: Reader, memorySize: Int = 32) {
        iterator = IntermediateReader(reader)
        memory = IntArray(memorySize)

    }

    override fun remove() {
        throw UnsupportedOperationException("remove")
    }

    override fun hasNext(): Boolean =
        index >= 0 || iterator.hasNext()

    fun isFinished(): Boolean =
        index == -1 && !iterator.hasNext()

    fun peek(): Int {
        if (index >= 0)
            return memory[index]
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

    fun unread(v: Int): CodePointIterator {
        index++
        if (index == memory.size)
            memory = memory.copyOf(memory.size + min(memory.size / 2, 8))
        memory[index] = v
        return this
    }

    fun takeCodePoints(dest: StringBuilder = StringBuilder(), condition: (Int) -> Boolean): StringBuilder {
        while (hasNext()) {
            val codepoint = nextInt()
            if (!condition(codepoint)) {
                unread(codepoint)
                return dest
            }
            dest.appendCodePoint(codepoint)
        }
        return dest
    }

    fun takeCodePoints(
        dest: StringBuilder = StringBuilder(), maxCount: Int, condition: (Int) -> Boolean
    ): StringBuilder {
        var count = 0
        while (count < maxCount && hasNext()) {
            val codepoint = nextInt()
            if (!condition(codepoint)) {
                unread(codepoint)
                return dest
            }
            dest.appendCodePoint(codepoint)
            count++
        }
        return dest
    }

    fun skipLine() {
        while (hasNext())
            if (nextInt() == '\n'.code) break
    }

    fun skipWhile(condition: (Int) -> Boolean) {
        while (hasNext()) {
            val code = nextInt()
            if (!condition(code)) {
                unread(code)
                break
            }
        }
    }

    override fun close() {
        if (iterator is Closeable)
            iterator.close()
    }
}

internal class IntermediateReader(private var reader: Reader) : PrimitiveIterator.OfInt, Closeable {
    override fun hasNext(): Boolean {
        return reader.ready()
    }

    override fun next(): Int {
        return reader.read()
    }

    override fun nextInt(): Int {
        return reader.read()
    }

    override fun remove() {
        throw java.lang.UnsupportedOperationException("Remove not supported!")
    }

    override fun close() {
        reader.close()
    }
}
