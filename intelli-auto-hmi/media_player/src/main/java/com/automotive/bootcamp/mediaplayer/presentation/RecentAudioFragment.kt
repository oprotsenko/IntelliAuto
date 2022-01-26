package com.automotive.bootcamp.mediaplayer.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.AutoFitGridLayoutManager
import com.automotive.bootcamp.common.utils.GRID_RECYCLE_COLUMN_WIDTH
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentAudiosListBinding
import com.automotive.bootcamp.mediaplayer.presentation.adapters.AudioRecyclerViewAdapter
import com.automotive.bootcamp.mediaplayer.viewModels.RecentAudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecentAudioFragment:
    BaseFragment<FragmentAudiosListBinding>(FragmentAudiosListBinding::inflate),
    MediaItemClickListener, OnItemClickListener {

    private val viewModel: RecentAudioViewModel by viewModel()
    private val audioAdapter: AudioRecyclerViewAdapter by lazy {
        AudioRecyclerViewAdapter(
            onMediaItemClickListener = this,
            onItemClickListener = this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun setObservers() {
        viewModel.recentAudioData.observe(viewLifecycleOwner) {
            audioAdapter.submitList(it)
        }
    }

    override fun onMediaClick(position: Int) {
        playAudio(position)
    }

    override fun onItemClick(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.apply {
            inflate(R.menu.audio_popup_menu)
            if (viewModel.recentAudioData.value?.get(position)?.isRecent == false) {
                menu.findItem(R.id.audioRemoveRecent).apply {
                    isVisible = false
                }
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.audioPlay -> {
                        playAudio(position)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.audioAddToPlaylist -> {
                        return@setOnMenuItemClickListener false
                    }
                    R.id.audioRemoveRecent -> {
                        viewModel.removeFromRecent(position)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.audioAddRemoveFavourite -> {
                        viewModel.setIsFavourite(position)
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }
            show()
        }
    }

    private fun playAudio(position: Int) {
        val playlist = viewModel.getAudioList()
        if (playlist != null) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fullScreenContainer,
                    NowPlayingFragment.newInstance(playlist, position)
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), GRID_RECYCLE_COLUMN_WIDTH)
            adapter = audioAdapter
            itemAnimator?.changeDuration = 0
        }
    }
}