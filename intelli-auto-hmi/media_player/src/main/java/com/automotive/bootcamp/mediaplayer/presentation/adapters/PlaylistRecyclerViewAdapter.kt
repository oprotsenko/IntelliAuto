package com.automotive.bootcamp.mediaplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.ItemPlaylistBinding
import com.automotive.bootcamp.mediaplayer.presentation.MediaItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.OnItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper

class PlaylistRecyclerViewAdapter(
    private val onMediaItemClickListener: MediaItemClickListener,
    private val onItemClickListener: OnItemClickListener,
) :
    ListAdapter<PlaylistWrapper, PlaylistRecyclerViewAdapter.PlaylistViewHolder>(
        PlaylistDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            ItemPlaylistBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_playlist, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
            root.setOnClickListener {
                onMediaItemClickListener.onMediaClick(position)
            }
            popupMenu.setOnClickListener {
                onItemClickListener.onItemClick(popupMenu, position)
            }
        }
    }

    class PlaylistViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val root = binding.root
        val popupMenu = binding.ibPopUpMenu

        fun bind(playlist: PlaylistWrapper) {
            binding.apply {
//                for (i in 4) {
//                    if (!playlist.playlist.list.isNullOrEmpty()) {
//                        ivFirstPlaceholder.setImageBitmap(playlist.playlist.list[i].cover)
//                    }
//                    i++
//                }
                tvSongTitle.text = playlist.playlistName
            }
        }
    }

    class PlaylistDiffCallBack : DiffUtil.ItemCallback<PlaylistWrapper>() {
        override fun areItemsTheSame(
            oldItem: PlaylistWrapper,
            newItem: PlaylistWrapper
        ): Boolean =
            oldItem.playlist.id == newItem.playlist.id

        override fun areContentsTheSame(
            oldItem: PlaylistWrapper,
            newItem: PlaylistWrapper
        ): Boolean =
            oldItem == newItem
    }
}