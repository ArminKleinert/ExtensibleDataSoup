package kleinert.soap.cons


class LazyList<T>(fn: () -> ImmutableLazyList<T>?) : ImmutableLazyList<T> {
    private var fn: (() -> ImmutableLazyList<T>?)? = fn
    private var sv: ImmutableLazyList<T>? = null
    private var lazySize: Int = -1

    override val car: T
        get() {
            evaluateStep()
            return sv!!.car
        }

    override val cdr: ImmutableLazyList<T>
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
    fun evaluateStep(): ImmutableLazyList<T>? {
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

    override fun iterator(): Iterator<T> {
        return asSequence().iterator()
    }

    override fun <R> sameTypeFromList(list: List<R>): ImmutableLazyList<R> {
        return ImmutableLazyList.from(list)
    }

    override fun isLazyType(): Boolean = true

    override fun toString(): String {
        return toString(limit = 100000)
    }

    fun toString(limit: Int = 100000): String {
        return commonToString(limit = limit)
    }

    companion object {
//        fun <T, R> map(f: (T) -> R, lst: Cons<T>): Cons<R> {
//            return LazyCons {
//                if (lst.isEmpty()) nullCons()
//                else Cons.cons(f(lst.car), map(f, lst.cdr))
//            }
//        }
//
//        fun <T> filter(p: (T) -> Boolean, lst: Cons<T>): Cons<T> {
//            return LazyCons {
//                when {
//                    lst.isEmpty() -> nullCons()
//                    p(lst.car) -> Cons.cons(lst.car, filter(p, lst.cdr))
//                    else -> filter(p, lst.cdr)
//                }
//            }
//        }
//
//        fun <T> take(n: Long, lst: Cons<T>): Cons<T> {
//            return LazyCons {
//                when {
//                    lst.isEmpty() -> nullCons()
//                    n == 1L -> Cons.cons(lst.car, nullCons())
//                    n > 0 -> Cons.cons(lst.car, take(n - 1, lst.cdr))
//                    else -> nullCons()
//                }
//            }
//        }
//
//        fun <T> drop(n: Int, lst: Cons<T>): Cons<T> {
//            return LazyCons {
//                when {
//                    lst.isEmpty() -> nullCons()
//                    n > 0 -> drop(n - 1, lst.cdr)
//                    else -> lst
//                }
//            }
//        }

        fun <T> of(vararg xs: T): ImmutableLazyList<T> = lazySeq(xs.asSequence())

        fun <T> fromIterator(iterator: Iterator<T>): ImmutableLazyList<T> = lazySeq {
            when {
                iterator.hasNext() -> ImmutableLazyList.cons(iterator.next(), fromIterator(iterator))
                else -> nullCons()
            }
        }

        fun <T> lazySeq(seq: Sequence<T>): ImmutableLazyList<T> = fromIterator(seq.iterator())

        fun <T> lazySeq(fn: () -> ImmutableLazyList<T>?): ImmutableLazyList<T> = LazyList(fn)

        fun <T> repeatedly(n: Int = -1, fn: () -> T): ImmutableLazyList<T> = lazySeq {
            when (n) {
                -1 -> ImmutableLazyList.cons(fn(), repeatedly(n, fn))
                0 -> nullCons()
                else -> ImmutableLazyList.cons(fn(), repeatedly(n - 1, fn))
            }
        }

        fun <T> lazySeq(x: T, fn: () -> ImmutableLazyList<T>) = ImmutableLazyList.cons(x, fn)

        fun <T> cycle(xs: ImmutableLazyList<T>): ImmutableLazyList<T> = lazySeq {
            if (xs.isEmpty()) nullCons()
            else lazySeq(xs.car) { ImmutableLazyList.concat(xs.cdr, cycle(xs)) }
        }

        fun <T> repeat(x: T): ImmutableLazyList<T> = lazySeq {
            lazySeq(x) { repeat(x) }
        }

        fun <T> iterate(f: (T) -> T, x: T): ImmutableLazyList<T> = lazySeq {
            lazySeq(x) { iterate(f, f(x)) }
        }
    }
}
