package ru.frozenpriest.simplemangareader

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class SimpleMangaApp: Application() {
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        super.onCreate()
    }
}