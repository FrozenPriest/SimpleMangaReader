package ru.frozenpriest.simplemangareader.ui.screens.explore

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.repository.MangaRepository
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    val repository: MangaRepository,
    val sharedPreferences: SharedPreferences
) : ViewModel() {
    val mangaFeedList = mutableStateOf<List<Manga>>(listOf())
    val isLoading = MutableStateFlow(false)
    val isLoadingMore = MutableStateFlow(false)

    private var offset = 0
    private val limit = 20
    private var total = 20

    fun getMangas() = viewModelScope.launch {
        isLoading.value = true
        offset = 0
        val mangas = repository.getMangasWithCovers(offset = 0, limit = limit)
        mangaFeedList.value = mangas.mangas
        total = mangas.total
        offset += limit
        isLoading.value = false
    }

    fun loadMore() = viewModelScope.launch {
        isLoadingMore.value = true
        val mangas =  repository.getMangasWithCovers(offset = offset, limit = limit)
        mangaFeedList.value += mangas.mangas
        total = mangas.total
        offset += limit
        isLoadingMore.value = false
    }

    fun canLoadMore(): Boolean = !isLoadingMore.value && total >= offset
}