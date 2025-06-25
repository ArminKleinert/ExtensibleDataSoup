package kleinert.soap.cons

import kleinert.soap.cons.EmptyList.sameTypeFromList


class LazyList<T>(fn: () -> PersistentList<T>?) : PersistentList<T> {
    private var fn: (() -> PersistentList<T>?)? = fn
    private var sv: PersistentList<T>? = null
    private var lazySize: Int = -1

    override val car: T
        get() {
            evaluateStep()
            return sv!!.car
        }

    override val cdr: PersistentList<T>
        get() {
            evaluateStep()
            return sv!!.cdr
        }

    override val size: Int
        get() {
            if (lazySize < 0) {
                var c = 0
                var s1 = evaluateStep()
                while (s1!!.isNotEmpty()) {
                    s1 = s1.cdr
                    c += 1
                }
                lazySize = c
            }
            return lazySize
        }

    @Synchronized
    fun evaluateStep(): PersistentList<T>? {
        if (fn != null) {
            val tfn = fn!!
            fn = null
            val temp = tfn()
            sv = temp
        }
        return sv
    }

    override fun isEmpty(): Boolean {
        return evaluateStep()!!.isEmpty()
    }

    override fun drop(n: Int): PersistentList<T> = drop(n, this)

    override fun take(n: Int): PersistentList<T> = take(n, this)

    override fun <R> map(transform: (T) -> R): PersistentList<R> = map(transform, this)

    override fun filter(predicate: (T) -> Boolean): PersistentList<T> = filter(predicate, this)

    override fun isLazyType(): Boolean = true

    override fun toString(): String {
        return toString(limit = 10000)
    }

    fun toString(limit: Int = 10000): String {
        return commonToString(limit = limit)
    }

    companion object {
        fun <T, R> map(f: (T) -> R, lst: PersistentList<T>): PersistentList<R> {
            return lazySeq {
                if (lst.isEmpty()) nullCons()
                else PersistentList.cons(f(lst.car), map(f, lst.cdr))
            }
        }

        fun <T> filter(p: (T) -> Boolean, lst: PersistentList<T>): PersistentList<T> {
            return lazySeq {
                when {
                    lst.isEmpty() -> nullCons()
                    p(lst.car) -> PersistentList.cons(lst.car, filter(p, lst.cdr))
                    else -> filter(p, lst.cdr)
                }
            }
        }

        fun <T> splitAt(number: Int, lst: PersistentList<T>): Pair<PersistentList<T>, PersistentList<T>> {
            var dest = VList.of<T>()
            var src = lst
            var n = number
            while (n > 0 && src.isNotEmpty()) {
                dest = dest.cons(src.car)
                n--
                src = src.cdr
            }
            return dest.reversed() to src
        }

        fun <T> take(n: Int, lst: PersistentList<T>): PersistentList<T> {
            return lazySeq {
                when {
                    lst.isEmpty() -> nullCons()
                    n == 1 -> PersistentList.cons(lst.car, nullCons())
                    n > 0 -> PersistentList.cons(lst.car, take(n - 1, lst.cdr))
                    else -> nullCons()
                }
            }
        }

        fun <T> drop(n: Int, lst: PersistentList<T>): PersistentList<T> {
            return lazySeq {
                when {
                    lst.isEmpty() -> nullCons()
                    n > 0 -> drop(n - 1, lst.cdr)
                    else -> lst
                }
            }
        }

        fun <T> of(vararg xs: T): PersistentList<T> = lazySeq(xs.iterator())

        private fun <T> lazySeq(iterator: Iterator<T>): PersistentList<T> = lazySeq {
            when {
                iterator.hasNext() -> PersistentList.cons(iterator.next(), lazySeq(iterator))
                else -> nullCons()
            }
        }

        fun <T> lazySeq(fn: () -> PersistentList<T>?): PersistentList<T> = LazyList(fn)

        fun <T> repeatedly(n: Int = -1, fn: () -> T): PersistentList<T> = lazySeq {
            when (n) {
                -1 -> PersistentList.cons(fn(), repeatedly(n, fn))
                0 -> nullCons()
                else -> PersistentList.cons(fn(), repeatedly(n - 1, fn))
            }
        }

        fun <T> lazySeq(x: T, fn: () -> PersistentList<T>) = PersistentList.cons(x, fn)

        fun <T> cycle(xs: PersistentList<T>): PersistentList<T> = lazySeq {
            if (xs.isEmpty()) nullCons()
            else lazySeq(xs.car) { PersistentList.concat(xs.cdr, cycle(xs)) }
        }

        fun <T> repeat(x: T): PersistentList<T> = lazySeq {
            lazySeq(x) { repeat(x) }
        }

        fun <T> iterate(f: (T) -> T, x: T): PersistentList<T> = lazySeq {
            lazySeq(x) { iterate(f, f(x)) }
        }

    }
}
