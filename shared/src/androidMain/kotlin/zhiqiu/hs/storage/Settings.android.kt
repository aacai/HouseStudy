package zhiqiu.hs.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private var appContext: Context? = null

/** 在 Application/Activity 中尽早调用，注入 Android Context。 */
fun initAndroidContext(context: Context) {
    appContext = context.applicationContext
}

actual fun provideKeyValueStore(): KeyValueStore {
    val ctx = appContext
        ?: error("Android Context 未初始化，请先调用 initAndroidContext(this)")
    val prefs: SharedPreferences = ctx.getSharedPreferences("housestudy", Context.MODE_PRIVATE)
    return object : KeyValueStore {
        override fun getString(key: String): String? = prefs.getString(key, null)
        override fun setString(key: String, value: String) {
            prefs.edit { putString(key, value) }
        }
    }
}
