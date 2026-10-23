package zhiqiu.hs.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.colorControls
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight

enum class GlassStyle {
    Frosted,
    Liquid;

    fun stored(): String = if (this == Liquid) "liquid" else "frosted"

    companion object {
        fun fromStored(value: String): GlassStyle =
            if (value == "liquid") Liquid else Frosted
    }
}

/** 卡片等普通玻璃采样源（装饰背景） */
val LocalBackdrop = staticCompositionLocalOf<Backdrop?> { null }

/**
 * 底栏采样源：装饰背景 + 页面内容。
 * 底栏必须画在 scene 采样层之外，否则会采样到自己并可能崩溃。
 */
val LocalBarBackdrop = staticCompositionLocalOf<Backdrop?> { null }

val LocalGlassStyle = staticCompositionLocalOf { GlassStyle.Frosted }

/**
 * @param content 页面主体，会进入 scene 采样（供底栏透视）
 * @param overlay 浮层（底栏），不进入 scene，用 [LocalBarBackdrop] 透视背后页面
 */
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val ambient = rememberLayerBackdrop {
        drawRect(Palette.Background)
        drawContent()
    }
    val scene = rememberLayerBackdrop()
    val barBackdrop = rememberCombinedBackdrop(ambient, scene)

    Box(modifier.fillMaxSize()) {
        // 整页墙纸：铺满窗口的弥散光（给玻璃采样，也是肉眼看到的 App 背景）
        MeshGlowWallpaper(
            Modifier
                .fillMaxSize()
                .layerBackdrop(ambient)
        )

        CompositionLocalProvider(
            LocalBackdrop provides ambient,
            LocalBarBackdrop provides barBackdrop
        ) {
            // 页面内容写入 scene，供底栏采样（不要把 overlay 放进来）
            Box(
                Modifier
                    .fillMaxSize()
                    .layerBackdrop(scene)
            ) {
                content()
            }
            overlay()
        }
    }
}

/** 全屏弥散光网格：按窗口比例铺光，不是角落里的硬色圆 */
@Composable
private fun MeshGlowWallpaper(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        drawRect(Palette.Background)

        fun glow(color: Color, cx: Float, cy: Float, radius: Float, peak: Float) {
            val center = Offset(cx, cy)
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to color.copy(alpha = peak),
                        0.35f to color.copy(alpha = peak * 0.45f),
                        0.7f to color.copy(alpha = peak * 0.12f),
                        1f to Color.Transparent
                    ),
                    center = center,
                    radius = radius
                ),
                radius = radius,
                center = center
            )
        }

        // 左上蓝、右上紫、右中青、底部靛——互相叠成整页氛围光
        glow(Color(0xFF3B82F6), w * 0.08f, h * 0.05f, w * 0.72f, 0.55f)
        glow(Color(0xFF8B5CF6), w * 0.92f, h * 0.08f, w * 0.68f, 0.48f)
        glow(Color(0xFF06B6D4), w * 0.85f, h * 0.55f, w * 0.58f, 0.36f)
        glow(Color(0xFF6366F1), w * 0.35f, h * 0.95f, w * 0.75f, 0.40f)
        glow(Color(0xFF4C8DFF), w * 0.55f, h * 0.35f, w * 0.45f, 0.18f)
    }
}

/**
 * @param forBar 使用 [LocalBarBackdrop]，透视页面内容；胶囊用更强 lens
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    forBar: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val backdrop = if (forBar) LocalBarBackdrop.current else LocalBackdrop.current
    val style = LocalGlassStyle.current
    Box(
        when {
            backdrop != null && style == GlassStyle.Liquid -> {
                modifier.drawBackdrop(
                    backdrop = backdrop,
                    shape = { shape },
                    effects = {
                        vibrancy()
                        if (forBar) {
                            // 官方 Glass Bottom Bar / tab 配方，表面更透才能看见折射
                            blur(2.dp.toPx())
                            lens(16.dp.toPx(), 32.dp.toPx())
                        } else {
                            blur(4.dp.toPx())
                            lens(
                                refractionHeight = 16.dp.toPx(),
                                refractionAmount = 32.dp.toPx(),
                                depthEffect = true
                            )
                        }
                    },
                    highlight = { Highlight.Default },
                    onDrawSurface = {
                        drawRect(Color.White.copy(alpha = if (forBar) 0.06f else 0.10f))
                    }
                )
            }
            backdrop != null -> {
                // 磨砂：提亮 + 提饱和，模糊别太重，否则在弥散光背景上会发灰发闷
                modifier
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { shape },
                        effects = {
                            vibrancy()
                            colorControls(brightness = 0.04f, contrast = 1.02f, saturation = 1.18f)
                            blur(if (forBar) 10.dp.toPx() else 14.dp.toPx())
                        },
                        highlight = { Highlight.Plain },
                        onDrawSurface = {
                            drawRect(Color.White.copy(alpha = if (forBar) 0.07f else 0.06f))
                        }
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.07f), shape)
            }
            else -> {
                modifier
                    .clip(shape)
                    .background(Color.White.copy(alpha = 0.06f))
                    .border(1.dp, Color.White.copy(alpha = 0.09f), shape)
            }
        }
    ) {
        content()
    }
}
