package com.yangdai.opennote.domain.usecase

import com.yangdai.opennote.data.local.entity.NoteEntity
import com.yangdai.opennote.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetNotes(
    private val repository: NoteRepository
) {
    // 缓存比较器以避免重复创建
    private val titleAscendingComparator = compareBy<NoteEntity> { it.title.lowercase() }
    private val titleDescendingComparator = compareByDescending<NoteEntity> { it.title.lowercase() }
    private val createdAscendingComparator = compareBy<NoteEntity> { it.createdAt }
    private val createdDescendingComparator = compareByDescending<NoteEntity> { it.createdAt }
    private val modifiedAscendingComparator = compareBy<NoteEntity> { it.timestamp }
    private val modifiedDescendingComparator = compareByDescending<NoteEntity> { it.timestamp }

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Modified(OrderType.Descending),
        trash: Boolean = false,
        filterFolder: Boolean = false,
        folderId: Long? = null,
    ): Flow<List<NoteEntity>> = flow {
        when {
            trash -> repository.getAllDeletedNotes()
            filterFolder -> repository.getNotesByFolderId(folderId)
            else -> repository.getAllNotes()
        }
            .flowOn(Dispatchers.IO)
            .map { notes ->
                withContext(Dispatchers.Default) {
                    sortNotes(notes, noteOrder)
                }
            }
            .collect { sortedNotes ->
                emit(sortedNotes)
            }
    }

    private fun sortNotes(notes: List<NoteEntity>, noteOrder: NoteOrder): List<NoteEntity> =
        when (noteOrder) {
            is NoteOrder.Title -> notes.sortedWith(
                when (noteOrder.orderType) {
                    OrderType.Ascending -> titleAscendingComparator
                    OrderType.Descending -> titleDescendingComparator
                }
            )

            is NoteOrder.Created -> notes.sortedWith(
                if (noteOrder.orderType is OrderType.Ascending) createdAscendingComparator
                else createdDescendingComparator
            )

            is NoteOrder.Modified -> notes.sortedWith(
                if (noteOrder.orderType is OrderType.Ascending) modifiedAscendingComparator
                else modifiedDescendingComparator
            )
        }
}
