package zhiqiu.hs.storage

private class MemoryKeyValueStore : KeyValueStore {
    private val map = mutableMapOf<String, String>()
    override fun getString(key: String): String? = map[key]
    override fun setString(key: String, value: String) {
        map[key] = value
    }
}

actual fun provideKeyValueStore(): KeyValueStore = MemoryKeyValueStore()
