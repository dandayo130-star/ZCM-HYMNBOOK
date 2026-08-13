package com.zcm.hymnbook.data.repository

import com.zcm.hymnbook.data.database.HymnDao
import com.zcm.hymnbook.data.database.HymnEntity
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for hymn data used by the ViewModels.
 *
 * Today this is backed only by the local Room database (HymnDao), so the
 * app is fully offline-capable.
 *
 * FUTURE SUPABASE INTEGRATION
 * ----------------------------
 * This class is intentionally the *only* place the rest of the app talks
 * to hymn data. When an online admin system is added, this repository is
 * where a remote data source gets wired in, e.g.:
 *
 *   class HymnRepository(
 *       private val hymnDao: HymnDao,
 *       private val remoteDataSource: SupabaseHymnDataSource? = null
 *   ) {
 *       suspend fun syncFromRemote() {
 *           val remoteHymns = remoteDataSource?.fetchAllHymns() ?: return
 *           hymnDao.insertHymns(remoteHymns.map { it.toEntity() })
 *       }
 *   }
 *
 * The Supabase URL and anon/public key must NEVER be hard-coded in the
 * app. They should be supplied at build time via a secure mechanism
 * (e.g. a gradle.properties file excluded from version control, or a
 * remote config service), never committed to source control, and the
 * anon key used must only have row-level-security-scoped READ access —
 * write/admin operations belong on a separate, authenticated admin
 * client (web dashboard or admin app), not inside this consumer app.
 */
class HymnRepository(private val hymnDao: HymnDao) {

    val allHymns: Flow<List<HymnEntity>> = hymnDao.getAllHymns()

    val favoriteHymns: Flow<List<HymnEntity>> = hymnDao.getFavoriteHymns()

    val recentlyViewed: Flow<List<HymnEntity>> = hymnDao.getRecentlyViewed(10)

    val featuredHymns: Flow<List<HymnEntity>> = hymnDao.getFeaturedHymns(6)

    fun getHymnsByCategory(category: String): Flow<List<HymnEntity>> =
        hymnDao.getHymnsByCategory(category)

    fun getHymnById(id: Long): Flow<HymnEntity?> = hymnDao.getHymnById(id)

    fun searchHymns(query: String): Flow<List<HymnEntity>> {
        val trimmed = query.trim()
        return if (trimmed.isEmpty()) allHymns else hymnDao.searchHymns(trimmed)
    }

    suspend fun getHymnByNumber(number: Int): HymnEntity? = hymnDao.getHymnByNumber(number)

    suspend fun toggleFavorite(hymn: HymnEntity) {
        hymnDao.setFavorite(hymn.id, !hymn.isFavorite)
    }

    suspend fun markViewed(hymnId: Long) {
        hymnDao.markViewed(hymnId)
    }

    suspend fun insertHymn(hymn: HymnEntity): Long = hymnDao.insertHymn(hymn)

    suspend fun insertHymns(hymns: List<HymnEntity>) = hymnDao.insertHymns(hymns)

    suspend fun updateHymn(hymn: HymnEntity) = hymnDao.updateHymn(hymn)

    suspend fun deleteHymn(id: Long) = hymnDao.deleteHymnById(id)

    suspend fun hymnCount(): Int = hymnDao.getHymnCount()
}
