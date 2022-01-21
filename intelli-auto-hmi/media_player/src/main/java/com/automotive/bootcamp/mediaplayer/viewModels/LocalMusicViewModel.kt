package com.automotive.bootcamp.mediaplayer.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.automotive.bootcamp.common.base.CoroutineViewModel
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.domain.models.SongWrapper
import com.automotive.bootcamp.mediaplayer.domain.useCases.GetLocalMusic
import kotlinx.coroutines.launch

class LocalMusicViewModel(private val getLocalMusic: GetLocalMusic) : CoroutineViewModel() {

    val localMusicData by lazy { MutableLiveData<List<SongWrapper>>() }

    init {
        viewModelScope.launch {
            val songs = getLocalMusic.getLocalSongs()
            localMusicData.value = songs.map { songItem ->
                SongWrapper(
                    song = Song(
                        id = songItem.id,
                        cover = songItem.cover,
                        title = songItem.title,
                        artist = songItem.artist,
                        duration = songItem.duration,
                        songURL = songItem.url
                    )
                )
            }

//            val audioEntity = AudioEntity(title = "title", artist =  "artist", duration =  "3", songURL =  "songUrl")
//
//            val audiosList = listOf(
//                AudioEntity(title = "title1", artist =  "artist1", duration =  "3", songURL =  "songUrl1"),
//                AudioEntity(title = "title2", artist =  "artist2", duration =  "4", songURL =  "songUrl2"),
//                AudioEntity(title = "title3", artist =  "artist3", duration =  "5", songURL =  "songUrl3"),
//                AudioEntity(title = "title4", artist =  "artist4", duration =  "6", songURL =  "songUrl4")
//            )
//
//            getLocalMusic.insertAudio(audioEntity)
//            getLocalMusic.insertAudios(audiosList)
//
//            val playlistEntity1 = PlaylistEntity(name = "name2", type = "custom")
//            val playlistEntity2 = PlaylistEntity(name = "name3", type = "custom")
//            val playlistEntity3 = PlaylistEntity(name = "name4", type = "online")
//
//            getLocalMusic.insertPlaylist(playlistEntity1)
//            getLocalMusic.insertPlaylist(playlistEntity2)
//            getLocalMusic.insertPlaylist(playlistEntity3)
//
//            val crossRefEntity1 = AudioPlaylistCrossRef(2, 5)
//            val crossRefEntity2 = AudioPlaylistCrossRef(3, 5)
//            val crossRefEntity3 = AudioPlaylistCrossRef(12, 6)
//            getLocalMusic.insertAudioPlaylistCrossRef(crossRefEntity1)
//            getLocalMusic.insertAudioPlaylistCrossRef(crossRefEntity2)
//            getLocalMusic.insertAudioPlaylistCrossRef(crossRefEntity3)

//            getLocalMusic.deletePlaylist(4)
//            getLocalMusic.deleteAudioFromPlaylist(7, 2)

//            val result = getLocalMusic.getPlaylistsWithAudiosByType("online")

//            val result = getLocalMusic.getPlaylistWithAudiosById(5)

//            val result = getLocalMusic.getAllPlaylistsWithAudios()
//
//            Log.d("LocalMusicViewModel", result.toString())
        }
    }
}