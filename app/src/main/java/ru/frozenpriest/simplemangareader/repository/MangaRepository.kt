package ru.frozenpriest.simplemangareader.repository

import ru.frozenpriest.simplemangareader.data.models.Manga
import ru.frozenpriest.simplemangareader.data.models.MangaListWithTotal
import ru.frozenpriest.simplemangareader.data.models.UserInfo
import ru.frozenpriest.simplemangareader.data.remote.MangadexApi
import ru.frozenpriest.simplemangareader.data.remote.RefreshToken
import ru.frozenpriest.simplemangareader.data.remote.responses.Token
import timber.log.Timber

class MangaRepository(
    private val api: MangadexApi
) {

    suspend fun authIn(login: String, password: String): Token {
        return api.authIn(UserInfo(login, password)).token
    }

    suspend fun refreshToken(refreshToken: String) : Token {
        return api.refreshToken(RefreshToken(refreshToken)).token
    }

    suspend fun checkTokenValid(token: String) : Boolean {
        return api.checkTokenValid("Bearer $token").isAuthenticated
    }

    suspend fun getUserMangasWithCovers(offset: Int, limit: Int = 20): MangaListWithTotal {
        val resp = api.getFollowedManga(offset, limit)
        val mangas = mutableListOf<Manga>()
        resp.results.forEach { manga ->
            Timber.e(manga.relationships.toString())
            val coverFileName = api.getMangaCover(manga.relationships.find { it.type == "cover_art" }?.id ?:"").data.attributes.fileName
            mangas.add(
                Manga(
                    name = manga.data.attributes.title.en,
                    posterLink = "https://uploads.mangadex.org/covers/${manga.data.id}/${coverFileName}"
                )
            )
        }

        return MangaListWithTotal(mangas, resp.total)
    }

    suspend fun getMangasWithCovers(offset: Int, limit: Int = 20): MangaListWithTotal {
        val resp = api.getMangas(offset, limit)
        val mangas = mutableListOf<Manga>()
        resp.results.forEach { manga ->
            Timber.e(manga.relationships.toString())
            val coverFileName = api.getMangaCover(manga.relationships.find { it.type == "cover_art" }?.id ?:"").data.attributes.fileName
            mangas.add(
                Manga(
                    name = manga.data.attributes.title.en,
                    posterLink = "https://uploads.mangadex.org/covers/${manga.data.id}/${coverFileName}"
                )
            )
        }

        return MangaListWithTotal(mangas, resp.total)
    }


}