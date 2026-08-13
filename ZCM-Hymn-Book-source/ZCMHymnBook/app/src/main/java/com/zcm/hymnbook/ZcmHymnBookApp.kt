package com.zcm.hymnbook

import android.app.Application
import com.zcm.hymnbook.data.database.HymnDatabase
import com.zcm.hymnbook.data.preferences.UserPreferencesRepository
import com.zcm.hymnbook.data.repository.HymnRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Application class. Owns the singleton instances of the database,
 * repository and preferences so they can be shared across the app
 * without a full DI framework. Kept intentionally simple; can be
 * swapped for Hilt later without touching the UI layer.
 */
class ZcmHymnBookApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database: HymnDatabase by lazy { HymnDatabase.getDatabase(this, applicationScope) }

    val hymnRepository: HymnRepository by lazy { HymnRepository(database.hymnDao()) }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(this)
    }
}
