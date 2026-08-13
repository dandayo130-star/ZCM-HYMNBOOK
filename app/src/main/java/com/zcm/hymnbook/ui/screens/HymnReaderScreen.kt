package com.zcm.hymnbook.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zcm.hymnbook.data.database.HymnEntity
import com.zcm.hymnbook.ui.theme.LyricsTextStyle
import com.zcm.hymnbook.viewmodel.HymnViewModel
import com.zcm.hymnbook.viewmodel.SettingsViewModel

/**
 * Full hymn-reading screen: number, title, author, composer, category,
 * lyrics, favorite/share/text-size controls, and previous/next navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HymnReaderScreen(
    hymnId: Long,
    hymnViewModel: HymnViewModel,
    settingsViewModel: SettingsViewModel,
    onBack: () -> Unit,
    onOpenHymn: (Long) -> Unit
) {
    val hymn by hymnViewModel.observeHymn(hymnId).collectAsState(initial = null)
    val fontScalePercent by settingsViewModel.readerFontScalePercent.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(hymnId) {
        hymnViewModel.markViewed(hymnId)
    }

    val currentHymn = hymn

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(currentHymn?.let { "Hymn #${it.hymnNumber}" } ?: "Hymn") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (currentHymn != null) {
                        IconButton(onClick = { hymnViewModel.toggleFavorite(currentHymn) }) {
                            Icon(
                                imageVector = if (currentHymn.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Toggle favorite",
                                tint = if (currentHymn.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(onClick = { shareHymn(context, currentHymn) }) {
                            Icon(Icons.Filled.Share, contentDescription = "Share hymn")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (currentHymn == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Hymn not found")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = currentHymn.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentHymn.category,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 6.dp))

            Text(
                text = "Author: ${currentHymn.author}    •    Composer: ${currentHymn.composer}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 12.dp))

            // Text size controls
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = { settingsViewModel.decreaseReaderFontScale() }) {
                    Icon(Icons.Filled.TextDecrease, contentDescription = "Decrease text size")
                }
                Text(
                    text = "$fontScalePercent%",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                IconButton(onClick = { settingsViewModel.increaseReaderFontScale() }) {
                    Icon(Icons.Filled.TextIncrease, contentDescription = "Increase text size")
                }
            }

            androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 8.dp))

            Text(
                text = currentHymn.lyrics,
                style = LyricsTextStyle.copy(fontSize = (LyricsTextStyle.fontSize.value * (fontScalePercent / 100f)).sp)
            )

            androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 24.dp))

            // Previous / Next navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val previous = hymnViewModel.neighborHymn(currentHymn.hymnNumber, -1)
                val next = hymnViewModel.neighborHymn(currentHymn.hymnNumber, 1)

                TextButton(
                    onClick = { previous?.let { onOpenHymn(it.id) } },
                    enabled = previous != null
                ) {
                    Icon(Icons.Filled.ArrowBackIosNew, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
                    Text("Previous")
                }
                TextButton(
                    onClick = { next?.let { onOpenHymn(it.id) } },
                    enabled = next != null
                ) {
                    Text("Next")
                    Icon(Icons.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.padding(start = 6.dp))
                }
            }
        }
    }
}

private fun shareHymn(context: android.content.Context, hymn: HymnEntity) {
    val shareText = buildString {
        appendLine("ZCM Hymn Book")
        appendLine()
        appendLine("Hymn #${hymn.hymnNumber}: ${hymn.title}")
        appendLine()
        appendLine(hymn.lyrics)
    }
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(sendIntent, "Share hymn via"))
}
