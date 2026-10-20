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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zhiqiu.hs.ui.AboutScreen
import zhiqiu.hs.ui.AppBackground
import zhiqiu.hs.ui.DestinyScreen
import zhiqiu.hs.ui.HouseTheme
import zhiqiu.hs.ui.LayoutScreen
import zhiqiu.hs.ui.Palette

@Composable
fun App() {
    HouseTheme {
        var tab by remember { mutableIntStateOf(0) }
        AppBackground {
            Column(Modifier.fillMaxSize()) {
                TopBar()
                Box(Modifier.weight(1f).fillMaxWidth()) {
                    when (tab) {
                        0 -> LayoutScreen()
                        1 -> DestinyScreen()
                        else -> AboutScreen()
                    }
                }
                BottomBar(tab) { tab = it }
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
        Text("HouseStudy", color = Palette.TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("八宅风水 · 户型与命理", color = Palette.TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun BottomBar(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Palette.Surface.copy(alpha = 0.9f))
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem("布局", selected == 0) { onSelect(0) }
        NavItem("命理", selected == 1) { onSelect(1) }
        NavItem("关于", selected == 2) { onSelect(2) }
    }
}

@Composable
private fun NavItem(label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(if (selected) Palette.Primary else Color.Transparent)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            label,
            color = if (selected) Palette.TextPrimary else Palette.TextMuted,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
    }
}
