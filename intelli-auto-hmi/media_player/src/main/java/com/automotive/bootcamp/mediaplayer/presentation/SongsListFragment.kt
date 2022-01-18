package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.utils.GRID_RECYCLE_COLUMN_WIDTH
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentSongsListBinding
import com.automotive.bootcamp.mediaplayer.viewModels.SongsListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SongsListFragment :
    BaseFragment<FragmentSongsListBinding>(FragmentSongsListBinding::inflate),
    MediaItemClickListener {

    private val sharedViewModel by sharedViewModel<SongsListViewModel>()
    private val songsAdapter: SongsRecyclerViewAdapter by lazy {
        SongsRecyclerViewAdapter(
            onMediaItemClickListener = this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun setObservers() {
        sharedViewModel.albumsListData.observe(viewLifecycleOwner) { it ->
            songsAdapter.submitList(it)
        }
    }

    override fun onMediaClickListener(position: Int) {
        sharedViewModel.select(position)

        this.requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.launchMediaPlayer, NowPlayingFragment()).addToBackStack(null)
            .commit()
    }

    private fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), GRID_RECYCLE_COLUMN_WIDTH)
            adapter = songsAdapter
        }
    }
}