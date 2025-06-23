package kleinert.soap.cons


class LazyCons<T>(fn: () -> Cons<T>?) : Cons<T> {
    private var fn: (() -> Cons<T>?)? = fn
    private var sv: Cons<T>? = null
    private var s: Cons<T>? = null

    override val car: T
        get() {
            seq()
            return if (s == null) throw NoSuchElementException() else s!!.first()
        }

    override val cdr: Cons<T>
        get() = next() ?: Cons.of()

    override val size: Int
        get() = this.count()

    @Synchronized
    fun sval(): Cons<T>? {
        if (fn != null) {
            sv = fn!!.invoke()
            fn = null
        }
        return if (sv != null) sv else s
    }

    @Synchronized
    fun seq(): Cons<T>? {
        sval()
        if (sv != null) {
            var ls = sv
            sv = null
            while (ls is LazyCons<*>) {
                ls = (ls as LazyCons<T>).sval()
            }
            s = rtSeq(ls)
        }
        return s
    }

    fun rtSeq(coll: Cons<T>?) = when (coll) {
        null -> null
        is LazyCons<T> -> coll.seq()
        else -> Cons.fromIterable(coll.toList())
    }

    fun count(): Int {
        var c = 0
        run {
            var s = this.seq()
            while (s != null) {
                ++c
                s = s.next()
            }
        }
        return c
    }

    override operator fun next(): Cons<T>? {
        seq()
        return if (s == null) null else s!!.next()
    }

    override fun cons(element: T): Cons<T> {
        return ConsCell(element, this)
    }

    override fun cleared(): Cons<T> {
        return Cons.of()
    }

    override fun equals(other: Any?): Boolean {
        val s = seq()
        return super.commonEqualityCheck(other)
    }

    override fun isEmpty(): Boolean {
        return seq() == null
    }

    override operator fun contains(element: T): Boolean {
        var s = this.seq()
        while (s != null) {
            if (s.first() == element) {
                return true
            }
            s = s.next()
        }
        return false
    }

    override fun iterator(): Iterator<T> {
        return asSequence().iterator()
    }

    override fun toList(): List<T> {
        return ArrayList<T>(this)
    }

    override fun indexOf(element: T): Int {
        var s = seq()
        var i = 0
        while (s != null) {
            if (s.first() == element) {
                return i
            }
            s = s.next()
            ++i
        }
        return -1
    }

    override fun lastIndexOf(element: T): Int {
        return toList().lastIndexOf(element)
    }

    override fun listIterator(): ListIterator<T> {
        return toList().listIterator()
    }

    override fun listIterator(index: Int): ListIterator<T> {
        return toList().listIterator(index)
    }

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> {
        return Cons.fromIterable(list)
    }

    override fun isLazyType(): Boolean = true

    override fun hashCode(): Int {
        var result = fn?.hashCode() ?: 0
        result = 31 * result + (sv?.hashCode() ?: 0)
        result = 31 * result + (s?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun <T, R> map(f: (T) -> R, lst: Cons<T>): Cons<R> {
            return LazyCons {
                println("car: " + lst.car)
                if (lst.isEmpty()) nullCons()
                else Cons.cons(f(lst.car), map(f, lst.cdr))
            }
        }

        fun <T> filter(p: (T) -> Boolean, lst: Cons<T>): Cons<T> {
            return LazyCons {
                when {
                    lst.isEmpty() -> nullCons()
                    p(lst.car) -> filter(p, lst.cdr)
                    else -> Cons.cons(lst.car, filter(p, lst.cdr))
                }
            }
        }

        fun <T> take(n: Long, lst: Cons<T>): Cons<T> {
            return LazyCons {
                when {
                    lst.isEmpty() -> nullCons()
                    n > 0 -> Cons.cons(lst.car, take(n-1, lst.cdr))
                    else -> nullCons()
                }
            }
        }

        fun <T> drop(n: Int, lst: Cons<T>): Cons<T> {
            return LazyCons {
                when {
                    lst.isEmpty() -> nullCons()
                    n > 0 -> drop(n-1, lst.cdr)
                    else -> lst
                }
            }
        }

        fun <T, R> fmap(f: (T)->R, p: (T) -> Boolean, xs: Cons<T>): Cons<R> {
            return LazyCons {
                when {
                    xs.isEmpty() -> nullCons()
                    p(xs.car) -> Cons.cons(f(xs.car), fmap(f, p, xs.cdr))
                    else -> fmap(f, p, xs.cdr)
                }
            }
        }
    }
}
