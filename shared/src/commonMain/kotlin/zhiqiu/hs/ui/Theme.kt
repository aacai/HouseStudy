package zhiqiu.hs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kashif_e.backdrop.Backdrop
import com.kashif_e.backdrop.drawBackdrop
import com.kashif_e.backdrop.backdrops.layerBackdrop
import com.kashif_e.backdrop.backdrops.rememberLayerBackdrop
import com.kashif_e.backdrop.effects.blur

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

val LocalBackdrop = staticCompositionLocalOf<Backdrop?> { null }

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

@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val backdrop = rememberLayerBackdrop()
    Box(modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .background(Palette.Background)
        ) {
            BackdropBlob(Color(0xFF2563EB), 360.dp, (-100).dp, (-80).dp)
            BackdropBlob(Color(0xFF7C3AED), 320.dp, 220.dp, 20.dp)
            BackdropBlob(Color(0xFF0EA5E9), 280.dp, 20.dp, 520.dp)
        }
        CompositionLocalProvider(LocalBackdrop provides backdrop) {
            content()
        }
    }
}

@Composable
private fun BackdropBlob(color: Color, size: Dp, x: Dp, y: Dp) {
    Box(
        Modifier
            .offset(x = x, y = y)
            .size(size)
            .background(color.copy(alpha = 0.35f), CircleShape)
    )
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    content: @Composable BoxScope.() -> Unit
) {
    val backdrop = LocalBackdrop.current
    Box(
        if (backdrop != null) {
            modifier.drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = { blur(20.dp.toPx()) },
                onDrawSurface = {
                    drawRect(Color.White.copy(alpha = 0.05f))
                }
            )
        } else {
            modifier
                .clip(shape)
                .background(Color.White.copy(alpha = 0.06f))
                .border(1.dp, Color.White.copy(alpha = 0.09f), shape)
        }
    ) {
        content()
    }
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    tooltip: String? = null,
    onClick: () -> Unit
) {
    val visual: @Composable () -> Unit = {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (enabled) Palette.Primary else Palette.SurfaceHigh)
                .clickable(enabled = enabled, onClick = onClick)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (enabled) Color.White else Palette.TextMuted
                    )
                    Spacer(Modifier.width(6.dp))
                }
                Text(
                    text,
                    color = if (enabled) Color.White else Palette.TextMuted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
    if (tooltip != null) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = { PlainTooltip { Text(tooltip) } },
            state = rememberTooltipState(),
            modifier = modifier
        ) { visual() }
    } else {
        Box(modifier) { visual() }
    }
}

@Composable
fun GhostButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    tooltip: String? = null,
    onClick: () -> Unit
) {
    val visual: @Composable () -> Unit = {
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.03f))
                .clickable(onClick = onClick)
                .padding(vertical = 11.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Palette.TextSecondary
                    )
                    Spacer(Modifier.width(6.dp))
                }
                Text(text, color = Palette.TextSecondary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
    if (tooltip != null) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = { PlainTooltip { Text(tooltip) } },
            state = rememberTooltipState(),
            modifier = modifier
        ) { visual() }
    } else {
        Box(modifier) { visual() }
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
fun ScreenHeader(title: String, subtitle: String, icon: ImageVector? = null) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Box(
                Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Palette.Primary.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Palette.Primary
                )
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, color = Palette.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Palette.TextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
        }
    }
}
