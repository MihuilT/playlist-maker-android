package com.myapp.playlistmaker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapp.playlistmaker.data.database.dao.PlaylistDao
import com.myapp.playlistmaker.data.database.dao.ProfileDao
import com.myapp.playlistmaker.data.database.dao.TrackDao
import com.myapp.playlistmaker.data.database.entity.PlaylistEntity
import com.myapp.playlistmaker.data.database.entity.ProfileEntity
import com.myapp.playlistmaker.data.database.entity.TrackEntity
import com.myapp.playlistmaker.data.database.entity.TrackFtsEntity

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        ProfileEntity::class,
        TrackFtsEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class PlaylistMakerDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: PlaylistMakerDatabase? = null

        fun getInstance(context: Context): PlaylistMakerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaylistMakerDatabase::class.java,
                    "playlist_maker_db"
                )
                    // учебный проект: при изменении схемы база создается заново
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
