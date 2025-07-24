package kleinert.soap.data

/**
 * @property meta The metadata map.
 * @property obj The object itself.
 *
 * @author Armin Kleinert
 */
data class IObj<T>(val meta: Map<out Any?, Any?>?, val obj: T) {
    companion object {
        fun <T> valueOf(meta:String, obj:T) = IObj(PersistentMap(mapOf(Keyword["tag"] to meta)), obj)
        fun <T> valueOf(meta:Symbol, obj:T) = IObj(PersistentMap(mapOf(Keyword["tag"] to meta)), obj)
        fun <T> valueOf(meta:Keyword, obj:T) = IObj(PersistentMap(mapOf(meta to true)), obj)
        fun <T> valueOf(meta:Map<out Any?, Any?>, obj:T) = IObj(meta, obj)
        fun <T> valueOf(obj:T) = IObj(mapOf(), obj)
    }
}
