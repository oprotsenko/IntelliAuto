package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.utils.GRID_RECYCLE_COLUMN_WIDTH
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentAudiosListBinding
import com.automotive.bootcamp.mediaplayer.viewModels.LocalMusicViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalMusicFragment :
    BaseFragment<FragmentAudiosListBinding>(FragmentAudiosListBinding::inflate),
    MediaItemClickListener {

    private val viewModel: LocalMusicViewModel by viewModel()
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
        viewModel.localMusicData.observe(viewLifecycleOwner) {
            songsAdapter.submitList(it)
        }
    }

    override fun onMediaClickListener(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fullScreenContainer, NowPlayingFragment.newInstance(position))
            .addToBackStack(null)
            .commit()
    }

    private fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), GRID_RECYCLE_COLUMN_WIDTH)
            adapter = songsAdapter
        }
    }
}