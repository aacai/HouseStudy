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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Calculator
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.Lucide
import org.jetbrains.compose.resources.stringResource
import zhiqiu.hs.core.FORTUNE_MAP
import zhiqiu.hs.core.Gender
import zhiqiu.hs.core.MingGuaResult
import zhiqiu.hs.core.calculateMingGua
import zhiqiu.hs.core.trigramByKey
import zhiqiu.hs.generated.resources.*
import zhiqiu.hs.i18n.directionLabel
import zhiqiu.hs.i18n.elementLabel
import zhiqiu.hs.i18n.genderLabel
import zhiqiu.hs.i18n.groupLabel
import zhiqiu.hs.i18n.starLabel
import zhiqiu.hs.i18n.trigramLabel

@Composable
fun DestinyScreen() {
    var year by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.男) }
    var result by remember { mutableStateOf<MingGuaResult?>(null) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 18.dp)
    ) {
        ScreenHeader(
            stringResource(Res.string.destiny_title),
            stringResource(Res.string.destiny_subtitle),
            icon = Lucide.Compass
        )
        Spacer(Modifier.height(14.dp))

        GlassCard(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionTitle(stringResource(Res.string.basic_info))
                InputField(year, { year = it.filter { c -> c.isDigit() }.take(4) }, stringResource(Res.string.birth_year_placeholder))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Gender.entries.forEach { g ->
                        GenderOption(genderLabel(g), gender == g, Modifier.weight(1f)) { gender = g }
                    }
                }
                PrimaryButton(
                    stringResource(Res.string.btn_calculate),
                    Modifier.fillMaxWidth(),
                    icon = Lucide.Calculator,
                    tooltip = stringResource(Res.string.tip_calculate)
                ) {
                    year.toIntOrNull()?.takeIf { it in 1900..2100 }?.let {
                        result = calculateMingGua(it, gender)
                    }
                }
            }
        }

        result?.let { r ->
            Spacer(Modifier.height(14.dp))
            MingGuaCard(r)
            Spacer(Modifier.height(14.dp))
            FortuneCard(r)
        }
    }
}

@Composable
private fun MingGuaCard(r: MingGuaResult) {
    GlassCard(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionTitle(stringResource(Res.string.ming_gua_title))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    trigramLabel(r.trigram.key) + stringResource(Res.string.trigram_suffix),
                    color = Palette.TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        stringResource(
                            Res.string.element_template,
                            groupLabel(r.group),
                            elementLabel(r.trigram.element)
                        ),
                        color = Palette.TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(Res.string.post_heaven, r.number),
                        color = Palette.TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
            Text(
                stringResource(Res.string.ming_gua_hint),
                color = Palette.TextSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FortuneCard(r: MingGuaResult) {
    val map = FORTUNE_MAP[r.trigram.key].orEmpty()
    val items = map.map { (dirKey, star) -> (trigramByKey(dirKey)?.direction ?: dirKey) to star }
    val good = items.filter { it.second.auspicious }
    val bad = items.filterNot { it.second.auspicious }

    GlassCard(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionTitle(stringResource(Res.string.fortune_title))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        stringResource(Res.string.four_good),
                        color = Palette.Jade,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    good.forEach { (dir, star) -> FortuneRow(directionLabel(dir), starLabel(star), Palette.Jade) }
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        stringResource(Res.string.four_bad),
                        color = Palette.Crimson,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    bad.forEach { (dir, star) -> FortuneRow(directionLabel(dir), starLabel(star), Palette.Crimson) }
                }
            }
        }
    }
}

@Composable
private fun FortuneRow(direction: String, star: String, color: Color) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            star,
            color = color,
            fontSize = 13.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Text(direction, color = Palette.TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun GenderOption(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
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
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) Palette.GoldSoft else Palette.TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
    }
}
