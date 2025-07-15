package kleinert.soap.edn

import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStream
import java.io.Reader
import java.util.*
import java.util.function.IntConsumer
import java.util.stream.IntStream
import kotlin.math.min


internal class CodePointIterator : PrimitiveIterator.OfInt, Closeable {
    private var memoryIndex = -1
    private var memory: IntArray
    private val iterator: PrimitiveIterator.OfInt

    var textIndex: Int = 0
        private set

    var lineIdx: Int = 0
        private set

    constructor(codepointStream: IntStream, memorySize: Int = 32) {
        memory = IntArray(memorySize)
        iterator = codepointStream.iterator()
    }

    constructor(reader: InputStream, memorySize: Int = 32) {
        iterator = IntermediateInputStream(reader)
        memory = IntArray(memorySize)
    }

    constructor(reader: Reader, memorySize: Int = 32) {
        iterator = IntermediateReader(reader.buffered())
        memory = IntArray(memorySize)
    }

    override fun remove() {
        throw UnsupportedOperationException("remove")
    }

    override fun hasNext(): Boolean =
        memoryIndex >= 0 || iterator.hasNext()

    fun peek(): Int {
        if (memoryIndex >= 0)
            return memory[memoryIndex]
        val temp = nextInt()
        unread(temp)
        return temp
    }

    override fun next(): Int = nextInt()

    override fun nextInt(): Int {
        val code: Int
        if (memoryIndex >= 0) {
            code = memory[memoryIndex]
            memoryIndex--
            textIndex++
        } else if (iterator.hasNext()) {
            code = iterator.nextInt()
            textIndex++
        } else {
            code = -1
        }
        if (code == '\n'.code) {
            lineIdx++
        }
        return code
    }

    override fun forEachRemaining(p0: IntConsumer?) {
        while (hasNext()) p0!!.accept(nextInt())
    }

    fun unread(v: Int): CodePointIterator {
        memoryIndex++
        if (memoryIndex == memory.size)
            memory = memory.copyOf(memory.size + min(memory.size / 2, 8))
        memory[memoryIndex] = v
        textIndex--
        if (v == '\n'.code)
            lineIdx--
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
        while (hasNext()) {
            val ni = nextInt()
            if (ni == '\n'.code) break
        }
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
        if (iterator is Closeable) {
            iterator.close()
        }
    }
}

private class IntermediateReader(private var reader: BufferedReader) : PrimitiveIterator.OfInt, Closeable {
    private var memory: Int = -1

    override fun hasNext(): Boolean {
        if (memory != -1) return true
        val temp = reader.read()
        if (temp == -1) return false
        memory = temp
        return true
    }

    override fun next(): Int {
        return nextInt()
    }

    override fun nextInt(): Int {
        if (memory != -1) {
            val temp = memory
            memory = -1
            return temp
        }
        return reader.read()
    }

    override fun remove() {
        throw java.lang.UnsupportedOperationException("Remove not supported!")
    }

    override fun close() {
        reader.close()
    }
}

private class IntermediateInputStream(private var input: InputStream) : PrimitiveIterator.OfInt {
    private var memory: Int = -1

    override fun hasNext(): Boolean {
        if (memory != -1) return true
        val temp = input.read()
        if (temp == -1) return false
        memory = temp
        return true
    }

    override fun next(): Int {
        return nextInt()
    }

    override fun nextInt(): Int {
        if (memory != -1) {
            val temp = memory
            memory = -1
            return temp
        }
        return input.read()
    }

    override fun remove() {
        throw java.lang.UnsupportedOperationException("Remove not supported!")
    }
}
