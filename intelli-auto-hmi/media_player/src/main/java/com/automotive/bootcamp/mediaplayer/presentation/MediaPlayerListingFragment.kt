package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import com.automotive.bootcamp.common.base.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentMediaPlayerListingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaPlayerListingFragment:
    BaseFragment<FragmentMediaPlayerListingBinding>(FragmentMediaPlayerListingBinding::inflate),
    MediaItemClickListener {

    private val listingViewModel: MediaPlayerListingViewModel by viewModel()
    private val mediaPlayerAdapter: MediaPlayerRecyclerViewAdapter by lazy {
        MediaPlayerRecyclerViewAdapter(
            onMediaItemClickListener = this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    override fun setObservers() {
        listingViewModel.albumsListData.observe(viewLifecycleOwner) { it ->
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

    override fun onMediaClickListener(position: Int) {
        val media = listingViewModel.albumsListData.value?.get(position)

        this.requireActivity().
            supportFragmentManager.
            beginTransaction().
            replace(R.id.mediaServiceContent, NowPlayingFragment(media)).
            addToBackStack(null).
            commit()
    }
}