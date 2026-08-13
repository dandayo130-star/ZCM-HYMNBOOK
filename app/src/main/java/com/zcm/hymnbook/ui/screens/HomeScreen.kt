package com.zcm.hymnbook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
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
import com.zcm.hymnbook.ui.components.SectionHeader
import com.zcm.hymnbook.ui.components.ZcmSearchBar
import com.zcm.hymnbook.viewmodel.HymnViewModel

@Composable
fun HomeScreen(
    viewModel: HymnViewModel,
    onHymnClick: (HymnEntity) -> Unit,
    onSeeAllHymns: () -> Unit
) {
    val query by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val recentlyViewed by viewModel.recentlyViewed.collectAsState()
    val featured by viewModel.featuredHymns.collectAsState()
    val allHymns by viewModel.allHymns.collectAsState()

    val isSearching = query.isNotBlank()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "ZCM Hymn Book",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Zenith Christian Ministry",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.padding(top = 14.dp))
            ZcmSearchBar(query = query, onQueryChange = viewModel::onSearchQueryChanged)
        }

        if (isSearching) {
            if (searchResults.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.MenuBook,
                    title = "No hymns found",
                    subtitle = "Try a different hymn number, title, lyric, author or composer."
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(searchResults, key = { it.id }) { hymn ->
                        HymnListItem(
                            hymn = hymn,
                            onClick = { onHymnClick(hymn) },
                            onFavoriteClick = { viewModel.toggleFavorite(hymn) }
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (recentlyViewed.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "Recently Viewed",
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(recentlyViewed, key = { "recent_${it.id}" }) { hymn ->
                                Box(modifier = Modifier.width(240.dp)) {
                                    HymnListItem(
                                        hymn = hymn,
                                        onClick = { onHymnClick(hymn) },
                                        onFavoriteClick = { viewModel.toggleFavorite(hymn) }
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    SectionHeader(
                        title = "Featured Hymns",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
                items(featured, key = { "featured_${it.id}" }) { hymn ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                        HymnListItem(
                            hymn = hymn,
                            onClick = { onHymnClick(hymn) },
                            onFavoriteClick = { viewModel.toggleFavorite(hymn) }
                        )
                    }
                }

                item {
                    SectionHeader(
                        title = "All Hymns",
                        actionLabel = "See all",
                        onActionClick = onSeeAllHymns,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
                items(allHymns.take(6), key = { "all_${it.id}" }) { hymn ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                        HymnListItem(
                            hymn = hymn,
                            onClick = { onHymnClick(hymn) },
                            onFavoriteClick = { viewModel.toggleFavorite(hymn) }
                        )
                    }
                }
            }
        }
    }
}
