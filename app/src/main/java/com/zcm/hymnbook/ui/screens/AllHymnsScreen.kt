package com.zcm.hymnbook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zcm.hymnbook.data.database.HymnEntity
import com.zcm.hymnbook.ui.components.EmptyState
import com.zcm.hymnbook.ui.components.HymnListItem
import com.zcm.hymnbook.viewmodel.HymnViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllHymnsScreen(
    viewModel: HymnViewModel,
    onHymnClick: (HymnEntity) -> Unit,
    onBack: () -> Unit
) {
    val allHymns by viewModel.allHymns.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("All Hymns") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (allHymns.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.MenuBook,
                title = "No hymns yet",
                subtitle = "Hymns you add will appear here.",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding()),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(allHymns, key = { it.id }) { hymn ->
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
