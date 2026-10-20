package zhiqiu.hs.storage

/** 跨平台键值存储接口（用于存档）。 */
interface KeyValueStore {
    fun getString(key: String): String?
    fun setString(key: String, value: String)
}

expect fun provideKeyValueStore(): KeyValueStore
