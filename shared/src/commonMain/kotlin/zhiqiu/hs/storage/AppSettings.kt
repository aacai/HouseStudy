package zhiqiu.hs.storage

object AppSettings {
    private const val LANG = "app_language"

    fun language(): String = AppStore.getStringOrNull(LANG) ?: ""

    fun setLanguage(tag: String) {
        AppStore.putString(LANG, tag)
    }
}
