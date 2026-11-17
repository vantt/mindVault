package dev.passchef.mobile.connect

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.passchef.mobile.cookbook.*
import dev.passchef.mobile.gate.StepDots
import dev.passchef.mobile.navigation.*
import dev.passchef.mobile.ui.*

/**
 * Connect wizard (D1 Picker): CM1 choose → CM2 tab → CM3 columns → CM4 review → saved.
 * Progress (ids, tab title, column choices) lives in [AppRouteState] and survives lock (D5);
 * sheet rows/token live in CookbookSources and are wiped on lock, so "Continue" re-authorizes silently.
 */
@Composable fun ConnectWizardScreen(cookbook: CookbookController, routes: AppRouteState) {
    val vi = LocalCookbookVietnamese.current
    val sources = cookbook.sources
    val progress = routes.connect
    val draft = sources.draft
    val metadata = sources.metadata
    val step = when {
        draft != null && progress.step == ConnectStep.REVIEW -> ConnectStep.REVIEW
        draft != null -> ConnectStep.MAP
        metadata != null -> ConnectStep.TAB
        else -> ConnectStep.CHOOSE
    }
    LaunchedEffect(metadata?.spreadsheetId, draft?.origin?.key) {
        val progress = routes.connect
        if (metadata != null) routes.connect = progress.copy(step = if (draft != null) maxOf(progress.step, ConnectStep.MAP) else ConnectStep.TAB,
            fileId = metadata.spreadsheetId, tabTitle = draft?.origin?.title ?: progress.tabTitle)
        else if (draft != null) routes.connect = progress.copy(step = maxOf(progress.step, ConnectStep.MAP), tabTitle = draft.origin.title)
    }
    // Saved (or an already-known sheet refreshed straight away): leave the wizard, show its passwords.
    LaunchedEffect(sources.status, sources.busy) {
        val progress = routes.connect
        if (sources.status == "SYNCED" && !sources.busy && (progress.fileId != null || progress.step >= ConnectStep.MAP)) {
            val key = sources.snapshots.lastOrNull()?.mapping?.origin?.key
            routes.resetConnect(); routes.sharedSheetId = null
            routes.removeWhere { it == AppRoute.Connect }
            // Land on the new sheet's list, not on an older detail kept in the Passwords stack (D5).
            routes.select(AppTab.PASSWORDS); if (routes.route != null) routes.select(AppTab.PASSWORDS)
            routes.listScope = key
        }
    }
    BackHandler(enabled = step != ConnectStep.CHOOSE) {
        when (step) {
            ConnectStep.REVIEW -> routes.connect = progress.copy(step = ConnectStep.MAP)
            else -> { sources.invalidate(); routes.resetConnect() }
        }
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                if (step == ConnectStep.CHOOSE) { routes.resetConnect(); routes.pop() }
                else if (step == ConnectStep.REVIEW) routes.connect = progress.copy(step = ConnectStep.MAP)
                else { sources.invalidate(); routes.resetConnect() }
            }) { Icon(PassChefIcons.Back, contentDescription = words(vi, "Back", "Quay lại"), tint = PassChefPalette.Text) }
            Spacer(Modifier.weight(1f))
            StepDots(step.ordinal, 4)
        }
        when (step) {
            ConnectStep.CHOOSE -> ConnectChooser(cookbook, routes)
            ConnectStep.TAB -> metadata?.let { ChooseTab(sources, it) }
            ConnectStep.MAP -> draft?.let { d -> key(d) {
                // Column choices belong to one sheet/tab; a different draft starts from fresh suggestions.
                MappingScreen(sources, d, progress.mapping?.takeIf { it.originKey == d.origin.key }) { picked ->
                    val choices = picked.copy(originKey = d.origin.key)
                    if (mappingPreview(d, choices) == null) { sources.markInvalidMapping(); routes.connect = progress.copy(mapping = choices) }
                    else routes.connect = progress.copy(step = ConnectStep.REVIEW, mapping = choices)
                }
            } }
            ConnectStep.REVIEW -> draft?.let { d -> progress.mapping?.let { ReviewMapping(sources, d, it) } }
        }
    }
}

/** CM2: which tab holds the codes; row counts shown; a single tab is still confirmed by the user. */
@Composable private fun ChooseTab(sources: CookbookSources, document: com.passchef.sheets.SpreadsheetMetadata) {
    val vi = LocalCookbookVietnamese.current
    Text(document.properties.title, style = MaterialTheme.typography.titleLarge)
    Text(words(vi, "Which tab holds your written codes?", "Tab nào chứa các mã đã ghi?"), color = PassChefPalette.Secondary)
    if (sources.busy || isProblem(sources.status)) CookbookStatus(cookbookMessage(sources.status, vi),
        tone = if (isProblem(sources.status)) CookbookTone.Error else CookbookTone.Warning)
    document.sheets.forEachIndexed { index, tab ->
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            CookbookSecondaryAction(tab.properties.title, onClick = { sources.choose(tab.properties) }, enabled = !sources.busy,
                modifier = Modifier.weight(1f).testTag("tab-$index"))
            Spacer(Modifier.width(12.dp))
            PassChefCaption(words(vi, "${tab.properties.gridProperties.rowCount} rows", "${tab.properties.gridProperties.rowCount} hàng"))
        }
    }
}
