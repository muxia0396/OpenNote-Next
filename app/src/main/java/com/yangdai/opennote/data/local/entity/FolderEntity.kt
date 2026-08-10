package com.yangdai.opennote.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yangdai.opennote.presentation.theme.*
import kotlinx.serialization.Serializable

@Serializable
@Entity(indices = [Index(value = ["sourceUri"], unique = true, name = "idx_folder_source_uri")])
data class FolderEntity(
    @PrimaryKey val id: Long? = null,
    val name: String = "",
    val color: Int? = null,
    val sourceUri: String? = null
) {
    companion object {
        val folderColors = listOf(
            Red,
            Orange,
            Yellow,
            Green,
            Cyan,
            Blue,
            Purple
        )
    }
}
