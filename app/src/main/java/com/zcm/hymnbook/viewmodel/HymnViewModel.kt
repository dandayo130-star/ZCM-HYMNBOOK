package com.zcm.hymnbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zcm.hymnbook.data.database.HymnEntity
import com.zcm.hymnbook.data.repository.HymnRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Central ViewModel backing Home, Search, Hymn List, Hymn Reader,
 * Favorites and Categories. Keeps all Room/Flow access out of Compose UI
 * as required — screens only observe StateFlow and call plain functions.
 */
class HymnViewModel(private val repository: HymnRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val searchResults: StateFlow<List<HymnEntity>> = _searchQuery
        .flatMapLatest { query -> repository.searchHymns(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHymns: StateFlow<List<HymnEntity>> = repository.allHymns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteHymns: StateFlow<List<HymnEntity>> = repository.favoriteHymns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentlyViewed: StateFlow<List<HymnEntity>> = repository.recentlyViewed
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredHymns: StateFlow<List<HymnEntity>> = repository.featuredHymns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    val categoryHymns: StateFlow<List<HymnEntity>> = _selectedCategory
        .flatMapLatest { category ->
            if (category == null) repository.allHymns else repository.getHymnsByCategory(category)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(hymn: HymnEntity) {
        viewModelScope.launch { repository.toggleFavorite(hymn) }
    }

    fun markViewed(hymnId: Long) {
        viewModelScope.launch { repository.markViewed(hymnId) }
    }

    fun observeHymn(id: Long) = repository.getHymnById(id)

    /**
     * Returns the hymn immediately before/after the given hymn number
     * within the full, number-sorted hymn list, used for Previous/Next
     * navigation in the reader. Falls back to null at the list boundaries.
     */
    fun neighborHymn(currentHymnNumber: Int, direction: Int): HymnEntity? {
        val sorted = allHymns.value.sortedBy { it.hymnNumber }
        val currentIndex = sorted.indexOfFirst { it.hymnNumber == currentHymnNumber }
        if (currentIndex == -1) return null
        val targetIndex = currentIndex + direction
        return sorted.getOrNull(targetIndex)
    }

    class Factory(private val repository: HymnRepository) : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HymnViewModel::class.java)) {
                return HymnViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
