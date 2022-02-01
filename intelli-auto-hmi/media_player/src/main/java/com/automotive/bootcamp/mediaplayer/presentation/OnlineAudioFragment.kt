package com.automotive.bootcamp.mediaplayer.presentation

import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.asLiveData
import com.automotive.bootcamp.common.base.BaseFragment
import com.automotive.bootcamp.common.utils.AutoFitGridLayoutManager
import com.automotive.bootcamp.mediaplayer.utils.FRAGMENT_RESULT_KEY
import com.automotive.bootcamp.common.utils.GRID_RECYCLE_COLUMN_WIDTH
import com.automotive.bootcamp.mediaplayer.utils.PLAYLIST_NAME_KEY
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.FragmentAudiosListBinding
import com.automotive.bootcamp.mediaplayer.presentation.adapters.AudioRecyclerViewAdapter
import com.automotive.bootcamp.mediaplayer.viewModels.OnlineAudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnlineAudioFragment :
    BaseFragment<FragmentAudiosListBinding>(FragmentAudiosListBinding::inflate),
    MediaItemClickListener, OnItemClickListener {

    private val viewModel: OnlineAudioViewModel by viewModel()
    private val audioAdapter: AudioRecyclerViewAdapter by lazy {
        AudioRecyclerViewAdapter(
            onMediaItemClickListener = this,
            onItemClickListener = this
        )
    }

    override fun initRecyclerView() {
        binding.rvAlbums.apply {
            layoutManager = AutoFitGridLayoutManager(requireContext(), GRID_RECYCLE_COLUMN_WIDTH)
            adapter = audioAdapter
            itemAnimator?.changeDuration = 0
        }
    }

    override fun initView() {
        binding.tvSelectedPlaylistName.text = resources.getString(R.string.online_music)
    }

    override fun setObservers() {
        viewModel.apply {
            onlineAudioData.observe(viewLifecycleOwner) {
                audioAdapter.submitList(it)
            }
            parentFragmentManager.setFragmentResultListener(
                FRAGMENT_RESULT_KEY, viewLifecycleOwner, { _, bundle ->
                    val playlistName = bundle.getString(PLAYLIST_NAME_KEY)
                    playlistName?.let { viewModel.apply {
                        createPlaylist(playlistName, dynamicallyAddAudioPosition) }
                    }
                })

        }
    }

    override fun onMediaClick(position: Int) {
        playAudio(position)
    }

    override fun onItemClick(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.apply {
            inflate(R.menu.audio_popup_menu)
            if (viewModel.onlineAudioData.value?.get(position)?.isRecent == false) {
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
                        viewModel.playlists?.let { playlists ->
                            for (i in playlists.indices) {
                                menu.findItem(R.id.audioAddToPlaylist).subMenu.add(
                                    R.id.audioAddToPlaylist,
                                    playlists[i].playlist.id.toInt(),
                                    i,
                                    playlists[i].playlistName
                                ).setOnMenuItemClickListener submenu@{
                                    viewModel.addToPlaylist(playlists[i].playlist.id, position)
                                    return@submenu true
                                }
                                show()
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }
                    R.id.audioCreatePlaylist -> {
                        viewModel.dynamicallyAddAudioPosition = position
                        val enterNameDialog = EnterNameDialog()
                        enterNameDialog.show(
                            parentFragmentManager, null
                        )
                        return@setOnMenuItemClickListener true
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
}