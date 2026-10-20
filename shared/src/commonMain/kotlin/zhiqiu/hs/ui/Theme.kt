package zhiqiu.hs.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import zhiqiu.hs.i18n.appFontFamily

object Palette {
    val Background = Color(0xFF0E1014)
    val Surface = Color(0xFF161A21)
    val SurfaceHigh = Color(0xFF1E242D)
    val Primary = Color(0xFF4C8DFF)
    val Success = Color(0xFF34C759)
    val Danger = Color(0xFFFF453A)
    val TextPrimary = Color(0xFFF2F2F7)
    val TextSecondary = Color(0xFF9AA1AD)
    val TextMuted = Color(0xFF6B7280)

    val Gold = Primary
    val GoldSoft = Primary
    val Jade = Success
    val Crimson = Danger
    val Ink = Background
    val InkMid = Surface
    val InkSoft = SurfaceHigh
}

@Composable
fun HouseTheme(content: @Composable () -> Unit) {
    val baseTypography = Typography()
    val family = appFontFamily()
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Palette.Primary,
            onPrimary = Color.White,
            background = Palette.Background,
            surface = Palette.Surface,
            surfaceVariant = Palette.SurfaceHigh,
            onBackground = Palette.TextPrimary,
            onSurface = Palette.TextPrimary
        ),
        typography = Typography(
            displayLarge = baseTypography.displayLarge.copy(fontFamily = family),
            displayMedium = baseTypography.displayMedium.copy(fontFamily = family),
            displaySmall = baseTypography.displaySmall.copy(fontFamily = family),
            headlineLarge = baseTypography.headlineLarge.copy(fontFamily = family),
            headlineMedium = baseTypography.headlineMedium.copy(fontFamily = family),
            headlineSmall = baseTypography.headlineSmall.copy(fontFamily = family),
            titleLarge = baseTypography.titleLarge.copy(fontFamily = family),
            titleMedium = baseTypography.titleMedium.copy(fontFamily = family),
            titleSmall = baseTypography.titleSmall.copy(fontFamily = family),
            bodyLarge = baseTypography.bodyLarge.copy(fontFamily = family),
            bodyMedium = baseTypography.bodyMedium.copy(fontFamily = family),
            bodySmall = baseTypography.bodySmall.copy(fontFamily = family),
            labelLarge = baseTypography.labelLarge.copy(fontFamily = family),
            labelMedium = baseTypography.labelMedium.copy(fontFamily = family),
            labelSmall = baseTypography.labelSmall.copy(fontFamily = family)
        ),
        content = content
    )
}
