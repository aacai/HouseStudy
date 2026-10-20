@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@file:OptIn(InternalResourceApi::class)

package zhiqiu.hs.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import org.jetbrains.compose.resources.ComposeEnvironment
import org.jetbrains.compose.resources.DefaultComposeEnvironment
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.LocalComposeEnvironment
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.rememberResourceEnvironment

enum class AppLang(val tag: String) {
    SYSTEM(""),
    ZH("zh"),
    EN("en");

    companion object {
        fun fromTag(tag: String): AppLang = entries.firstOrNull { it.tag == tag } ?: SYSTEM
    }
}

@Composable
fun AppLanguageProvider(lang: AppLang, content: @Composable () -> Unit) {
    val system = rememberResourceEnvironment()
    val environment = remember(system, lang) {
        if (lang == AppLang.SYSTEM) system
        else ResourceEnvironment(
            language = LanguageQualifier(lang.tag),
            script = system.script,
            region = system.region,
            theme = system.theme,
            density = system.density
        )
    }
    val composeEnvironment = remember(environment) { FixedComposeEnvironment(environment) }
    CompositionLocalProvider(LocalComposeEnvironment provides composeEnvironment) {
        content()
    }
}

private class FixedComposeEnvironment(
    private val environment: ResourceEnvironment
) : ComposeEnvironment {
    @Composable
    override fun rememberEnvironment(): ResourceEnvironment = environment
}
