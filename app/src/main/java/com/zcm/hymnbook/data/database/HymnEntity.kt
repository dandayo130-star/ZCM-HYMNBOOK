package com.zcm.hymnbook.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single hymn.
 *
 * Field names are chosen to map 1:1 onto a future Supabase "hymns"
 * table (see HymnRepository docs) so that a remote sync layer can be
 * dropped in later with minimal translation logic.
 */
@Entity(tableName = "hymns")
data class HymnEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hymnNumber: Int,
    val title: String,
    val lyrics: String,
    val category: String,
    val author: String,
    val composer: String,
    val isFavorite: Boolean = false,
    val lastViewedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
