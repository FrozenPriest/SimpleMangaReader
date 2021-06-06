package ru.frozenpriest.simplemangareader.data.remote

import retrofit2.http.*
import ru.frozenpriest.simplemangareader.data.models.UserInfo
import ru.frozenpriest.simplemangareader.data.remote.responses.AuthResponse
import ru.frozenpriest.simplemangareader.data.remote.responses.CheckTokenResponse
import ru.frozenpriest.simplemangareader.data.remote.responses.CoverResponse
import ru.frozenpriest.simplemangareader.data.remote.responses.RefreshTokenResponse
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
    suspend fun getFollowedManga(): FollowedManga

    @GET("cover/{coverId}")
    suspend fun getMangaCover(@Path("coverId") coverId: String): CoverResponse
}