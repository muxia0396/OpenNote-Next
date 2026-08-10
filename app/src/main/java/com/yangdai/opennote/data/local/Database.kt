package com.yangdai.opennote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yangdai.opennote.data.local.dao.FolderDao
import com.yangdai.opennote.data.local.dao.NoteDao
import com.yangdai.opennote.data.local.entity.FolderEntity
import com.yangdai.opennote.data.local.entity.NoteEntity

@Database(
    version = 5,
    entities = [NoteEntity::class, FolderEntity::class]
)
abstract class Database : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val folderDao: FolderDao

    companion object {
        const val NAME = "NOTE_DB"
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // 添加索引
        db.execSQL("CREATE INDEX IF NOT EXISTS `idx_deleted_timestamp` ON `NoteEntity` (`isDeleted`, `timestamp`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `idx_folder_deleted_timestamp` ON `NoteEntity` (`folderId`, `isDeleted`, `timestamp`)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `NoteEntity` ADD COLUMN `sourceUri` TEXT DEFAULT NULL")
        db.execSQL("ALTER TABLE `NoteEntity` ADD COLUMN `lastReadProgress` REAL NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE `FolderEntity` ADD COLUMN `sourceUri` TEXT DEFAULT NULL")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `idx_note_source_uri` ON `NoteEntity` (`sourceUri`)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `idx_folder_source_uri` ON `FolderEntity` (`sourceUri`)")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `NoteEntity` ADD COLUMN `openAsExternalHtml` INTEGER NOT NULL DEFAULT 0")
        db.execSQL(
            "UPDATE `NoteEntity` SET `content` = '', `openAsExternalHtml` = 1 " +
                    "WHERE `sourceUri` IS NOT NULL AND " +
                    "(lower(`sourceUri`) LIKE '%.html%' OR lower(`sourceUri`) LIKE '%.htm%')"
        )
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `NoteEntity` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")
        db.execSQL("UPDATE `NoteEntity` SET `createdAt` = `timestamp` WHERE `createdAt` = 0")
    }
}
