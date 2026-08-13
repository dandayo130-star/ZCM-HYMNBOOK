package com.zcm.hymnbook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zcm.hymnbook.data.database.HymnEntity
import com.zcm.hymnbook.ui.components.EmptyState
import com.zcm.hymnbook.ui.components.HymnListItem
import com.zcm.hymnbook.viewmodel.HymnViewModel

@Composable
fun FavoritesScreen(
    viewModel: HymnViewModel,
    onHymnClick: (HymnEntity) -> Unit
) {
    val favorites by viewModel.favoriteHymns.collectAsState()

    if (favorites.isEmpty()) {
        EmptyState(
            icon = Icons.Filled.FavoriteBorder,
            title = "No favorite hymns yet",
            subtitle = "Tap the heart on any hymn to save it here.",
            modifier = Modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Favorites",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(favorites, key = { it.id }) { hymn ->
                HymnListItem(
                    hymn = hymn,
                    onClick = { onHymnClick(hymn) },
                    onFavoriteClick = { viewModel.toggleFavorite(hymn) }
                )
            }
        }
    }
}
