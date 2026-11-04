package dev.passchef.mobile.proof

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.passchef.mobile.cookbook.ColumnRole
import dev.passchef.mobile.cookbook.MenuEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Actual offline UI acceptance on isolated synthetic storage; no physical/Google/speech claim. */
@RunWith(AndroidJUnit4::class)
class MenuProposalLimitsUiTest {
    @get:Rule val ui = createEmptyComposeRule()
    private fun proof(action: (MenuProposalUiFixture) -> Unit) {
        val fixture = MenuProposalUiFixture(ui)
        var primary: Throwable? = null
        try { fixture.open(); action(fixture) }
        catch (failure: Throwable) { primary = failure; throw failure }
        finally { fixture.close(primary) }
    }

    @Test fun tiedAndSparseProposalsRefuseMissingAndDuplicateRoles() = proof { fixture ->
        val tied = fixture.preview("a#1\tb#1\nc#1\td#1\ne#1\tf#1")
        fixture.proposal(tied, "")
        fixture.confirm(); fixture.preserved(); fixture.status("INVALID_MAPPING")
        fixture.firstRow("1"); fixture.role(ColumnRole.RECIPE, "1"); fixture.role(ColumnRole.LABEL, "1")
        fixture.confirm(); fixture.preserved(); fixture.status("INVALID_MAPPING")
        fixture.report("tied-missing-duplicate-refused")
        val sparse = fixture.preview("a#1\nb#1")
        fixture.proposal(sparse, "")
        fixture.confirm(); fixture.preserved(); fixture.status("INVALID_MAPPING")
        fixture.report("sparse-no-autocache-passed")
    }

    @Test fun uniqueProposalRequiresExplicitConfirmationAndPreservesLiteralCells() = proof { fixture ->
        val header = listOf("Label", "Recipe", "Username")
        val origin = fixture.preview("Label\tRecipe\tUsername\nMail-one\talpha#1\t00123\nMail-two\tbeta@1\tĐầuBếp\nMail-three\tgamma#1\t00007")
        fixture.proposal(origin, "2")
        assertEquals("Header proposes the label role", "1", fixture.editable("column-LABEL"))
        assertEquals("Header proposes the username role", "3", fixture.editable("column-USERNAME"))
        assertEquals("URL remains explicitly ignored", "", fixture.editable("column-URL"))
        assertEquals("Notes remain explicitly ignored", "", fixture.editable("column-NOTES"))
        fixture.firstRow("2"); fixture.confirm()
        fixture.accept(origin, mapOf(ColumnRole.RECIPE to 1, ColumnRole.LABEL to 0, ColumnRole.USERNAME to 2), 2, header,
            listOf(MenuEntry(origin, 2, "alpha#1", "Mail-one", "00123"),
                MenuEntry(origin, 3, "beta@1", "Mail-two", "ĐầuBếp"),
                MenuEntry(origin, 4, "gamma#1", "Mail-three", "00007")))
        fixture.report("unique-confirmed-literal-cache-passed")
    }

    @Test fun tableLimitsAndOversizedEditPreserveLastGoodCookbook() = proof { fixture ->
        val tooManyRows = List(1001) { "abcd#1" }.joinToString("\n")
        val tooManyColumns = List(33) { "abcd#1" }.joinToString("\t")
        for (table in listOf(tooManyRows, tooManyColumns, "x".repeat(4097))) {
            fixture.preview(table); fixture.preserved(); fixture.status("INVALID_TABLE")
            fixture.report("table-limit-refusal-preserved")
        }
        val origin = fixture.preview("abcd#1")
        fixture.proposal(origin, "")
        val acceptedText = fixture.editable("source-table")
        fixture.field("source-table", "x".repeat(120001))
        assertEquals("Oversized UI edit leaves the accepted field exact", acceptedText, fixture.editable("source-table"))
        fixture.preserved()
        fixture.repreview()
        fixture.proposal(origin, "")
        fixture.report("oversized-ui-edit-refused")
    }

    @Test fun twentySourcesRefuseValidTwentyFirstMappingWithoutDataLoss() = proof { fixture ->
        repeat(19) { fixture.singleColumn(fixture.preview("abcd#1")) }
        assertEquals("Twenty real UI-confirmed sources exist", 20, fixture.snapshots().size)
        fixture.preserved(); fixture.report("twenty-real-ui-sources-committed")
        val refused = fixture.preview("abcd#1")
        fixture.proposal(refused, ""); fixture.firstRow("1"); fixture.role(ColumnRole.RECIPE, "1")
        for (role in ColumnRole.entries.filter { it != ColumnRole.RECIPE })
            assertEquals("The twenty-first mapping has no duplicate optional role", "", fixture.editable("column-${role.name}"))
        fixture.confirm(); fixture.preserved()
        assertTrue("Rejected origin never replaces any of the twenty accepted sources", fixture.snapshots().size == 20 &&
            fixture.snapshots().none { it.mapping.origin.key == refused.key })
        fixture.report("capacity-refusal-preserved")
        // Storage refusal must not ask the Chef to change a valid column mapping.
        fixture.status("CACHE_FAILED")
        fixture.report("capacity-storage-error-passed")
    }
}
