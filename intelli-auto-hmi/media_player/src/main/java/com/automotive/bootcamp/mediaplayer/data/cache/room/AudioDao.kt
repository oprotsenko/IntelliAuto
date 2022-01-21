package com.automotive.bootcamp.mediaplayer.data.cache.room

import androidx.room.*
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRef
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios

@Dao
interface AudioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(audio: AudioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioPlaylistCrossRef(crossRef: AudioPlaylistCrossRef)

    @Query("DELETE FROM playlists WHERE pid = :pid")
    suspend fun deletePlaylist(pid: Int)

    @Query("DELETE FROM audio_playlist_cross_ref WHERE aid = :aid AND pid = :pid")
    suspend fun deleteAudioFromPlaylist(aid:Int, pid: Int)

    @Transaction
    @Query("SELECT * FROM playlists WHERE pid = :pid")
    suspend fun getAudiosByPlaylistId(pid: Int): List<PlaylistWithAudios>


}