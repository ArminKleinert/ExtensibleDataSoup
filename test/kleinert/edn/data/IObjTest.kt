package kleinert.edn.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class IObjTest {
    @Test
    fun valueOf_StringAndObj() {
        val meta = "meta"
        val io = IObj.valueOf(meta, "obj")
        Assertions.assertTrue(io is IObj<String>)
        Assertions.assertEquals("obj", io.obj)
        Assertions.assertEquals(mapOf(Keyword["tag"] to meta), io.meta)
        Assertions.assertEquals(IObj(mapOf(Keyword["tag"] to meta), "obj"), io)
    }

    @Test
    fun valueOf_SymbolAndObj() {
        val meta = Symbol.symbol("meta")
        val io = IObj.valueOf(meta, "obj")
        Assertions.assertTrue(io is IObj<String>)
        Assertions.assertEquals("obj", io.obj)
        Assertions.assertEquals(mapOf(Keyword["tag"] to meta), io.meta)
        Assertions.assertEquals(IObj(mapOf(Keyword["tag"] to meta), "obj"), io)
    }

    @Test
    fun valueOf_KeywordAndObj() {
        val meta = Keyword.get("meta")
        val io = IObj.valueOf(meta, "obj")
        Assertions.assertTrue(io is IObj<String>)
        Assertions.assertEquals("obj", io.obj)
        Assertions.assertEquals(mapOf(meta to true), io.meta)
        Assertions.assertEquals(IObj(mapOf(meta to true), "obj"), io)
    }

    @Test
    fun valueOf_MapAndObj() {
        val meta = mapOf(Keyword["tag"] to 123)
        val io = IObj.valueOf(meta, "obj")
        Assertions.assertTrue(io is IObj<String>)
        Assertions.assertEquals("obj", io.obj)
        Assertions.assertEquals(meta, io.meta)
        Assertions.assertEquals(IObj(meta, "obj"), io)
    }

    @Test
    fun valueOf_ObjOnly() {
        val io = IObj.valueOf("obj")
        Assertions.assertTrue(io is IObj<String>)
        Assertions.assertEquals("obj", IObj.valueOf("obj").obj)
        Assertions.assertEquals(mapOf<Any?, Any?>(), IObj.valueOf("obj").meta)
    }

    @Test
    fun getMeta() {
    }

    @Test
    fun getObj() {
    }

    @Test
    fun component1() {
    }

    @Test
    fun component2() {
    }

    @Test
    fun copy() {
    }

    @Test
    fun equals() {
    }

}