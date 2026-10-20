package zhiqiu.hs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 中性现代配色，主色蓝，吉凶用绿/红。 */
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

    // 兼容旧引用，统一映射到新中性色
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
        typography = Typography(),
        content = content
    )
}

/** 深色底 + 模糊色斑，供半透明卡片透出，形成玻璃质感（各端一致）。 */
@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier.fillMaxSize().background(Palette.Background)) {
        Blob(Color(0xFF2563EB), 320.dp, (-90).dp, (-60).dp)
        Blob(Color(0xFF7C3AED), 280.dp, 200.dp, 40.dp)
        Blob(Color(0xFF0EA5E9), 240.dp, 60.dp, 460.dp)
        content()
    }
}

@Composable
private fun Blob(color: Color, size: Dp, x: Dp, y: Dp) {
    Box(
        Modifier
            .offset(x = x, y = y)
            .size(size)
            .blur(110.dp)
            .background(color.copy(alpha = 0.35f), CircleShape)
    )
}

/** 半透明玻璃卡片：细描边 + 顶部高光，桌面端同样有质感。 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier
            .shadow(12.dp, shape, clip = false)
            .clip(shape)
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.09f), shape)
    ) {
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White.copy(alpha = 0.07f), Color.Transparent)
                    )
                )
        )
        content()
    }
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (enabled) Palette.Primary else Palette.SurfaceHigh)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (enabled) Color.White else Palette.TextMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun GhostButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Palette.TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun Pill(
    text: String,
    color: Color = Palette.TextSecondary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 9.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(6.dp).clip(CircleShape).background(Palette.Primary))
        Spacer(Modifier.width(8.dp))
        Text(text, color = Palette.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ScreenHeader(title: String, subtitle: String) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
    ) {
        Text(title, color = Palette.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Palette.TextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
    }
}
