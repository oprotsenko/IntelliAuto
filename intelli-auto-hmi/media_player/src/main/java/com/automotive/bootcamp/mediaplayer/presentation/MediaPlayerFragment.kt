package com.automotive.bootcamp.mediaplayer.presentation

import android.annotation.SuppressLint
import android.content.ContentUris
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.automotive.bootcamp.common.base.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerFragment() :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {
    private val viewModel: MediaPlayerViewModel by viewModel()
    private val mediaPlayerAdapter: MediaPlayerRecyclerViewAdapter by lazy { MediaPlayerRecyclerViewAdapter() }

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
            MediaStore.Audio.Media.DURATION
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
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol)
                val artist = cursor.getString(artistCol)
                val duration = cursor.getString(durationCol)
                val image = ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, id).toString()
                viewModel.albumsListData.value?.add(MediaAlbum(id, image, title,artist,duration))
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
}