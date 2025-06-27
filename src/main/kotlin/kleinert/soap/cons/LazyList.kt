package kleinert.soap.cons


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

    private fun evaluate(): PersistentList<T> = PersistentList.from(toList())

    override fun drop(n: Int): PersistentList<T> = drop(n, this)

    override fun take(n: Int): PersistentList<T> = take(n, this)

    override fun <R> map(transform: (T) -> R): PersistentList<R> = map(transform, this)

    override fun filter(predicate: (T) -> Boolean): PersistentList<T> = filter(predicate, this)

    override fun filterNot(predicate: (T) -> Boolean): PersistentList<T> = filterNot(predicate, this)

    override fun filterNotNull(): PersistentList<T> = filterNotNull(this)

    override fun distinct(): PersistentList<T> = distinct(this)

    override fun dropWhile(predicate: (T) -> Boolean): PersistentList<T> = dropWhile(predicate, this)

    override fun takeWhile(predicate: (T) -> Boolean): PersistentList<T> = takeWhile(predicate, this)

    override fun <R> flatMap(transform: (T) -> Iterable<R>): PersistentList<R> = flatMap(transform, this)


    override fun <R> flatMapIndexed(transform: (index: Int, T) -> Iterable<R>): PersistentList<R> =
        flatMapIndexed(transform, this)

    override fun withIndex(): PersistentList<IndexedValue<T>> =
        mapIndexed { index: Int, t: T -> IndexedValue(index, t) }

    override fun <R> zip(other: PersistentList<R>): PersistentList<Pair<T, R>> =
        zip(this, other)

    override operator fun minus(element: T): PersistentList<T> = minus(setOf(element))

    override operator fun minus(elements: Iterable<T>): PersistentList<T> = minus(elements.toSet())


    // TODO: Test
    override operator fun minus(elements: Set<T>): PersistentList<T> =
        minus(elements, this)

    override fun isLazyType(): Boolean = true

    override fun toString(): String {
        return toString(limit = 10000)
    }

    fun toString(limit: Int = 10000): String {
        return commonToString(limit = limit)
    }

    override fun equals(other: Any?) =
        commonEqualityCheck(other)

    companion object {
        fun <T, R> map(f: (T) -> R, lst: PersistentList<T>): PersistentList<R> = lazySeq {
            if (lst.isEmpty()) nullCons()
            else PersistentList.cons(f(lst.car), map(f, lst.cdr))
        }

        fun <T> filter(p: (T) -> Boolean, lst: PersistentList<T>): PersistentList<T> = lazySeq {
            when {
                lst.isEmpty() -> lst
                p(lst.car) -> PersistentList.cons(lst.car, filter(p, lst.cdr))
                else -> filter(p, lst.cdr)
            }
        }

        fun <T> filterIndexed(
            predicate: (index: Int, T) -> Boolean,
            xs: PersistentList<T>,
            index: Int = 0
        ): PersistentList<T> = lazySeq {
            when {
                xs.isEmpty() -> nullCons()
                predicate(index, xs.car) -> PersistentList.cons(xs.car, filterIndexed(predicate, xs.cdr, index + 1))
                else -> filterIndexed(predicate, xs.cdr, index + 1)
            }
        }

        fun <T> filterNotNull(lst: PersistentList<T>): PersistentList<T> =
            filter({ it != null }, lst)

        fun <T> filterNot(p: (T) -> Boolean, lst: PersistentList<T>): PersistentList<T> =
            filter({ !p(it) }, lst)

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
            }.evaluate()
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

        fun <T> of(vararg xs: T): LazyList<T> = lazySeq(xs.iterator())

        private fun <T> lazySeq(iterator: Iterator<T>): LazyList<T> = lazySeq {
            when {
                iterator.hasNext() -> PersistentList.cons(iterator.next(), lazySeq(iterator))
                else -> nullCons()
            }
        }

        fun <T> lazySeq(fn: () -> PersistentList<T>?): LazyList<T> = LazyList(fn)

        fun <T> repeatedly(n: Int = -1, fn: () -> T): PersistentList<T> = lazySeq {
            when (n) {
                -1 -> PersistentList.cons(fn(), repeatedly(n, fn))
                0 -> nullCons()
                else -> PersistentList.cons(fn(), repeatedly(n - 1, fn))
            }
        }

        private fun <T> lazySeq(x: T, fn: () -> PersistentList<T>) = PersistentList.cons(x, fn)

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

        fun <T> distinct(xs: PersistentList<T>, memo: MutableSet<T> = mutableSetOf()): PersistentList<T> =
            lazySeq {
                when {
                    xs.isEmpty() -> nullCons()
                    memo.contains(xs.car) -> distinct(xs.cdr, memo)
                    else -> {
                        memo.add(xs.car)
                        lazySeq(xs.car) { distinct(xs.cdr, memo) }
                    }
                }
            }

        fun <T> minus(elements: Set<T>, xs: PersistentList<T>): PersistentList<T> = when {
            xs.isEmpty() -> xs
            elements.isEmpty() -> xs
            else -> filterNot({ it in elements }, xs)
        }

        fun <T> dropWhile(predicate: (T) -> Boolean, xs: PersistentList<T>): PersistentList<T> = lazySeq {
            when {
                xs.isEmpty() -> nullCons<T>()
                !predicate(xs.car) -> xs
                else -> dropWhile(predicate, xs.cdr)
            }
        }


        fun <T> takeWhile(predicate: (T) -> Boolean, xs: PersistentList<T>): PersistentList<T> = lazySeq {
            when {
                xs.isEmpty() -> nullCons<T>()
                predicate(xs.car) -> PersistentList.cons(xs.car, takeWhile(predicate, xs.cdr))
                else -> nullCons()
            }
        }.evaluate()

        fun <T, R> flatMap(transform: (T) -> Iterable<R>, xs: PersistentList<T>): PersistentList<R> = lazySeq {
            when {
                xs.isEmpty() -> nullCons<R>()
                else -> PersistentList.concat(
                    PersistentList.from(transform(xs.car)),
                    flatMap(transform, xs.cdr)
                )
            }
        }

        fun <T, R> flatMapIndexed(
            transform: (index: Int, T) -> Iterable<R>,
            xs: PersistentList<T>,
            init: Int = 0
        ): PersistentList<R> = lazySeq {
            if (xs.isEmpty()) nullCons()
            else PersistentList.concat(
                PersistentList.from(transform(init, xs.car)),
                flatMapIndexed(transform, xs.cdr, init + 1)
            )
        }

        // TODO: Test
        fun <T, R : Any> mapNotNull(transform: (T) -> R?, xs: PersistentList<T>): PersistentList<R> = lazySeq {
            if (xs.isEmpty()) nullCons()
            else {
                val temp = transform(xs.car)
                if (temp == null) mapNotNull(transform, xs.cdr)
                else PersistentList.cons(temp as R, mapNotNull(transform, xs.cdr))
            }
        }

        // TODO: Test
        fun <R, T> zip(xs: PersistentList<T>, ys: PersistentList<R>): PersistentList<Pair<T, R>> = lazySeq {
            when {
                xs.isEmpty() -> nullCons()
                ys.isEmpty() -> nullCons()
                else -> PersistentList.cons(xs.car to ys.car, zip(xs.cdr, ys.cdr))
            }
        }

        fun <R, T> windowed(
            size: Int,
            xs: PersistentList<T>,
            step: Int = 1,
            partialEndWindow: Boolean = false,
            transform: (PersistentList<T>) -> R
        ): PersistentList<R> =
            lazySeq {
                val front = xs.take(size)
                when {
                    front.isEmpty() -> nullCons()
                    front.size < size -> if (partialEndWindow) PersistentList.of(transform(front)) else nullCons() // The entire list was iterated.
                    front.size == size -> PersistentList.cons(
                        transform(front),
                        windowed(size, xs.drop(step), step, partialEndWindow, transform)
                    )

                    else -> throw IllegalStateException()
                }
            }

        fun <T> windowed(
            size: Int,
            xs: PersistentList<T>,
            step: Int = 1,
            partialEndWindow: Boolean = false
        ): PersistentList<PersistentList<T>> =
            lazySeq {
                val front = xs.take(size)
                when {
                    front.isEmpty() -> nullCons()
                    front.size < size -> if (partialEndWindow) PersistentList.of(front) else nullCons() // The entire list was iterated.
                    front.size == size -> PersistentList.cons(
                        front,
                        windowed(size, xs.drop(step), step, partialEndWindow)
                    )

                    else -> throw IllegalStateException()
                }
            }
    }
}
