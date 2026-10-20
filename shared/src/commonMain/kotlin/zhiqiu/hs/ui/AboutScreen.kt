package zhiqiu.hs.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AboutScreen() {
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
