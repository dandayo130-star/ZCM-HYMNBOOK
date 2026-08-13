package com.zcm.hymnbook.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/** All navigable destinations. */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Favorites : Screen("favorites")
    data object Categories : Screen("categories")
    data object Settings : Screen("settings")
    data object AllHymns : Screen("all_hymns")

    data object CategoryDetail : Screen("category/{categoryLabel}") {
        fun createRoute(categoryLabel: String) = "category/$categoryLabel"
    }

    data object HymnReader : Screen("reader/{hymnId}") {
        fun createRoute(hymnId: Long) = "reader/$hymnId"
    }
}

/** Simple pairing of a bottom-nav destination with its label/icon. */
data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", Icons.Filled.Home),
    BottomNavItem(Screen.Favorites, "Favorites", Icons.Filled.Favorite),
    BottomNavItem(Screen.Categories, "Categories", Icons.Filled.List),
    BottomNavItem(Screen.Settings, "Settings", Icons.Filled.Settings)
)
