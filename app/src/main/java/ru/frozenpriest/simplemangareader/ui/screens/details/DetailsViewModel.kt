package ru.frozenpriest.simplemangareader.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.frozenpriest.simplemangareader.data.models.ChapterInfo
import ru.frozenpriest.simplemangareader.repository.MangaRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: MangaRepository,
) : ViewModel() {

    val chapters = MutableStateFlow(emptyList<ChapterInfo>())

    fun loadChapters(id: String) = viewModelScope.launch{
        val newChapters =  repository.getMangaChapters(id)
        chapters.value = newChapters
    }
}