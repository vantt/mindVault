package dev.passchef.mobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.passchef.mobile.R

/** "PassChef" wordmark, no dot; "Chef" in accent-ink (D16 / spec v2). */
@Composable
fun PassChefWordmark(size: Int = 24) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.passchef_mark), contentDescription = null, modifier = Modifier.size((size + 4).dp))
        Spacer(Modifier.width(10.dp))
        Text("Pass", style = PassChefDisplay.copy(fontSize = size.sp), color = PassChefPalette.Text)
        Text("Chef", style = PassChefDisplay.copy(fontSize = size.sp), color = PassChefPalette.AccentInk)
    }
}

/** Gate header (setup / unlock): wordmark + optional language switch. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PassChefHeader(language: String?, onLanguage: () -> Unit) {
    Column {
        FlowRow(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically) {
            PassChefWordmark()
            // Material 3 keeps a 48dp touch target around the 40dp visual (proofs tap the expanded edge).
            if (language != null) TextButton(onClick = onLanguage, shape = RectangleShape) { Text(language, color = PassChefPalette.AccentInk) }
        }
        HorizontalDivider(color = PassChefPalette.Hairline)
    }
}

/** Section titles are body type so callers can safely pass dynamic sheet labels. */
@Composable
fun CookbookSection(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier.fillMaxWidth().background(PassChefPalette.Surface, MaterialTheme.shapes.medium)
        .border(1.dp, PassChefPalette.Hairline, MaterialTheme.shapes.medium).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        content()
    }
}

/** Mono uppercase caption for section labels and sheet paths (spec §6 item 5). */
@Composable
fun PassChefCaption(text: String, modifier: Modifier = Modifier, color: androidx.compose.ui.graphics.Color = PassChefPalette.Muted) {
    Text(text.uppercase(), modifier, color = color,
        style = MaterialTheme.typography.labelMedium.copy(fontFamily = PassChefMonoFamily, letterSpacing = 1.sp))
}

enum class CookbookTone { Neutral, Ready, Warning, Error }

@Composable
fun CookbookStatus(message: String, modifier: Modifier = Modifier, tone: CookbookTone = CookbookTone.Neutral) {
    if (message.isEmpty()) return
    val (foreground, background) = when (tone) {
        CookbookTone.Neutral -> PassChefPalette.Secondary to PassChefPalette.Raised
        CookbookTone.Ready -> PassChefPalette.Moss to PassChefPalette.MossBackground
        CookbookTone.Warning -> PassChefPalette.Honey to PassChefPalette.AmberBackground
        CookbookTone.Error -> PassChefPalette.Coral to PassChefPalette.CoralBackground
    }
    Text(message, modifier.fillMaxWidth().background(background, MaterialTheme.shapes.small).padding(12.dp),
        color = foreground, style = MaterialTheme.typography.bodyMedium)
}

/** The one amber action per screen. Light theme adds the 1px accent-ink edge for 3:1 contrast. */
@Composable
fun CookbookPrimaryAction(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(6.dp),
        border = if (PassChefPalette.Dark || !enabled) null else BorderStroke(1.dp, PassChefPalette.AccentInk),
        colors = ButtonDefaults.buttonColors(containerColor = PassChefPalette.Amber, contentColor = PassChefPalette.OnAmber),
        modifier = modifier.heightIn(min = 52.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold)
        // Decorative arrow: hidden from TalkBack so the button reads as its label only.
        Text("  ▸", fontWeight = FontWeight.SemiBold, modifier = Modifier.clearAndSetSemantics { })
    }
}

@Composable
fun CookbookSecondaryAction(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    OutlinedButton(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, PassChefPalette.Outline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = PassChefPalette.Text),
        modifier = modifier.heightIn(min = 48.dp)) { Text(label) }
}

@Composable
fun CookbookGhostAction(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    TextButton(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = PassChefPalette.AccentInk),
        modifier = modifier.heightIn(min = 48.dp)) { Text(label) }
}

/** Neutral selected state (text colour), never amber (palette D). */
@Composable
fun CookbookChoice(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    OutlinedButton(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, if (isSelected) PassChefPalette.Text else PassChefPalette.Outline),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) PassChefPalette.Raised else PassChefPalette.Canvas,
            contentColor = if (isSelected) PassChefPalette.Text else PassChefPalette.Secondary),
        modifier = modifier.heightIn(min = 48.dp).semantics { selected = isSelected }) {
        // Check mark is its own Text so the label stays stable for TalkBack/tests; selection is also in semantics.
        if (isSelected) Text("✓ ", color = PassChefPalette.Text)
        Text(label)
    }
}
