package com.yangdai.opennote.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yangdai.opennote.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT id, title, substr(content, 1, 6000) AS content, folderId, isMarkdown, isDeleted, sourceUri, lastReadProgress, openAsExternalHtml, createdAt, timestamp FROM NOTEENTITY WHERE isDeleted = 0 ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT id, title, substr(content, 1, 6000) AS content, folderId, isMarkdown, isDeleted, sourceUri, lastReadProgress, openAsExternalHtml, createdAt, timestamp FROM NOTEENTITY WHERE isDeleted = 1 ORDER BY timestamp DESC")
    fun getAllDeletedNotes(): Flow<List<NoteEntity>>

    @Query("SELECT id, title, substr(content, 1, 6000) AS content, folderId, isMarkdown, isDeleted, sourceUri, lastReadProgress, openAsExternalHtml, createdAt, timestamp FROM NOTEENTITY WHERE folderId = :folderId AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getNotesByFolderId(folderId: Long?): Flow<List<NoteEntity>>

    @Query("SELECT id, title, CASE WHEN instr(lower(content), lower(:keyword)) > 0 THEN substr(content, max(instr(lower(content), lower(:keyword)) - 500, 1), 1600) ELSE substr(content, 1, 1600) END AS content, folderId, isMarkdown, isDeleted, sourceUri, lastReadProgress, openAsExternalHtml, createdAt, timestamp FROM NOTEENTITY WHERE isDeleted = 0 AND (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%') ORDER BY timestamp DESC")
    fun getNotesByKeyWord(keyword: String): Flow<List<NoteEntity>>

    @Query("SELECT id, sourceUri, createdAt FROM NOTEENTITY WHERE sourceUri IS NOT NULL AND isDeleted = 0")
    suspend fun getImportedNoteCreationReferences(): List<NoteCreationReference>

    @Query("SELECT * FROM NOTEENTITY WHERE isDeleted = 0 ORDER BY timestamp DESC")
    suspend fun getAllNotesForBackup(): List<NoteEntity>

    @Query(
        """
        SELECT * FROM NOTEENTITY
        WHERE id = :id
    """
    )
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT id, isDeleted, openAsExternalHtml FROM NOTEENTITY WHERE sourceUri = :sourceUri LIMIT 1")
    suspend fun getNoteBySourceUri(sourceUri: String): NoteSourceReference?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteEntity: NoteEntity): Long

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Query("DELETE FROM NOTEENTITY WHERE folderId = :folderId")
    suspend fun deleteNotesByFolderId(folderId: Long?)

    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    @Query("UPDATE NOTEENTITY SET lastReadProgress = :progress WHERE id = :id")
    suspend fun updateReadProgress(id: Long, progress: Float)

    @Query("UPDATE NOTEENTITY SET createdAt = :createdAt WHERE id = :id")
    suspend fun updateCreatedAt(id: Long, createdAt: Long)

    @Query("UPDATE NOTEENTITY SET isDeleted = :isDeleted WHERE id = :id")
    suspend fun updateDeletedState(id: Long, isDeleted: Boolean)

    @Query("UPDATE NOTEENTITY SET folderId = :folderId, isDeleted = 0 WHERE id = :id")
    suspend fun moveNote(id: Long, folderId: Long?)

    @Query("UPDATE NOTEENTITY SET content = '', openAsExternalHtml = 1 WHERE id = :id")
    suspend fun convertToExternalHtml(id: Long)

}

data class NoteCreationReference(
    val id: Long,
    val sourceUri: String,
    val createdAt: Long
)
