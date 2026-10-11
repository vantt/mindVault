package com.passchef.sheets

/**
 * Distinct, catchable error types for Sheets API v4 failures (plan.md test
 * matrix item 7 "Error cascades" / item 8 "Authorization") -- callers must be
 * able to tell an expired/revoked token (401) apart from a scope/consent
 * mismatch (403) apart from any other API failure, never a generic exception.
 */
sealed class SheetsClientException(message: String) : Exception(message)

/** The access token is missing, expired, or revoked (Sheets API responded 401). */
class SheetsUnauthorizedException(message: String) : SheetsClientException(message)

/**
 * The token lacks the required scope, or the user has not granted consent
 * (Sheets API responded 403).
 */
class SheetsForbiddenException(message: String) : SheetsClientException(message)

/** Any other non-2xx Sheets API response. */
class SheetsApiException(val statusCode: Int, message: String) : SheetsClientException(message)
