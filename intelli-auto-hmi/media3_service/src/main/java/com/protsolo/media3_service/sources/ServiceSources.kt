package com.protsolo.media3_service.sources

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaMetadata
import com.protsolo.media3_service.LOCAL_ROOT_ID
import com.protsolo.media3_service.sources.models.AudioItem
import com.protsolo.media3_service.utils.AlbumArtContentProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ServiceSources(context: Context) : AbstractMusicSource() {

    private val localRepository = LocalAudioSource(context)
//    private val remoteSource: RemoteAudioSource by inject()
//    private val cacheRepository: CacheMediaRepository by inject()

    private val sourcesJob = Job()
    private val sourcesScope = CoroutineScope(Dispatchers.Main + sourcesJob)

//    private val allPlaylistsFlow = cacheRepository.getAllPlaylists()
//    private var allPlaylists: List<PlaylistItem>? = null

    val sourceList = mutableListOf<MediaMetadata>()

    init {
        Log.d("serviceTAG", "state initializing ")
        state = State.INITIALIZING

//        sourcesScope.launch {
//            allPlaylistsFlow.collect {
//                allPlaylists = it
//                Log.d("allPlaylists", "allPlaylists collect " + it?.size)
//            }
//        }
    }

    override fun iterator(): Iterator<MediaMetadata> = sourceList.iterator()

    override suspend fun load() {
        retrieveLocalAudio()
        Log.d("serviceTAG", "local done")
        retrieveRemoteAudio()
        Log.d("serviceTAG", "remote done")
        retrieveCacheAudio()
        Log.d("serviceTAG", "cache done")
        state = State.INITIALIZED
    }

    private suspend fun retrieveCacheAudio() {
//        Log.d("serviceTAG", "playlists loaded -> ${allPlaylists?.size}")
//        val list = allPlaylists.mapToMediaMetadataCompat()
//        list.forEach {
//            Log.d("serviceTAGA", "playlist " + it.getString(METADATA_KEY_MEDIA_ID))
//            sourceList.add(it)
//        }
    }

    private suspend fun retrieveRemoteAudio() {
//        val audios = remoteSource.load()
//        audios?.let { cacheRepository.insertAudios(audios) }
//        val metadataCompat = audios?.mapToMediaMetadataCompat(REMOTE_ROOT_ID, true)
//        metadataCompat?.forEach {
//            sourceList.add(it)
//        }
    }

    private suspend fun retrieveLocalAudio() {
        val audios = localRepository.retrieveLocalAudio()
//        cacheRepository.insertAudios(audios)
        val metadataCompat = audios.mapToMediaMetadata(LOCAL_ROOT_ID)
        metadataCompat.forEach {
            sourceList.add(it)
        }
    }

    private suspend fun List<AudioItem>.mapToMediaMetadata(
        rootId: String,
        isRemote: Boolean = false
    ): List<MediaMetadata> =
        this.map { audio ->
//            val rating = cacheRepository.isFavourite(audio.id)
//            Log.d("rating", "${audio.id} ->>> $rating")
            val imageUri = AlbumArtContentProvider.mapUri(Uri.parse(audio.cover), isRemote)
            MediaMetadata.Builder()
                .setAlbumArtist(audio.artist)
                .setAlbumTitle(audio.title)
                .setArtworkUri(imageUri)
                .setMediaUri(Uri.parse(audio.url))
                .setIsPlayable(true)
                .setCompilation(audio.id.toString())
                .setGenre(rootId)
//                .putString(METADATA_KEY_MEDIA_ID, audio.id.toString())
//                .putString(METADATA_KEY_ARTIST, audio.artist)
//                .putString(METADATA_KEY_TITLE, audio.title)
//                .putString(METADATA_KEY_ART_URI, imageUri.toString())
//                .putString(METADATA_KEY_MEDIA_URI, audio.url)
//                .putString(METADATA_KEY_DISPLAY_TITLE, audio.title)
//                .putString(METADATA_KEY_DISPLAY_SUBTITLE, audio.artist)
//                .putString(METADATA_KEY_DISPLAY_ICON_URI, imageUri.toString())
//                .putLong(METADATA_KEY_FLAGS, MediaBrowser.MediaItem.FLAG_PLAYABLE.toLong())
//                .putString(METADATA_KEY_ALBUM, rootId)
//                .putRating(METADATA_KEY_RATING, RatingCompat.newHeartRating(rating))
                .build()
        }

//    private suspend fun List<PlaylistItem>?.mapToMediaMetadataCompat(): List<MediaMetadataCompat> {
//        Log.d("serviceTAG", "map playlist")
//        val mediaMetadata = mutableListOf<MediaMetadataCompat>()
//        this?.map { playlistItem ->
//            mediaMetadata.add(playlistItem.mapPlaylistToMediaMetadataCompat())
//        }
//        this?.forEach { playlistItem ->
//            val metadataList = playlistItem.list?.mapToMediaMetadataCompat(playlistItem.name)
//            metadataList?.forEach {
//                mediaMetadata.add(it)
//            }
//        }
//        return mediaMetadata
//    }

//    private fun PlaylistItem.mapPlaylistToMediaMetadataCompat(): MediaMetadataCompat =
//        Builder()
//            .putString(METADATA_KEY_MEDIA_ID, this.name)
//            .putString(METADATA_KEY_TITLE, this.name)
//            .putString(METADATA_KEY_ALBUM, ALBUMS_ROOT_ID)
//            .putLong(METADATA_KEY_FLAGS, MediaBrowser.MediaItem.FLAG_BROWSABLE.toLong())
//            .build()
//
//    suspend fun addToRecent(aid: Long) {
//        cacheRepository.addToRecent(aid)
//    }
//
//    suspend fun addToFavourite(aid: Long) {
//        cacheRepository.addToFavourite(aid)
//    }
}