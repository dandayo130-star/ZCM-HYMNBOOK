package com.zcm.hymnbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HymnDao {

    @Query("SELECT * FROM hymns ORDER BY hymnNumber ASC")
    fun getAllHymns(): Flow<List<HymnEntity>>

    @Query("SELECT * FROM hymns WHERE id = :id")
    fun getHymnById(id: Long): Flow<HymnEntity?>

    @Query("SELECT * FROM hymns WHERE hymnNumber = :hymnNumber LIMIT 1")
    suspend fun getHymnByNumber(hymnNumber: Int): HymnEntity?

    @Query("SELECT * FROM hymns WHERE isFavorite = 1 ORDER BY hymnNumber ASC")
    fun getFavoriteHymns(): Flow<List<HymnEntity>>

    @Query("SELECT * FROM hymns WHERE category = :category ORDER BY hymnNumber ASC")
    fun getHymnsByCategory(category: String): Flow<List<HymnEntity>>

    @Query(
        """
        SELECT * FROM hymns
        WHERE lastViewedAt IS NOT NULL
        ORDER BY lastViewedAt DESC
        LIMIT :limit
        """
    )
    fun getRecentlyViewed(limit: Int = 10): Flow<List<HymnEntity>>

    /**
     * "Featured" hymns: a simple, deterministic pick (lowest hymn numbers)
     * used to populate the Home screen's Featured section. This can later
     * be replaced with an admin-curated "isFeatured" flag from the remote
     * database without changing the Home screen UI.
     */
    @Query("SELECT * FROM hymns ORDER BY hymnNumber ASC LIMIT :limit")
    fun getFeaturedHymns(limit: Int = 6): Flow<List<HymnEntity>>

    @Query(
        """
        SELECT * FROM hymns
        WHERE CAST(hymnNumber AS TEXT) LIKE '%' || :query || '%'
           OR title LIKE '%' || :query || '%'
           OR lyrics LIKE '%' || :query || '%'
           OR author LIKE '%' || :query || '%'
           OR composer LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
        ORDER BY hymnNumber ASC
        """
    )
    fun searchHymns(query: String): Flow<List<HymnEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHymn(hymn: HymnEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHymns(hymns: List<HymnEntity>)

    @Update
    suspend fun updateHymn(hymn: HymnEntity)

    @Query("DELETE FROM hymns WHERE id = :id")
    suspend fun deleteHymnById(id: Long)

    @Query("UPDATE hymns SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE hymns SET lastViewedAt = :viewedAt WHERE id = :id")
    suspend fun markViewed(id: Long, viewedAt: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM hymns")
    suspend fun getHymnCount(): Int
}
