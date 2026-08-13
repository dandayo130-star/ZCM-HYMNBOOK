package com.zcm.hymnbook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zcm.hymnbook.BuildConfig
import com.zcm.hymnbook.data.preferences.AppTheme
import com.zcm.hymnbook.data.preferences.TextSize
import com.zcm.hymnbook.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val theme by viewModel.theme.collectAsState()
    val textSize by viewModel.textSize.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 20.dp))
        SettingsSectionCard(title = "Theme") {
            Column(Modifier.selectableGroup()) {
                ThemeOptionRow("System Default", theme == AppTheme.SYSTEM) { viewModel.setTheme(AppTheme.SYSTEM) }
                ThemeOptionRow("Light", theme == AppTheme.LIGHT) { viewModel.setTheme(AppTheme.LIGHT) }
                ThemeOptionRow("Dark", theme == AppTheme.DARK) { viewModel.setTheme(AppTheme.DARK) }
            }
        }

        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 16.dp))
        SettingsSectionCard(title = "Text Size") {
            Column(Modifier.selectableGroup()) {
                TextSize.entries.forEach { size ->
                    ThemeOptionRow(size.label, textSize == size) { viewModel.setTextSize(size) }
                }
            }
        }

        androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 16.dp))
        SettingsSectionCard(title = "About") {
            Column {
                Text(
                    text = "ZCM Hymn Book",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "A digital hymn book built for Zenith Christian Ministry, Odo Ere — for offline browsing, searching and reading of hymns.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
                )
                Text(
                    text = "App Version: ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .padding(vertical = 10.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
