package zhiqiu.hs.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class DirectionEntry(
    val direction: String = "",
    val interpretation: String = ""
)

@Serializable
data class HouseData(
    @SerialName("element_direction_interpretations")
    val interpretations: Map<String, Map<String, DirectionEntry>> = emptyMap()
)

object HouseDataLoader {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    /** 解析内嵌的方位解读数据（可直接调用，无需挂起）。 */
    fun load(): HouseData = json.decodeFromString(HOUSE_DATA_JSON)
}

/** 取某成员在某八卦方位下的解读（自动把简称转为 JSON 的显示名 key）。 */
fun HouseData.interpretationFor(memberShort: String, baguaKey: String): String? {
    val display = displayNameOf(memberShort)
    val memberObj = interpretations[display] ?: return null
    // 中宫不在方位解读体系中
    if (baguaKey == "中") return null
    return memberObj[baguaKey]?.interpretation?.takeIf { it.isNotBlank() }
}

/** 取某成员在所有方位下的解读（用于「查看所有方位解析」）。 */
fun HouseData.allDirectionsFor(memberShort: String): List<Pair<String, String>> {
    val display = displayNameOf(memberShort)
    val memberObj = interpretations[display] ?: return emptyList()
    return BAGUA_GRID.mapNotNull { (key, dir) ->
        if (key == "中") return@mapNotNull null
        memberObj[key]?.interpretation?.takeIf { it.isNotBlank() }?.let { dir to it }
    }
}
