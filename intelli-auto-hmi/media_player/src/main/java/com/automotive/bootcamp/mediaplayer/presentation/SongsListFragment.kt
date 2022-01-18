package com.automotive.bootcamp.mediaplayer.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.utils.GRID_RECYCLE_COLUMN_WIDTH
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentSongsListBinding
import com.automotive.bootcamp.mediaplayer.viewModels.SongsListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SongsListFragment :
    BaseFragment<FragmentSongsListBinding>(FragmentSongsListBinding::inflate),
    MediaItemClickListener {

    private val viewModel: SongsListViewModel by viewModel()
    private val songsAdapter: SongsRecyclerViewAdapter by lazy {
        SongsRecyclerViewAdapter(
            onMediaItemClickListener = this
        )
    }

    var player: MediaPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun setObservers() {
        viewModel.albumsListData.observe(viewLifecycleOwner) { it ->
            songsAdapter.submitList(it)
        }
    }

    override fun onMediaClickListener(position: Int) {
        val media = viewModel.albumsListData.value?.get(position)

        if (player == null) {
            player = MediaPlayer()
        }
        player?.setDataSource(media?.songURL)
        player?.prepare()
        player?.start()

        this.requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.launchMediaPlayer, NowPlayingFragment(media)).addToBackStack(null)
            .commit()
    }

    private fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), GRID_RECYCLE_COLUMN_WIDTH)
            adapter = songsAdapter
        }
    }
}