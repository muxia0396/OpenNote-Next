package com.yangdai.opennote.data.local.dao

data class NoteSourceReference(
    val id: Long,
    val isDeleted: Boolean,
    val openAsExternalHtml: Boolean
)
