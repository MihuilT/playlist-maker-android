package com.myapp.playlistmaker

import android.app.Application
import com.myapp.playlistmaker.di.appDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlaylistMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlaylistMakerApplication)
            modules(appDiModule)
        }
    }
}