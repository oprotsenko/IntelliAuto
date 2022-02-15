package com.automotive.bootcamp.music_service.data.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.automotive.bootcamp.music_service.data.cache.room.dao.AudioDao
import com.automotive.bootcamp.music_service.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.music_service.data.cache.room.entities.EmbeddedPlaylistEntity
import com.automotive.bootcamp.music_service.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.music_service.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider.DATABASE_NAME

@Database(
    entities = [
        AudioEntity::class,
        PlaylistEntity::class,
        EmbeddedPlaylistEntity::class,
        AudioPlaylistCrossRefEntity::class
    ],
    version = 1
)
abstract class RoomAudioDatabase : RoomDatabase() {
    abstract val audioDao: AudioDao

    companion object {
        @Volatile
        private var INSTANCE: RoomAudioDatabase? = null

        fun getInstance(context: Context): RoomAudioDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RoomAudioDatabase::class.java,
                    DATABASE_NAME
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}