package com.automotive.bootcamp.mediaplayer.data.cache.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.automotive.bootcamp.common.utils.DATABASE_NAME
import com.automotive.bootcamp.common.utils.DATABASE_VERSION
import com.automotive.bootcamp.mediaplayer.data.cache.room.dao.AudioDao
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.automotive.bootcamp.mediaplayer.utils.ImageBitmapString

@Database(
    entities = [
        AudioEntity::class,
        PlaylistEntity::class,
        AudioPlaylistCrossRefEntity::class
    ],
    version = DATABASE_VERSION
)
@TypeConverters(ImageBitmapString::class)
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