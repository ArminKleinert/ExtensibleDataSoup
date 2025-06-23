package kleinert.soap.cons


class LazyCons<T>(fn: () -> Cons<T>?) : Cons<T> {
    private var fn: (() -> Cons<T>?)? = fn
    private var sv: Cons<T>? = null
    private var lazySize: Int = -1

    override val car: T
        get() {
            evaluateStep()
            return sv!!.car
        }

    override val cdr: Cons<T>
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
    fun evaluateStep(): Cons<T>? {
        if (fn != null) {
            val tfn = fn!!
            fn = null
            val temp = tfn()
            sv = temp
        }
        return sv
    }


    override fun cons(element: T): Cons<T> {
        return ConsCell(element, this)
    }

    override fun cleared(): Cons<T> {
        return Cons.of()
    }

    override fun isEmpty(): Boolean {
        return evaluateStep()!!.isEmpty()
    }

    override fun iterator(): Iterator<T> {
        return asSequence().iterator()
    }

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> {
        return Cons.fromIterable(list)
    }

    override fun isLazyType(): Boolean = true

    companion object {
        fun <T, R> map(f: (T) -> R, lst: Cons<T>): Cons<R> {
            return LazyCons {
                if (lst.isEmpty()) nullCons()
                else Cons.cons(f(lst.car), map(f, lst.cdr))
            }
        }

        fun <T> filter(p: (T) -> Boolean, lst: Cons<T>): Cons<T> {
            return LazyCons {
                when {
                    lst.isEmpty() -> nullCons()
                    p(lst.car) -> Cons.cons(lst.car, filter(p, lst.cdr))
                    else -> filter(p, lst.cdr)
                }
            }
        }

        fun <T> take(n: Long, lst: Cons<T>): Cons<T> {
            return LazyCons {
                when {
                    lst.isEmpty() -> nullCons()
                    n == 1L -> Cons.cons(lst.car, nullCons())
                    n > 0 -> Cons.cons(lst.car, take(n - 1, lst.cdr))
                    else -> nullCons()
                }
            }
        }

        fun <T> drop(n: Int, lst: Cons<T>): Cons<T> {
            return LazyCons {
                when {
                    lst.isEmpty() -> nullCons()
                    n > 0 -> drop(n - 1, lst.cdr)
                    else -> lst
                }
            }
        }

        fun <T> fromIterator(iterator: Iterator<T>): Cons<T> {
            return LazyCons {
                when {
                    iterator.hasNext() -> Cons.cons(iterator.next(), fromIterator(iterator))
                    else -> nullCons()
                }
            }
        }
        fun <T> of(seq: Sequence<T>): Cons<T> {
            return LazyCons.fromIterator(seq.iterator())
        }

        fun <T, R> fmap(f: (T) -> R, p: (T) -> Boolean, xs: Cons<T>): Cons<R> {
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
