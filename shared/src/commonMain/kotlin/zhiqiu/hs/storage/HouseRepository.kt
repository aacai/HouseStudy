package zhiqiu.hs.storage

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** 户型布局：宫位索引(0..8) → 该宫位放置的成员简称列表。 */
typealias LayoutMap = Map<Int, List<String>>

object HouseRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private const val DEFAULT = "default"
    private const val ARCHIVES = "archives"

    fun saveDefault(map: LayoutMap) {
        AppStore.putString(DEFAULT, json.encodeToString(map))
    }

    fun loadDefault(): LayoutMap? =
        AppStore.getStringOrNull(DEFAULT)?.let { runCatching { json.decodeFromString<LayoutMap>(it) }.getOrNull() }

    fun saveNamed(name: String, map: LayoutMap) {
        if (name.isBlank()) return
        AppStore.putString(name, json.encodeToString(map))
        val set = listArchives().toMutableSet().apply { add(name) }
        AppStore.putString(ARCHIVES, json.encodeToString(set))
    }

    fun loadNamed(name: String): LayoutMap? =
        AppStore.getStringOrNull(name)?.let { runCatching { json.decodeFromString<LayoutMap>(it) }.getOrNull() }

    fun deleteNamed(name: String) {
        AppStore.putString(name, "{}")
        val set = listArchives().toMutableSet().apply { remove(name) }
        AppStore.putString(ARCHIVES, json.encodeToString(set))
    }

    fun listArchives(): Set<String> =
        AppStore.getStringOrNull(ARCHIVES)?.let { runCatching { json.decodeFromString<Set<String>>(it) }.getOrNull() }
            ?: emptySet()
}
