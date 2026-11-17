package dev.passchef.mobile.gate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.LocalCookbookVietnamese
import dev.passchef.mobile.cookbook.words
import dev.passchef.mobile.ui.*
import kotlinx.coroutines.delay

private const val SECRET = "Basic*"
private val EXAMPLES = listOf("Facebook" to "rand0m", "Gmail" to "x9Lk2m", "Bank" to "p3Zn7w")

/**
 * E1 (D18, option F): one paper strip `rand0mBasic*`. Tearing the secret part off leaves `rand0m$1`,
 * a written code that alone cannot log in. Auto-tears once after 1.2s; toggle + replay. Fake data.
 * Motion honours the system animator scale (0 = instant swap).
 */
@Composable fun TearExplainer() {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    var torn by remember { mutableStateOf(false) }
    var replay by remember { mutableStateOf(0) }
    LaunchedEffect(replay) { torn = false; delay(1200); torn = true }
    val progress by animateFloatAsState(if (torn) 1f else 0f, tween(700), label = "tear")
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(Modifier.testTag("onboarding-tear-toggle"), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CookbookChoice(t("Normal way", "Cách thường"), isSelected = !torn, onClick = { torn = false })
            CookbookChoice(t("Tear off secret", "Xé phần bí mật"), isSelected = torn, onClick = { torn = true })
        }
        // Paper strip: the code stays, the secret part flies off.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.background(PassChefPalette.Raised, MaterialTheme.shapes.small)
                .border(1.dp, PassChefPalette.StrongLine, MaterialTheme.shapes.small).padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text("rand0m", style = PassChefDish)
                AnimatedVisibility(torn, enter = expandHorizontally() + fadeIn(), exit = shrinkHorizontally() + fadeOut()) {
                    Text("\$1", style = PassChefDish, color = PassChefPalette.AccentInk)
                }
                if (progress < 0.5f) Text(SECRET, style = PassChefDish, color = PassChefPalette.Coral,
                    modifier = Modifier.graphicsLayer { alpha = 1f - progress * 2 })
            }
            Spacer(Modifier.width(12.dp))
            Text(SECRET, style = PassChefDish, color = PassChefPalette.Moss,
                modifier = Modifier.graphicsLayer {
                    alpha = progress; translationX = 40f * progress; translationY = 18f * progress; rotationZ = 7f * progress
                }.background(PassChefPalette.MossBackground, MaterialTheme.shapes.small).padding(horizontal = 10.dp, vertical = 8.dp))
        }
        Text(if (torn) t("Code stays on paper · secret part kept apart", "Mã nằm trên giấy · phần bí mật giữ riêng")
            else t("Whole password written on paper", "Cả mật khẩu viết trên giấy"),
            style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
        TearTable(torn, progress)
        val verdict = if (torn) t("This is only a code. Anyone who reads it still can't log in.", "Đây chỉ là mã. Ai đọc cũng không đăng nhập được.")
            else t("This is the full password. Anyone who reads it can log in.", "Đây là mật khẩu đầy đủ. Ai đọc cũng đăng nhập được.")
        Text(verdict, Modifier.fillMaxWidth().padding(top = 12.dp)
            .background(if (torn) PassChefPalette.MossBackground else PassChefPalette.CoralBackground, MaterialTheme.shapes.small)
            .padding(12.dp).semantics { liveRegion = LiveRegionMode.Polite },
            color = if (torn) PassChefPalette.Moss else PassChefPalette.Coral, style = MaterialTheme.typography.bodyMedium)
        Text(t("Tear the secret part off.", "Xé phần bí mật ra."), style = PassChefDisplay)
        Text(t("What's left is useless code.", "Phần còn lại là mã vô dụng."),
            style = PassChefDisplay.copy(fontStyle = FontStyle.Italic), color = PassChefPalette.AccentInk)
        CookbookGhostAction(t("↻ Play again", "↻ Xem lại"), onClick = { replay++ }, modifier = Modifier.testTag("onboarding-tear-replay"))
    }
}

@Composable private fun TearTable(torn: Boolean, progress: Float) {
    val vi = LocalCookbookVietnamese.current
    fun t(en: String, vn: String) = words(vi, en, vn)
    Column(Modifier.fillMaxWidth().border(1.dp, PassChefPalette.Hairline, MaterialTheme.shapes.small).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row {
            PassChefCaption(t("Full password", "Mật khẩu đầy đủ"), Modifier.weight(1.2f))
            if (torn) PassChefCaption("✂ " + t("Written code", "Mã đã ghi"), Modifier.weight(1f).graphicsLayer { alpha = progress })
        }
        for ((site, code) in EXAMPLES) Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1.2f)) {
                Text(site, style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
                Row {
                    Text(code, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = PassChefMonoFamily))
                    Text(SECRET, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = PassChefMonoFamily, fontWeight = FontWeight.SemiBold),
                        color = if (torn) PassChefPalette.Moss else PassChefPalette.Text)
                }
            }
            if (torn) Text("$code\$1", Modifier.weight(1f).graphicsLayer { alpha = progress },
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = PassChefMonoFamily), color = PassChefPalette.AccentInk)
        }
        Row {
            Text(if (torn) t("never written down", "không bao giờ ghi ra") else t("written on paper", "viết trên giấy"),
                Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall,
                color = if (torn) PassChefPalette.Moss else PassChefPalette.Coral)
            if (torn) Text(t("on paper", "trên giấy"), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = PassChefPalette.Muted)
        }
    }
}
