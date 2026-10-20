package zhiqiu.hs.storage

object AppSettings {
    private val store = provideKeyValueStore()
    private const val LANG = "app_language"

    fun language(): String = store.getString(LANG) ?: ""

    fun setLanguage(tag: String) {
        store.setString(LANG, tag)
    }
}
