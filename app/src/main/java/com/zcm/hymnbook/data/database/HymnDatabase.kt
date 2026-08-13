package com.zcm.hymnbook.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zcm.hymnbook.data.database.seed.SampleHymns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [HymnEntity::class], version = 1, exportSchema = false)
abstract class HymnDatabase : RoomDatabase() {

    abstract fun hymnDao(): HymnDao

    companion object {
        @Volatile
        private var INSTANCE: HymnDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HymnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HymnDatabase::class.java,
                    "zcm_hymn_book.db"
                )
                    .addCallback(SeedDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    /**
     * Populates the database with sample hymns the very first time it is
     * created on a device. This only ever runs once per install (Room only
     * calls onCreate the first time the underlying SQLite file is created).
     */
    private class SeedDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    database.hymnDao().insertHymns(SampleHymns.list())
                }
            }
        }
    }
}
