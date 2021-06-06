package ru.frozenpriest.simplemangareader.ui.screens.library

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.frozenpriest.simplemangareader.data.local.PreferenceHelper.refresh_token
import ru.frozenpriest.simplemangareader.data.local.PreferenceHelper.token
import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.repository.MangaRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    val repository: MangaRepository,
    val sharedPreferences: SharedPreferences
) : ViewModel() {
    val mangaFeedList = mutableStateOf<List<Manga>>(listOf())

    fun authIn(login: String, password: String) = viewModelScope.launch {
        val token = repository.authIn(login, password)
        sharedPreferences.token = token.session
        sharedPreferences.refresh_token = token.refresh
        Timber.e("Token = ${token.session}, refresh = ${token.refresh}")
        getMangas()
    }

    fun getMangas() = viewModelScope.launch {
        mangaFeedList.value = repository.getUserMangasWithCovers()
    }
}