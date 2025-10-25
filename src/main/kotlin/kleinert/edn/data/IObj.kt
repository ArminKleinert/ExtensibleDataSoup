package kleinert.edn.data

/**
 * @property meta The metadata map.
 * @property obj The object itself.
 *
 * @author Armin Kleinert
 */
data class IObj<T>(val meta: Map<out Any?, Any?>?, val obj: T) {
    companion object {
        /**
         * Creates an [IObj] with the meta-data being the map {:tag [meta]}.
         */
        fun <T> valueOf(meta:String, obj:T) = IObj(PersistentMap(mapOf(Keyword["tag"] to meta)), obj)
        /**
         * Creates an [IObj] with the meta-data being the map {:tag [meta]}.
         */
        fun <T> valueOf(meta: Symbol, obj:T) = IObj(PersistentMap(mapOf(Keyword["tag"] to meta)), obj)
        /**
         * Creates an [IObj] with the meta-data being the map {[meta] true}.
         */
        fun <T> valueOf(meta: Keyword, obj:T) = IObj(PersistentMap(mapOf(meta to true)), obj)
        /**
         * Equivalent to a normal constructor call for [IObj].
         */
        fun <T> valueOf(meta:Map<out Any?, Any?>, obj:T) = IObj(meta, obj)
        /**
         * Creates an [IObj] with empty meta-data.
         */
        fun <T> valueOf(obj:T) = IObj(mapOf(), obj)
    }
}
