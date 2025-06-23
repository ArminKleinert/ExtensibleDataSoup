package kleinert.soap.cons


class LazyCons<T> : Cons<T> {
    companion object {
        fun <T, R> map(f: (T) -> R, lst: Cons<T>): Cons<R> {
            println("HERE "+lst)
            return LazyCons {
                println(":"+lst + " "+lst.isEmpty())
                if (lst.isEmpty()) nullCons()
                else map(f, lst.cdr).cons(f(lst.car))
            }
        }
    }

    private val LOCK = Any()
    private var fn: (() -> Cons<T>?)?
    private var sv: Cons<T>?
    private var bufferedSize: Int = -1

    constructor (fn: (() -> Cons<T>?)?) {
        this.fn = fn
        this.sv = null
    }

    constructor (head: T, fn: (() -> Cons<T>?)?) {
        this.sv = ConsCell(head, LazyCons(fn))
        this.fn = null
    }

    private fun evaluateStep(): Cons<T> =
        if (fn != null)
            synchronized(LOCK) {
                if (fn != null) {
                    val temp = fn!!()
                    sv = temp ?: nullCons()
                }
                sv!!
            }
        else
            sv!!

    override val car: T
        get() = evaluateStep().car

    override val cdr: Cons<T>
        get() = evaluateStep().cdr

    override val size: Int
        get() {
            if (bufferedSize >= 0)
                return bufferedSize

            var seq: Cons<T>
            var count = 0
            do {
                seq = evaluateStep()
                if (seq.isEmpty())
                    break
                count++
            } while (true)
            bufferedSize = count
            return bufferedSize
        }

    override fun isEmpty(): Boolean = evaluateStep().isEmpty()

    override fun cleared(): Cons<T> = nullCons()

    override fun <R> sameTypeFromList(list: List<R>): Cons<R> = Cons.fromIterable(list)
}