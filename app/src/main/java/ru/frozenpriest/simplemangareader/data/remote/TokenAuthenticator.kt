package ru.frozenpriest.simplemangareader.data.remote

import android.content.SharedPreferences
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.frozenpriest.simplemangareader.data.local.PreferenceHelper.refresh_token
import ru.frozenpriest.simplemangareader.data.local.PreferenceHelper.token
import ru.frozenpriest.simplemangareader.repository.MangaRepository

class TokenAuthenticator(
    private val sharedPreferences: SharedPreferences,
    private val repository: Lazy<MangaRepository>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        sharedPreferences.refresh_token?.let {
            runBlocking {
                sharedPreferences.token = repository.get().refreshToken(it).session
            }
        }
        // Add new header to rejected request and retry it
        return response.request.newBuilder()
            .header("Authorization", "Bearer ${sharedPreferences.token}")
            .build()
    }
}