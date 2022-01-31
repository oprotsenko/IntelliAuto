package com.automotive.bootcamp.mediaplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.common.extensions.loadImage
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
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(ItemPlaylistBinding.bind(view))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        with(holder) {
            bind(getItem(bindingAdapterPosition))
            binding.apply {
                root.setOnClickListener {
                    onMediaItemClickListener.onMediaClick(bindingAdapterPosition)
                }
                popupMenu.setOnClickListener {
                    onItemClickListener.onItemClick(popupMenu, bindingAdapterPosition)
                }
            }
        }
    }

    class PlaylistViewHolder(val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val popupMenu = binding.ibPopUpMenu

        fun bind(playlist: PlaylistWrapper) {
            binding.apply {
                if (!playlist.playlist.list.isNullOrEmpty()) {
                    playlist.playlist.list[0].cover?.let { ivFirstPlaceholder.loadImage(it) }
                    if (playlist.playlist.list.size > 1){
                        playlist.playlist.list[1].cover?.let { ivSecondPlaceholder.loadImage(it) }
                    }
                    if (playlist.playlist.list.size > 2){
                        playlist.playlist.list[2].cover?.let { ivThirdPlaceholder.loadImage(it) }
                    }
                    if (playlist.playlist.list.size > 3){
                        playlist.playlist.list[3].cover?.let { ivForthPlaceholder.loadImage(it) }
                    }
                }
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