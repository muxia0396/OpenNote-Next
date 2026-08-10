package com.yangdai.opennote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yangdai.opennote.data.local.entity.FolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Query("SELECT * FROM FOLDERENTITY")
    fun getAllFolders(): Flow<List<FolderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folderEntity: FolderEntity): Long

    @Query("SELECT * FROM FOLDERENTITY WHERE name = :name LIMIT 1")
    suspend fun getFolderByName(name: String): FolderEntity?

    @Query("SELECT * FROM FOLDERENTITY WHERE sourceUri = :sourceUri LIMIT 1")
    suspend fun getFolderBySourceUri(sourceUri: String): FolderEntity?

    @Delete
    suspend fun deleteFolder(folderEntity: FolderEntity)

    @Update
    suspend fun updateFolder(folderEntity: FolderEntity)

}
