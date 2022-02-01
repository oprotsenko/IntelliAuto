package com.automotive.bootcamp.mediaplayer.data.cache.room.dao

import androidx.room.*
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.AudioEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.PlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.EmbeddedPlaylistEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.AudioPlaylistCrossRefEntity
import com.automotive.bootcamp.mediaplayer.data.cache.room.entities.relations.PlaylistWithAudios
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudio(audio: AudioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudios(audios: List<AudioEntity>): List<Long>

    @Query("SELECT EXISTS (SELECT 1 FROM playlists WHERE pid = :pid)")
    suspend fun playlistExists(pid: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM audio_playlist_cross_ref WHERE pid = :pid AND aid = :aid)")
    suspend fun playlistHasAudio(pid: Long, aid: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioToPlaylist(crossRefEntity: AudioPlaylistCrossRefEntity)

    @Delete
    suspend fun deleteAudioFromPlaylist(crossRefEntity: AudioPlaylistCrossRefEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmbeddedPlaylist(playlist: EmbeddedPlaylistEntity)

    @Transaction
    @Query("SELECT * FROM embedded_playlists WHERE name = :name")
    suspend fun getEmbeddedPlaylist(name: String): EmbeddedPlaylistEntity?

    @Query("DELETE FROM playlists WHERE pid = :pid")
    suspend fun deletePlaylist(pid: Long)

    @Transaction
    @Query("SELECT COUNT(*) FROM audio_playlist_cross_ref WHERE pid = :pid")
    suspend fun getPlaylistSize(pid: Long): Int

    @Transaction
    @Query("SELECT * FROM playlists WHERE pid = :pid")
    fun getPlaylistWithAudios(pid: Long): Flow<PlaylistWithAudios?>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsWithAudios(): Flow<List<PlaylistWithAudios>?>
}