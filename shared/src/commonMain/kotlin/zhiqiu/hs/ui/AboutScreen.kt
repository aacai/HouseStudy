package zhiqiu.hs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen() {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 18.dp)
    ) {
        ScreenHeader("关于", "HouseStudy · 八宅风水")
        Spacer(Modifier.height(14.dp))

        AboutCard("应用简介") {
            Text(
                "以八宅风水（东西四命）为基础，帮助你在九宫格中规划家庭成员与房间的方位布局，并依据出生年份推算个人命卦与吉凶方位。",
                color = Palette.TextSecondary, fontSize = 12.5.sp, lineHeight = 19.sp
            )
        }

        Spacer(Modifier.height(12.dp))
        AboutCard("功能") {
            Bullet("布局：在九宫格中放置成员或房间，逐宫查看方位解读。")
            Bullet("命理：按出生年份与性别推算命卦、四吉四凶方位。")
            Bullet("存档：保存多套方案，随时读取对比。")
        }

        Spacer(Modifier.height(12.dp))
        AboutCard("说明") {
            Text(
                "风水属传统民俗文化，本应用仅供学习与参考，不构成任何决策建议。",
                color = Palette.TextMuted, fontSize = 12.sp, lineHeight = 18.sp
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
private fun Bullet(text: String) {
    Text("· $text", color = Palette.TextSecondary, fontSize = 12.5.sp, lineHeight = 19.sp, fontWeight = FontWeight.Normal)
}
