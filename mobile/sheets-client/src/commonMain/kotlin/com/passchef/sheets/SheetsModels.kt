package com.passchef.sheets

import kotlinx.serialization.Serializable

/**
 * Wire model for the Sheets API v4 `spreadsheets.values` resource -- used both
 * as a `values.get` response body and as the request body for
 * `values.append` / `values.update` (D2/D3, oauth-sheets-3).
 *
 * `values` defaults to empty: Google omits the field entirely for an empty
 * range/sheet rather than returning `"values": []` (plan.md test matrix
 * item 2, "Input extremes").
 */
@Serializable
data class ValueRange(
    val range: String? = null,
    val majorDimension: String = "ROWS",
    val values: List<List<String>> = emptyList()
)

/** Response body of a `spreadsheets.values.update` call. */
@Serializable
data class UpdateValuesResponse(
    val spreadsheetId: String? = null,
    val updatedRange: String? = null,
    val updatedRows: Int = 0,
    val updatedColumns: Int = 0,
    val updatedCells: Int = 0
)

/** Response body of a `spreadsheets.values.append` call. */
@Serializable
data class AppendValuesResponse(
    val spreadsheetId: String? = null,
    val tableRange: String? = null,
    val updates: UpdateValuesResponse? = null
)

/**
 * Google API error envelope, e.g.
 * `{"error": {"code": 401, "message": "...", "status": "UNAUTHENTICATED"}}`.
 */
@Serializable
data class GoogleApiErrorBody(val error: GoogleApiErrorDetail)

@Serializable
data class GoogleApiErrorDetail(
    val code: Int,
    val message: String,
    val status: String? = null
)


@Serializable data class SpreadsheetMetadata(val spreadsheetId: String, val properties: SpreadsheetTitle,
    val sheets: List<SpreadsheetTab>)
@Serializable data class SpreadsheetTitle(val title: String)
@Serializable data class SpreadsheetTab(val properties: TabProperties)
@Serializable data class TabProperties(val sheetId: Int, val title: String, val gridProperties: GridSize)
@Serializable data class GridSize(val rowCount: Int, val columnCount: Int)
