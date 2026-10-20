package zhiqiu.hs.core

import zhiqiu.hs.core.FortuneStar.*

/** 五行 */
enum class Element(val label: String) {
    金("金"), 木("木"), 水("水"), 火("火"), 土("土")
}

/** 性别 */
enum class Gender { 男, 女 }

/** 东四命 / 西四命 */
enum class Group(val label: String) {
    东四("东四命"), 西四("西四命")
}

/**
 * 八卦（后天八卦）基础属性。
 * direction 为后天方位；innate 先天数；acquired 后天数。
 */
data class Trigram(
    val key: String,        // 乾
    val symbol: String,     // ☰
    val direction: String,  // 西北
    val element: Element,
    val yinYang: String,    // 阳 / 阴
    val familyRole: String, // 父 / 母 / 长男 ...
    val innate: Int,        // 先天数
    val acquired: Int,      // 后天数
    val body: String,       // 对应人体
    val nature: String,     // 自然物象
    val color: String,
    val meaning: String     // 卦义
)

val TRIGRAMS: List<Trigram> = listOf(
    Trigram("乾", "☰", "西北", Element.金, "阳", "父", 1, 6, "头", "天", "大赤", "刚健"),
    Trigram("坤", "☷", "西南", Element.土, "阴", "母", 8, 2, "腹", "地", "黄", "柔顺"),
    Trigram("震", "☳", "东", Element.木, "阳", "长男", 4, 3, "足", "雷", "绿", "震动"),
    Trigram("巽", "☴", "东南", Element.木, "阴", "长女", 5, 4, "股", "风", "青", "入"),
    Trigram("坎", "☵", "北", Element.水, "阳", "中男", 6, 1, "耳", "水", "黑", "陷"),
    Trigram("离", "☲", "南", Element.火, "阴", "中女", 3, 9, "目", "火", "红", "丽"),
    Trigram("艮", "☶", "东北", Element.土, "阳", "少男", 7, 8, "手", "山", "白", "止"),
    Trigram("兑", "☱", "西", Element.金, "阴", "少女", 2, 7, "口", "泽", "白", "悦")
)

fun trigramByKey(key: String): Trigram? = TRIGRAMS.firstOrNull { it.key == key }

/** 命卦数(1-9) → 八卦 */
fun trigramByNumber(n: Int): Trigram = when (n) {
    1 -> trigramByKey("坎")!!
    2 -> trigramByKey("坤")!!
    3 -> trigramByKey("震")!!
    4 -> trigramByKey("巽")!!
    6 -> trigramByKey("乾")!!
    7 -> trigramByKey("兑")!!
    8 -> trigramByKey("艮")!!
    9 -> trigramByKey("离")!!
    else -> error("无效的命卦数: $n")
}

/** 命卦推算结果 */
data class MingGuaResult(
    val trigram: Trigram,
    val group: Group,
    val number: Int
)

/**
 * 八宅命卦推算（东四命 / 西四命）。
 * 公式：
 *  - 1900–1999：男 (100 - 年) % 9；女 (年 - 4) % 9
 *  - 2000–2099：男 (99 - 年) % 9；女 (年 - 3) % 9
 *  余数为 0 视作 9；余数为 5 时男寄坤(2)、女寄艮(8)。
 */
fun calculateMingGua(birthYear: Int, gender: Gender): MingGuaResult {
    val baseYear = if (birthYear < 2000) 100 else 99
    val offset = if (birthYear < 2000) 4 else 3
    val raw = if (gender == Gender.男) (baseYear - birthYear) else (birthYear - offset)
    val rem = ((raw % 9) + 9) % 9
    val number = if (rem == 0) 9 else rem

    val finalNumber = when {
        number == 5 && gender == Gender.男 -> 2   // 男寄坤
        number == 5 && gender == Gender.女 -> 8   // 女寄艮
        else -> number
    }
    val trigram = trigramByNumber(finalNumber)
    val group = when (trigram.key) {
        "坎", "震", "巽", "离" -> Group.东四
        else -> Group.西四
    }
    return MingGuaResult(trigram, group, finalNumber)
}

/** 游年九星 */
enum class FortuneStar(val label: String, val auspicious: Boolean) {
    伏位("伏位", true),
    生气("生气", true),
    延年("延年", true),
    天医("天医", true),
    绝命("绝命", false),
    五鬼("五鬼", false),
    六煞("六煞", false),
    祸害("祸害", false)
}

/**
 * 八宅游年九星映射：命卦 → 该命卦在各方位的吉凶星。
 * 外键为命卦 key，内键为方位（八卦 key）。
 */
val FORTUNE_MAP: Map<String, Map<String, FortuneStar>> = mapOf(
    "坎" to mapOf(
        "坎" to 伏位, "巽" to 生气, "离" to 延年, "震" to 天医,
        "坤" to 绝命, "艮" to 五鬼, "乾" to 六煞, "兑" to 祸害
    ),
    "离" to mapOf(
        "离" to 伏位, "震" to 生气, "坎" to 延年, "巽" to 天医,
        "乾" to 绝命, "兑" to 五鬼, "坤" to 六煞, "艮" to 祸害
    ),
    "震" to mapOf(
        "震" to 伏位, "离" to 生气, "巽" to 延年, "坎" to 天医,
        "兑" to 绝命, "坤" to 五鬼, "艮" to 六煞, "乾" to 祸害
    ),
    "巽" to mapOf(
        "巽" to 伏位, "坎" to 生气, "震" to 延年, "离" to 天医,
        "艮" to 绝命, "乾" to 五鬼, "兑" to 六煞, "坤" to 祸害
    ),
    "乾" to mapOf(
        "乾" to 伏位, "兑" to 生气, "坤" to 延年, "艮" to 天医,
        "离" to 绝命, "震" to 五鬼, "巽" to 六煞, "坎" to 祸害
    ),
    "坤" to mapOf(
        "坤" to 伏位, "艮" to 生气, "乾" to 延年, "兑" to 天医,
        "坎" to 绝命, "震" to 五鬼, "巽" to 六煞, "离" to 祸害
    ),
    "艮" to mapOf(
        "艮" to 伏位, "坤" to 生气, "兑" to 延年, "乾" to 天医,
        "巽" to 绝命, "离" to 五鬼, "震" to 六煞, "坎" to 祸害
    ),
    "兑" to mapOf(
        "兑" to 伏位, "乾" to 生气, "艮" to 延年, "坤" to 天医,
        "震" to 绝命, "坎" to 五鬼, "离" to 六煞, "巽" to 祸害
    )
)

/** 九宫格顺序（后天八卦洛书布局，索引 0..8） */
val BAGUA_GRID: List<Pair<String, String>> = listOf(
    "乾" to "西北", "坎" to "北", "艮" to "东北",
    "兑" to "西", "中" to "中宫", "震" to "东",
    "坤" to "西南", "离" to "南", "巽" to "东南"
)

/** 可放置的家庭成员 / 房间：显示名 to 简称 */
val MEMBERS: List<Pair<String, String>> = listOf(
    "父亲" to "父", "母亲" to "母",
    "长子" to "长子", "次子" to "二子", "三子" to "三子",
    "长女" to "长女", "次女" to "二女", "三女" to "三女",
    "厕所" to "厕", "厨房" to "厨", "客厅" to "厅"
)

/** 简称 → JSON 的 key（用于读取方位解读）。 */
fun displayNameOf(short: String): String = when (short) {
    "父" -> "父亲"; "母" -> "母亲"
    "长子" -> "长子"; "二子" -> "二子"; "三子" -> "三子"
    "长女" -> "长女"; "二女" -> "二女"; "三女" -> "三女"
    "厕" -> "厕所"; "厨" -> "厨房"; "厅" -> "客厅"
    else -> short
}
