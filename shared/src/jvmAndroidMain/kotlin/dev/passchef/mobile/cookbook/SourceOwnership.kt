package dev.passchef.mobile.cookbook

data class SourceTicket internal constructor(val generation: Long, val source: String)
/** Used by actual auth/refresh/write paths. Invalidation and final IO commit have one linearization point. */
class SourceOwnership {
    private var generation = 0L
    private var source: String? = null
    @Synchronized fun begin(context: String): SourceTicket { source = context; return SourceTicket(++generation, context) }
    @Synchronized fun invalidate() { generation++; source = null }
    @Synchronized fun accepts(ticket: SourceTicket, ready: Boolean): Boolean =
        ready && generation == ticket.generation && source == ticket.source
    @Synchronized fun commit(ticket: SourceTicket, ready: () -> Boolean, action: () -> Unit): Boolean {
        if (!accepts(ticket, ready())) return false
        action(); return true
    }
}
