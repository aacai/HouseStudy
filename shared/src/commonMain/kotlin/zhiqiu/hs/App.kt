package zhiqiu.hs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.jetbrains.compose.resources.stringResource
import zhiqiu.hs.generated.resources.*
import zhiqiu.hs.i18n.AppLang
import zhiqiu.hs.i18n.AppLanguageProvider
import zhiqiu.hs.storage.AppSettings
import zhiqiu.hs.ui.AboutScreen
import zhiqiu.hs.ui.AppBackground
import zhiqiu.hs.ui.DestinyScreen
import zhiqiu.hs.ui.GlassCard
import zhiqiu.hs.ui.HouseTheme
import zhiqiu.hs.ui.LayoutScreen
import zhiqiu.hs.ui.Palette

@Composable
fun App() {
    var langTag by remember { mutableStateOf(AppSettings.language()) }
    AppLanguageProvider(AppLang.fromTag(langTag)) {
        HouseTheme {
            var tab by remember { mutableIntStateOf(0) }
            AppBackground {
                Column(Modifier.fillMaxSize()) {
                    TopBar()
                    Box(Modifier.weight(1f).fillMaxWidth()) {
                        when (tab) {
                            0 -> LayoutScreen()
                            1 -> DestinyScreen()
                            else -> AboutScreen(langTag) { tag ->
                                langTag = tag
                                AppSettings.setLanguage(tag)
                            }
                        }
                    }
                    BottomBar(tab) { tab = it }
                }
            }
        }
    }
}

@Composable
private fun TopBar() {
    Column(
        Modifier
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            stringResource(Res.string.app_name),
            color = Palette.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            stringResource(Res.string.app_tagline),
            color = Palette.TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun BottomBar(selected: Int, onSelect: (Int) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
            )
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    stringResource(Res.string.tab_layout),
                    Lucide.LayoutGrid,
                    stringResource(Res.string.tip_layout),
                    selected == 0
                ) { onSelect(0) }
                NavItem(
                    stringResource(Res.string.tab_destiny),
                    Lucide.Compass,
                    stringResource(Res.string.tip_destiny),
                    selected == 1
                ) { onSelect(1) }
                NavItem(
                    stringResource(Res.string.tab_about),
                    Lucide.Info,
                    stringResource(Res.string.tip_about),
                    selected == 2
                ) { onSelect(2) }
            }
        }
    }
}

@Composable
private fun NavItem(label: String, icon: ImageVector, tip: String, selected: Boolean, onClick: () -> Unit) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = { PlainTooltip { Text(tip) } },
        state = rememberTooltipState()
    ) {
        Column(
            Modifier
                .clip(RoundedCornerShape(50))
                .background(if (selected) Color.White.copy(alpha = 0.14f) else Color.Transparent)
                .clickable(onClick = onClick)
                .padding(horizontal = 26.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (selected) Palette.TextPrimary else Palette.TextMuted
            )
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                color = if (selected) Palette.TextPrimary else Palette.TextMuted,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
