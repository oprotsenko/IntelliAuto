package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import com.automotive.bootcamp.common.base.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerFragment :
    BaseFragment<FragmentMediaPlayerBinding>(FragmentMediaPlayerBinding::inflate) {
    private val viewModel: MediaPlayerViewModel by viewModel()
    private val mediaPlayerAdapter: MediaPlayerRecyclerViewAdapter by lazy { MediaPlayerRecyclerViewAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
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