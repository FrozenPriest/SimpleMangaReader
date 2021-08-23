package ru.frozenpriest.simplemangareader.ui.screens.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.frozenpriest.simplemangareader.repository.MangaRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChapterViewerViewModel @Inject constructor(
    val repository: MangaRepository,
) : ViewModel() {

    val pages = MutableStateFlow(emptyList<String>())
    val loading = MutableStateFlow(true)

    fun loadChapterImages(id: String) = viewModelScope.launch {
        loading.value = true
        pages.value = repository.getMangaChapterImages(id)
        Timber.e(pages.value.joinToString("\n"))
        loading.value = false
    }
}