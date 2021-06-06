package ru.frozenpriest.simplemangareader.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.frozenpriest.simplemangareader.data.remote.MangadexApi
import ru.frozenpriest.simplemangareader.data.remote.TokenAuthenticator
import ru.frozenpriest.simplemangareader.repository.MangaRepository
import ru.frozenpriest.simplemangareader.util.Constants.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMangadexRepository(
        api: MangadexApi
    ) = MangaRepository(api)


    @Singleton
    @Provides
    fun provideOkHttpClient(
        sharedPreferences: SharedPreferences,
        repositoryProvider: Lazy<MangaRepository>
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY

        }
        return OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(sharedPreferences, repositoryProvider))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideMangadexApi(client: OkHttpClient): MangadexApi {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(MangadexApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
}