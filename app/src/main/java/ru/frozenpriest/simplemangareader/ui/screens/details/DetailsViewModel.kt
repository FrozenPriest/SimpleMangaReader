package ru.frozenpriest.simplemangareader.ui.screens.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.frozenpriest.simplemangareader.repository.MangaRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: MangaRepository,
) : ViewModel() {

}