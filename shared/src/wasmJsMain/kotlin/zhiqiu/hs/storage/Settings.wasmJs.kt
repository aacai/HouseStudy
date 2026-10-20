package zhiqiu.hs.storage

import kotlinx.browser.window

private class BrowserKeyValueStore : KeyValueStore {
    private val prefix = "hs_"
    override fun getString(key: String): String? = window.localStorage.getItem(prefix + key)
    override fun setString(key: String, value: String) {
        window.localStorage.setItem(prefix + key, value)
    }
}

actual fun provideKeyValueStore(): KeyValueStore = BrowserKeyValueStore()
