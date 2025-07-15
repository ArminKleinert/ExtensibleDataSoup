package kleinert.soap.data

data class IObj<T>(val meta: Map<out Any?, Any?>?, val obj: T)
