package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.extensions.hideKeyboard
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
        binding.layoutAddCustomPlaylist.visibility = View.VISIBLE
    }

    override fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), GRID_RECYCLE_COLUMN_WIDTH)
            adapter = audioAdapter
            itemAnimator?.changeDuration = 0
        }
    }

    override fun setObservers() {
        viewModel.apply {
            playlistsData.observe(viewLifecycleOwner) {
                audioAdapter.submitList(it)
            }

            selectedPlaylist.observe(viewLifecycleOwner) {
                parentFragmentManager.beginTransaction().replace(
                    R.id.mediaPlayerServiceContainer,
                    CustomPlaylistFragment.newInstance(it)
                ).commit()
            }
        }
    }

    override fun setListeners() {
        binding.apply {
            root.setOnClickListener { root.hideKeyboard() }

            bUndoCreateCustomPlaylist.setOnClickListener {
                bUndoCreateCustomPlaylist.visibility = View.GONE
                tilCustomPlaylistName.visibility = View.GONE
                bCreateCustomPlaylist.visibility = View.GONE
                bAddCustomPlaylist.visibility = View.VISIBLE
            }
            bAddCustomPlaylist.setOnClickListener {
                bUndoCreateCustomPlaylist.visibility = View.VISIBLE
                tilCustomPlaylistName.visibility = View.VISIBLE
                bCreateCustomPlaylist.visibility = View.VISIBLE
                bAddCustomPlaylist.visibility = View.GONE
                bCreateCustomPlaylist.setOnClickListener {
                    val playlistName = etCustomPlaylistName.text.toString()
                    viewModel.createPlaylist(playlistName)
                    bUndoCreateCustomPlaylist.visibility = View.GONE
                    tilCustomPlaylistName.visibility = View.GONE
                    bCreateCustomPlaylist.visibility = View.GONE
                    bAddCustomPlaylist.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onMediaClick(position: Int) {
        viewModel.openPlaylist(position)
    }

    override fun onItemClick(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.apply {
            inflate(R.menu.playlist_popup_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.playlistPlay -> {
                        viewModel.openPlaylist(position)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.playlistRemovePlaylist -> {
                        viewModel.removePlaylist(position)
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
}