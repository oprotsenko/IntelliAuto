package com.automotive.bootcamp.mediaplayer.data.cache.room.dao

import androidx.room.*
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios

@Dao
interface AudioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(audio: AudioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudios(audios: List<AudioEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioPlaylistCrossRef(crossRefEntity: AudioPlaylistCrossRefEntity)

    @Delete
    suspend fun deleteAudioFromPlaylist(crossRefEntity: AudioPlaylistCrossRefEntity)

    @Query("DELETE FROM playlists WHERE pid = :pid")
    suspend fun deletePlaylist(pid: Long)

    @Query("SELECT EXISTS (SELECT 1 FROM playlists WHERE pid = :pid)")
    suspend fun playlistExists(pid: Long): Boolean

    @Transaction
    @Query("SELECT * FROM playlists WHERE pid = :pid")
    suspend fun getPlaylistWithAudiosById(pid: Long): List<PlaylistWithAudios>

    @Transaction
    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylistsWithAudios(): List<PlaylistWithAudios>
}