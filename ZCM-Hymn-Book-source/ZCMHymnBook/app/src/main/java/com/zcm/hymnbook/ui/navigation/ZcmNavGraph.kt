package com.zcm.hymnbook.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.zcm.hymnbook.data.preferences.UserPreferencesRepository
import com.zcm.hymnbook.data.repository.HymnRepository
import com.zcm.hymnbook.ui.screens.AllHymnsScreen
import com.zcm.hymnbook.ui.screens.CategoriesScreen
import com.zcm.hymnbook.ui.screens.CategoryDetailScreen
import com.zcm.hymnbook.ui.screens.FavoritesScreen
import com.zcm.hymnbook.ui.screens.HomeScreen
import com.zcm.hymnbook.ui.screens.HymnReaderScreen
import com.zcm.hymnbook.ui.screens.SettingsScreen
import com.zcm.hymnbook.viewmodel.HymnViewModel
import com.zcm.hymnbook.viewmodel.SettingsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ZcmNavGraph(
    hymnRepository: HymnRepository,
    preferencesRepository: UserPreferencesRepository
) {
    val navController = rememberNavController()
    val hymnViewModel: HymnViewModel = viewModel(factory = HymnViewModel.Factory(hymnRepository))
    val settingsViewModel: SettingsViewModel =
        viewModel(factory = SettingsViewModel.Factory(preferencesRepository))

    Scaffold(
        bottomBar = { ZcmBottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = hymnViewModel,
                    onHymnClick = { hymn -> navController.navigate(Screen.HymnReader.createRoute(hymn.id)) },
                    onSeeAllHymns = { navController.navigate(Screen.AllHymns.route) }
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    viewModel = hymnViewModel,
                    onHymnClick = { hymn -> navController.navigate(Screen.HymnReader.createRoute(hymn.id)) }
                )
            }
            composable(Screen.Categories.route) {
                CategoriesScreen(
                    onCategoryClick = { label ->
                        navController.navigate(Screen.CategoryDetail.createRoute(label))
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = settingsViewModel)
            }
            composable(Screen.AllHymns.route) {
                AllHymnsScreen(
                    viewModel = hymnViewModel,
                    onHymnClick = { hymn -> navController.navigate(Screen.HymnReader.createRoute(hymn.id)) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.CategoryDetail.route,
                arguments = listOf(navArgument("categoryLabel") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryLabel = backStackEntry.arguments?.getString("categoryLabel").orEmpty()
                CategoryDetailScreen(
                    categoryLabel = categoryLabel,
                    viewModel = hymnViewModel,
                    onHymnClick = { hymn -> navController.navigate(Screen.HymnReader.createRoute(hymn.id)) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.HymnReader.route,
                arguments = listOf(navArgument("hymnId") { type = NavType.LongType })
            ) { backStackEntry ->
                val hymnId = backStackEntry.arguments?.getLong("hymnId") ?: -1L
                HymnReaderScreen(
                    hymnId = hymnId,
                    hymnViewModel = hymnViewModel,
                    settingsViewModel = settingsViewModel,
                    onBack = { navController.popBackStack() },
                    onOpenHymn = { newId ->
                        navController.navigate(Screen.HymnReader.createRoute(newId)) {
                            popUpTo(Screen.HymnReader.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ZcmBottomNavigationBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { androidx.compose.material3.Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
