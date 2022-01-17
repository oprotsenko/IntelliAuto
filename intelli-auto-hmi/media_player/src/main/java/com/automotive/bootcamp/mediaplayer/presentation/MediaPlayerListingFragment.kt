package com.automotive.bootcamp.mediaplayer.presentation

import android.content.ContentUris
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.automotive.bootcamp.common.base.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerListingBinding
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerListingFragment :
    BaseFragment<FragmentMediaPlayerListingBinding>(FragmentMediaPlayerListingBinding::inflate),
    MediaItemClickListener {

    private val viewModel: MediaPlayerListingViewModel by viewModel()
    private val mediaPlayerAdapter: MediaPlayerRecyclerViewAdapter by lazy {
        MediaPlayerRecyclerViewAdapter(
            onMediaItemClickListener = this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSong()
        initRecyclerView()
    }

    private fun loadSong() {
        val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )
        requireActivity().contentResolver.query(
            collection,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol)
                val artist = cursor.getString(artistCol)
                val duration = cursor.getString(durationCol)
                val url = cursor.getString(dataCol)
//                val url =
//                    ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, id)
//                        .toString()
                Log.d("URL", url)
                viewModel.albumsListData.value?.add(MediaAlbum(id, null, title, artist, duration, url))
            }
        }
//        val uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
//        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
//        val cursor = requireActivity().contentResolver.query(uri, null, selection, null, null)
//        if (cursor != null) {
////                val artImage = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.))
//            val songTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
//            val singerName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
//            val songDuration =
//                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
//            val song = MediaAlbum("0", null, songTitle, singerName, songDuration)
//            viewModel.albumsListData.value?.add(song)
//        }
    }

    override fun setObservers() {
        viewModel.albumsListData.observe(viewLifecycleOwner) { it ->
            mediaPlayerAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), 200)
//          GridLayoutManager( requireContext(), 3, GridLayoutManager.VERTICAL,false)
            adapter = mediaPlayerAdapter
        }
    }

//    override fun setListeners() {
//        binding.apply {
//            ibNowPlayingPlayPause.setOnClickListener {
//                if (ibNowPlayingPlayPause.drawable == ContextCompat.getDrawable(requireContext(), R.drawable.ic_play,)) {
//                    mediaPlayer = MediaPlayer()
//                    try {
//                        mediaPlayer?.setDataSource()
//                    }
//                }
//            }
//        }
//    }

    var player: MediaPlayer? = null

    override fun onMediaClickListener(position: Int) {
        val media = viewModel.albumsListData.value?.get(position)

        if (player == null) {
            player = MediaPlayer()
        }
        player?.setDataSource(media?.songURL)
        player?.prepare()
        player?.start()

        this.requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mediaServiceContent, NowPlayingFragment(media)).addToBackStack(null)
            .commit()
    }
}