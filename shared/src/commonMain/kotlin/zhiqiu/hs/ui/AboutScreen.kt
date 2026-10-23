package zhiqiu.hs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.LayoutGrid
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Save
import org.jetbrains.compose.resources.stringResource
import zhiqiu.hs.generated.resources.*

@Composable
fun AboutScreen(
    langTag: String,
    glassStyle: GlassStyle,
    onLangChange: (String) -> Unit,
    onGlassStyle: (GlassStyle) -> Unit
) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 18.dp)
    ) {
        ScreenHeader(
            stringResource(Res.string.about_title),
            stringResource(Res.string.about_subtitle),
            icon = Lucide.Info
        )
        Spacer(Modifier.height(14.dp))

        AboutCard(stringResource(Res.string.about_intro_title)) {
            Text(
                stringResource(Res.string.about_intro),
                color = Palette.TextSecondary,
                fontSize = 12.5.sp,
                lineHeight = 19.sp
            )
        }

        Spacer(Modifier.height(12.dp))
        AboutCard(stringResource(Res.string.language_title)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LangOption(
                    stringResource(Res.string.lang_system),
                    langTag.isEmpty(),
                    Modifier.weight(1f)
                ) { onLangChange("") }
                LangOption(
                    stringResource(Res.string.lang_zh),
                    langTag == "zh",
                    Modifier.weight(1f)
                ) { onLangChange("zh") }
                LangOption(
                    stringResource(Res.string.lang_en),
                    langTag == "en",
                    Modifier.weight(1f)
                ) { onLangChange("en") }
            }
        }

        Spacer(Modifier.height(12.dp))
        AboutCard(stringResource(Res.string.glass_title)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LangOption(
                    stringResource(Res.string.glass_frosted),
                    glassStyle == GlassStyle.Frosted,
                    Modifier.weight(1f)
                ) { onGlassStyle(GlassStyle.Frosted) }
                LangOption(
                    stringResource(Res.string.glass_liquid),
                    glassStyle == GlassStyle.Liquid,
                    Modifier.weight(1f)
                ) { onGlassStyle(GlassStyle.Liquid) }
            }
        }

        Spacer(Modifier.height(12.dp))
        AboutCard(stringResource(Res.string.about_features_title)) {
            Bullet(stringResource(Res.string.feat_layout), Lucide.LayoutGrid)
            Bullet(stringResource(Res.string.feat_destiny), Lucide.Compass)
            Bullet(stringResource(Res.string.feat_save), Lucide.Save)
        }

        Spacer(Modifier.height(12.dp))
        AboutCard(stringResource(Res.string.about_note_title)) {
            Text(
                stringResource(Res.string.about_note),
                color = Palette.TextMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun AboutCard(title: String, content: @Composable () -> Unit) {
    GlassCard(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            SectionTitle(title)
            content()
        }
    }
}

@Composable
private fun LangOption(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) Palette.Gold.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.04f))
            .border(
                1.dp,
                if (selected) Palette.Gold.copy(alpha = 0.55f) else Color.White.copy(alpha = 0.10f),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) Palette.GoldSoft else Palette.TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
private fun Bullet(text: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(15.dp),
            tint = Palette.Primary
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = Palette.TextSecondary, fontSize = 12.5.sp, lineHeight = 19.sp)
    }
}
