package com.yangdai.opennote.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    indices = [
        // 用于getAllNotes和getAllDeletedNotes
        Index(value = ["isDeleted", "timestamp"], name = "idx_deleted_timestamp"),
        // 用于getNotesByFolderId
        Index(value = ["folderId", "isDeleted", "timestamp"], name = "idx_folder_deleted_timestamp"),
        Index(value = ["sourceUri"], unique = true, name = "idx_note_source_uri")
    ]
)
data class NoteEntity(
    @PrimaryKey val id: Long? = null,
    val title: String = "",
    val content: String = "",
    val folderId: Long? = null,
    val isMarkdown: Boolean = true,
    val isDeleted: Boolean = false,
    val sourceUri: String? = null,
    @ColumnInfo(defaultValue = "0") val lastReadProgress: Float = 0f,
    @ColumnInfo(defaultValue = "0") val openAsExternalHtml: Boolean = false,
    @ColumnInfo(defaultValue = "0") val createdAt: Long = 0L,
    val timestamp: Long
)
