package zhiqiu.hs.i18n

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import zhiqiu.hs.generated.resources.*

@Composable
actual fun appFontFamily(): FontFamily = FontFamily(
    Font(Res.font.noto_sc_regular, FontWeight.Normal),
    Font(Res.font.noto_sc_bold, FontWeight.Bold)
)
