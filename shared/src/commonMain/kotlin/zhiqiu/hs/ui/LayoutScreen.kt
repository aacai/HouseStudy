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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.composables.icons.lucide.FileSearch
import com.composables.icons.lucide.FolderOpen
import com.composables.icons.lucide.LayoutGrid
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Save
import org.jetbrains.compose.resources.stringResource
import zhiqiu.hs.core.BAGUA_GRID
import zhiqiu.hs.core.HouseData
import zhiqiu.hs.core.HouseDataLoader
import zhiqiu.hs.core.MEMBERS
import zhiqiu.hs.core.interpretationFor
import zhiqiu.hs.core.trigramByKey
import zhiqiu.hs.generated.resources.*
import zhiqiu.hs.i18n.directionLabel
import zhiqiu.hs.i18n.memberLabel
import zhiqiu.hs.storage.HouseRepository
import zhiqiu.hs.storage.LayoutMap

private val Repeatable = setOf("厕", "厨", "厅")

@Composable
fun LayoutScreen() {
    var layout by remember { mutableStateOf<LayoutMap>(emptyMap()) }
    var archives by remember { mutableStateOf<Set<String>>(emptySet()) }
    var data by remember { mutableStateOf<HouseData?>(null) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var showSave by remember { mutableStateOf(false) }
    var showArchive by remember { mutableStateOf(false) }
    var showAnalysis by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        HouseRepository.loadDefault()?.let { layout = it }
        archives = HouseRepository.listArchives()
        data = runCatching { HouseDataLoader.load() }.getOrNull()
    }
    LaunchedEffect(layout) { HouseRepository.saveDefault(layout) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 18.dp)
    ) {
        ScreenHeader(
            stringResource(Res.string.layout_title),
            stringResource(Res.string.layout_subtitle),
            icon = Lucide.LayoutGrid
        )
        Spacer(Modifier.height(14.dp))

        BaguaGrid(layout) { selectedIndex = it }

        Spacer(Modifier.height(18.dp))
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PrimaryButton(
                stringResource(Res.string.btn_view_analysis),
                Modifier.weight(1f),
                icon = Lucide.FileSearch,
                tooltip = stringResource(Res.string.tip_view_analysis)
            ) { showAnalysis = true }
            GhostButton(
                stringResource(Res.string.btn_save),
                Modifier.weight(1f),
                icon = Lucide.Save,
                tooltip = stringResource(Res.string.tip_save)
            ) { showSave = true }
            GhostButton(
                stringResource(Res.string.btn_load),
                Modifier.weight(1f),
                icon = Lucide.FolderOpen,
                tooltip = stringResource(Res.string.tip_load)
            ) {
                archives = HouseRepository.listArchives()
                showArchive = true
            }
        }
    }

    selectedIndex?.let { idx ->
        MemberPickerDialog(
            current = layout[idx] ?: emptyList(),
            onDismiss = { selectedIndex = null },
            onConfirm = { members ->
                layout = applySelection(layout, idx, members)
                selectedIndex = null
            }
        )
    }

    if (showSave) {
        SaveDialog(onDismiss = { showSave = false }) { name ->
            HouseRepository.saveNamed(name, layout)
            archives = HouseRepository.listArchives()
            showSave = false
        }
    }

    if (showArchive) {
        ArchiveDialog(
            archives = archives.toList(),
            onDismiss = { showArchive = false },
            onLoad = { name ->
                HouseRepository.loadNamed(name)?.let { layout = it }
                showArchive = false
            },
            onDelete = { name ->
                HouseRepository.deleteNamed(name)
                archives = HouseRepository.listArchives()
            }
        )
    }

    if (showAnalysis) {
        AnalysisDialog(layout, data, onDismiss = { showAnalysis = false })
    }
}

private fun applySelection(layout: LayoutMap, idx: Int, members: List<String>): LayoutMap {
    val unique = members.filter { it !in Repeatable }
    val cleaned = layout.mapValues { (i, list) ->
        if (i == idx) list else list.filter { it !in unique }
    }
    return if (members.isEmpty()) cleaned - idx else cleaned + (idx to members)
}

@Composable
private fun BaguaGrid(layout: LayoutMap, onClick: (Int) -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in 0 until 3) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (col in 0 until 3) {
                    val idx = row * 3 + col
                    val (key, dir) = BAGUA_GRID[idx]
                    BaguaCell(
                        key = key,
                        dir = dir,
                        members = layout[idx] ?: emptyList(),
                        modifier = Modifier.weight(1f)
                    ) { onClick(idx) }
                }
            }
        }
    }
}

@Composable
private fun BaguaCell(
    key: String,
    dir: String,
    members: List<String>,
    modifier: Modifier,
    onClick: () -> Unit
) {
    GlassCard(modifier.height(100.dp).clickable(onClick = onClick), shape = RoundedCornerShape(16.dp)) {
        Column(
            Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                directionLabel(dir),
                color = Palette.TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            if (members.isEmpty()) {
                Text(
                    "+",
                    color = Palette.TextMuted.copy(alpha = 0.7f),
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    members.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            row.forEach { m -> Pill(memberLabel(m)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberPickerDialog(
    current: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit
) {
    var selected by remember { mutableStateOf(current.toSet()) }
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(Modifier.fillMaxWidth().padding(20.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionTitle(stringResource(Res.string.select_member_title))
                MEMBERS.chunked(3).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { (display, short) ->
                            SelectChip(
                                text = memberLabel(display),
                                selected = selected.contains(short),
                                modifier = Modifier.weight(1f)
                            ) {
                                selected = if (selected.contains(short)) selected - short else selected + short
                            }
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GhostButton(stringResource(Res.string.action_clear), Modifier.weight(1f)) { selected = emptySet() }
                    PrimaryButton(stringResource(Res.string.action_confirm), Modifier.weight(1f)) { onConfirm(selected.toList()) }
                }
            }
        }
    }
}

@Composable
private fun SelectChip(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
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
private fun AnalysisDialog(layout: LayoutMap, data: HouseData?, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(Modifier.fillMaxWidth().padding(16.dp)) {
            Column(
                Modifier
                    .padding(18.dp)
                    .heightIn(max = 540.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SectionTitle(stringResource(Res.string.analysis_title))
                if (layout.isEmpty()) {
                    Text(
                        stringResource(Res.string.empty_layout),
                        color = Palette.TextSecondary,
                        fontSize = 12.sp
                    )
                } else {
                    BAGUA_GRID.forEachIndexed { idx, pair ->
                        val key = pair.first
                        val dir = pair.second
                        val members = layout[idx] ?: return@forEachIndexed
                        if (members.isEmpty()) return@forEachIndexed
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            val names = members.map { m ->
                                memberLabel(MEMBERS.firstOrNull { it.second == m }?.first ?: m)
                            }
                            Text(
                                "${directionLabel(dir)} · ${names.joinToString("、")}",
                                color = Palette.GoldSoft,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            members.forEach { m ->
                                val txt = data?.interpretationFor(m, key) ?: stringResource(Res.string.no_interpretation)
                                Text(txt, color = Palette.TextSecondary, fontSize = 12.sp, lineHeight = 18.sp)
                            }
                        }
                    }
                }
                GhostButton(stringResource(Res.string.action_close), Modifier.fillMaxWidth()) { onDismiss() }
            }
        }
    }
}

@Composable
private fun SaveDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(Modifier.fillMaxWidth().padding(20.dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionTitle(stringResource(Res.string.save_title))
                InputField(name, { name = it }, stringResource(Res.string.save_name_placeholder))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GhostButton(stringResource(Res.string.action_cancel), Modifier.weight(1f)) { onDismiss() }
                    PrimaryButton(
                        stringResource(Res.string.action_save),
                        Modifier.weight(1f),
                        enabled = name.isNotBlank()
                    ) { onSave(name.trim()) }
                }
            }
        }
    }
}

@Composable
private fun ArchiveDialog(
    archives: List<String>,
    onDismiss: () -> Unit,
    onLoad: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(Modifier.fillMaxWidth().padding(20.dp)) {
            Column(
                Modifier.padding(20.dp).heightIn(max = 480.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SectionTitle(stringResource(Res.string.archive_title))
                if (archives.isEmpty()) {
                    Text(
                        stringResource(Res.string.empty_archive),
                        color = Palette.TextSecondary,
                        fontSize = 12.sp
                    )
                } else {
                    archives.forEach { name ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(name, color = Palette.TextPrimary, fontSize = 13.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GhostButton(stringResource(Res.string.action_load)) { onLoad(name) }
                                GhostButton(stringResource(Res.string.action_delete)) { onDelete(name) }
                            }
                        }
                    }
                }
                GhostButton(stringResource(Res.string.action_close), Modifier.fillMaxWidth()) { onDismiss() }
            }
        }
    }
}

@Composable
internal fun InputField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        if (value.isEmpty()) {
            Text(placeholder, color = Palette.TextMuted, fontSize = 13.sp)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Palette.TextPrimary, fontSize = 13.sp),
            cursorBrush = SolidColor(Palette.Gold),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
