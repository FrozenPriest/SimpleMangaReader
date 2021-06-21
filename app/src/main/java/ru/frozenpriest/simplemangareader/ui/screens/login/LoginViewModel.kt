package ru.frozenpriest.simplemangareader.ui.screens.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.frozenpriest.simplemangareader.data.local.PreferenceHelper.login
import ru.frozenpriest.simplemangareader.data.local.PreferenceHelper.refresh_token
import ru.frozenpriest.simplemangareader.data.local.PreferenceHelper.token
import ru.frozenpriest.simplemangareader.repository.MangaRepository
import ru.frozenpriest.simplemangareader.util.Failure
import ru.frozenpriest.simplemangareader.util.Success
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val repository: MangaRepository,
    val sharedPreferences: SharedPreferences
) : ViewModel() {
    val authSuccessful = MutableStateFlow(false)
    val login = sharedPreferences.login ?: ""

    init {
        authSuccessful.value = sharedPreferences.refresh_token != ""

    }

    fun authIn(login: String, password: String) = viewModelScope.launch {
        when (val tokenResult = repository.authIn(login, password)) {
            is Failure -> authSuccessful.value = false
            is Success -> {
                val token = tokenResult.data!!
                sharedPreferences.token = token.session
                sharedPreferences.refresh_token = token.refresh
                Timber.e("Token = ${token.session}, refresh = ${token.refresh}")
                sharedPreferences.login = login
                authSuccessful.value = true
            }
        }

    }
}
