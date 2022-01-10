package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.MediaPlayerFragmentBinding
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import kotlinx.android.synthetic.main.media_player_fragment.*

class MediaPlayerFragment : BaseFragment(Fra) {

    private lateinit var viewModel: MediaPlayerViewModel
    private lateinit var mediaPlayerAdapter: MediaPlayerRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.media_player_fragment, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        viewModel = ViewModelProvider(this)[MediaPlayerViewModel::class.java]
        viewModel.albumsList.observe(viewLifecycleOwner) {
            mediaPlayerAdapter.submitList(it)
        }

        initRecyclerView()

        viewModel.getAlbums()
    }

    private fun initRecyclerView(){
        mediaPlayerAdapter = MediaPlayerRecyclerViewAdapter(requireContext())
        rvAlbums.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = mediaPlayerAdapter
        }
    }
}