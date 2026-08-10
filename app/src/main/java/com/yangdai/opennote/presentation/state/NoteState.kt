package com.yangdai.opennote.presentation.state

import androidx.compose.runtime.Stable

@Stable
data class NoteState(
    val id: Long? = null,
    val folderId: Long? = null,
    val isStandard: Boolean = true,
    val createdAt: Long? = null,
    val timestamp: Long? = null,
    val sourceUri: String? = null,
    val lastReadProgress: Float = 0f,
    val openAsExternalHtml: Boolean = false
)
