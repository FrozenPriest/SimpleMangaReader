package ru.frozenpriest.simplemangareader.data.remote

import retrofit2.http.*
import ru.frozenpriest.simplemangareader.data.models.UserInfo
import ru.frozenpriest.simplemangareader.data.remote.responses.*
import ru.frozenpriest.simplemangareader.data.remote.responses.usermangas.FollowedManga

interface MangadexApi {
    //auth
    @POST("auth/login")
    suspend fun authIn(@Body userInfo: UserInfo): AuthResponse

    @GET("auth/check")
    suspend fun checkTokenValid(@Header("Authorization") tokenBearer: String): CheckTokenResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshToken: RefreshToken): RefreshTokenResponse

    //lib
    @GET("user/follows/manga")
    suspend fun getFollowedManga(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20,
    ): FollowedManga

    @GET("cover/{coverId}")
    suspend fun getMangaCover(@Path("coverId") coverId: String): CoverResponse

    @GET("manga")
    suspend fun getMangas(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20,
    ): MangaResponse

    @GET("chapter")
    suspend fun getMangaChapters(
        @Query("manga") mangaId: String,
        @Query("translatedLanguage[]") translatedLanguages: ArrayList<String> = arrayListOf("en", "ru")
    ): ChaptersResponse
}