package com.yangdai.opennote.presentation.viewmodel

internal object SourceCreationTimeResolver {
    val trustedProviderColumns = listOf(
        "date_created",
        "creation_time",
        "created_at",
        "created",
        "ctime"
    )

    fun normalizeProviderTimestamp(value: Long): Long? = when {
        value <= 0L -> null
        value < 10_000_000_000L -> value * 1_000L
        else -> value
    }
}
