package zhiqiu.hs.storage

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** 户型布局：宫位索引(0..8) → 该宫位放置的成员简称列表。 */
typealias LayoutMap = Map<Int, List<String>>

object HouseRepository {
    private val store = provideKeyValueStore()
    private val json = Json { ignoreUnknownKeys = true }
    private const val DEFAULT = "default"
    private const val ARCHIVES = "archives"

    fun saveDefault(map: LayoutMap) {
        store.setString(DEFAULT, json.encodeToString(map))
    }

    fun loadDefault(): LayoutMap? =
        store.getString(DEFAULT)?.let { runCatching { json.decodeFromString<LayoutMap>(it) }.getOrNull() }

    fun saveNamed(name: String, map: LayoutMap) {
        if (name.isBlank()) return
        store.setString(name, json.encodeToString(map))
        val set = listArchives().toMutableSet().apply { add(name) }
        store.setString(ARCHIVES, json.encodeToString(set))
    }

    fun loadNamed(name: String): LayoutMap? =
        store.getString(name)?.let { runCatching { json.decodeFromString<LayoutMap>(it) }.getOrNull() }

    fun deleteNamed(name: String) {
        store.setString(name, "{}")
        val set = listArchives().toMutableSet().apply { remove(name) }
        store.setString(ARCHIVES, json.encodeToString(set))
    }

    fun listArchives(): Set<String> =
        store.getString(ARCHIVES)?.let { runCatching { json.decodeFromString<Set<String>>(it) }.getOrNull() }
            ?: emptySet()
}
