package ru.frozenpriest.simplemangareader.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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
    suspend fun checkTokenValid(): CheckTokenResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshToken: RefreshToken): RefreshTokenResponse

    //lib
    @GET("user/follows/manga")
    suspend fun getFollowedManga(): FollowedManga

    @GET("cover/{coverId}")
    suspend fun getMangaCover(@Path("coverId") coverId: String): CoverResponse
}