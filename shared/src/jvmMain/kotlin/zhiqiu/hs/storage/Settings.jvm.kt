package zhiqiu.hs.storage

import java.util.prefs.Preferences

private class HouseStudyPrefs

actual fun provideKeyValueStore(): KeyValueStore {
    val prefs = Preferences.userNodeForPackage(HouseStudyPrefs::class.java)
    return object : KeyValueStore {
        override fun getString(key: String): String? = prefs.get(key, null)
        override fun setString(key: String, value: String) {
            prefs.put(key, value)
        }
    }
}
