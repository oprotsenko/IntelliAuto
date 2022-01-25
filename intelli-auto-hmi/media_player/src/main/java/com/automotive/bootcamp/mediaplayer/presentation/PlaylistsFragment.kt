package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.utils.GRID_RECYCLE_COLUMN_WIDTH
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentAudiosListBinding
import com.automotive.bootcamp.mediaplayer.presentation.adapters.PlaylistRecyclerViewAdapter
import com.automotive.bootcamp.mediaplayer.viewModels.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment :
    BaseFragment<FragmentAudiosListBinding>(FragmentAudiosListBinding::inflate),
    MediaItemClickListener, OnItemClickListener {

    private val viewModel: PlaylistsViewModel by viewModel()
    private val audioAdapter: PlaylistRecyclerViewAdapter by lazy {
        PlaylistRecyclerViewAdapter(
            onMediaItemClickListener = this,
            onItemClickListener = this,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun setObservers() {
        viewModel.playlistsData.observe(viewLifecycleOwner) {
            audioAdapter.submitList(it)
        }
    }

    override fun onMediaClick(position: Int) {
        openPlaylist(position)
    }

    override fun onItemClick(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.apply {
            inflate(R.menu.playlist_popup_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.playlistPlay -> {
                        return@setOnMenuItemClickListener false
                    }
                    R.id.playlistRemovePlaylist -> {
                        return@setOnMenuItemClickListener false
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
            show()
        }
    }

    private fun openPlaylist(position: Int) {
        //todo
    }

    private fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), GRID_RECYCLE_COLUMN_WIDTH)
            adapter = audioAdapter
            itemAnimator?.changeDuration = 0
        }
    }
}