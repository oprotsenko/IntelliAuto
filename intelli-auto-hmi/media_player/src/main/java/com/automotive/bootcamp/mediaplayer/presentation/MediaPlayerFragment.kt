package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.AutoFitGridLayoutManager
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.viewModels.MediaPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate),
    MediaItemClickListener {

    private val mediaPlayerViewModel by sharedViewModel<MediaPlayerViewModel>()
    private val mediaPlayerAdapter by lazy {
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
        val collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_INTERNAL)
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
                mediaPlayerViewModel.albumsListData.value?.add(Song(id, null, title, artist, duration, url))
            }
        }
    }

    override fun setObservers() {
        mediaPlayerViewModel.albumsListData.observe(viewLifecycleOwner) {
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

    override fun onMediaClickListener(position: Int) {
        mediaPlayerViewModel.select(position)

        this.requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.launchMediaPlayer, NowPlayingFragment()).addToBackStack(null)
            .commit()
    }
}