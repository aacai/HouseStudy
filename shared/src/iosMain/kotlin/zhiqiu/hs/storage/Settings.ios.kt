package zhiqiu.hs.storage

import platform.Foundation.NSUserDefaults

actual fun provideKeyValueStore(): KeyValueStore {
    val defaults = NSUserDefaults.standardUserDefaults
    return object : KeyValueStore {
        override fun getString(key: String): String? = defaults.stringForKey(key)
        override fun setString(key: String, value: String) {
            defaults.setObject(value, key)
        }
    }
}
