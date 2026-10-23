package zhiqiu.hs.storage

object AppSettings {
    private const val LANG = "app_language"
    private const val GLASS = "app_glass"

    fun language(): String = AppStore.getStringOrNull(LANG) ?: ""

    fun setLanguage(tag: String) {
        AppStore.putString(LANG, tag)
    }

    fun glass(): String = AppStore.getStringOrNull(GLASS) ?: ""

    fun setGlass(value: String) {
        AppStore.putString(GLASS, value)
    }
}
