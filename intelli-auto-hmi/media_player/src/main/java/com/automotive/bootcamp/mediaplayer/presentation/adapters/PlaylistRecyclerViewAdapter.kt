package com.automotive.bootcamp.mediaplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.ItemAddPlaylistBinding
import com.automotive.bootcamp.mediaplayer.databinding.ItemPlaylistBinding
import com.automotive.bootcamp.mediaplayer.presentation.MediaItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.OnAddItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.OnItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.models.PlaylistWrapper
import com.automotive.bootcamp.mediaplayer.utils.enums.PlaylistItemType

class PlaylistRecyclerViewAdapter(
    private val onMediaItemClickListener: MediaItemClickListener,
    private val onItemClickListener: OnItemClickListener,
    private val onAddItemClickListener: OnAddItemClickListener,
) :
    ListAdapter<PlaylistWrapper, RecyclerView.ViewHolder>(
        PlaylistDiffCallBack()
    ) {

    private var itemsAmount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        itemsAmount = itemCount
        return when (viewType) {
            PlaylistItemType.TYPE_PLAYLIST_ITEM.ordinal -> {
                PlaylistViewHolder(
                    ItemPlaylistBinding.bind(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.item_playlist, parent, false
                        )
                    )
                )
            }
            else -> {
                AddPlaylistViewHolder(
                    ItemAddPlaylistBinding.bind(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.item_add_playlist, parent, false
                        )
                    ), onAddItemClickListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        itemsAmount = itemCount - 1
        when (holder) {
            is PlaylistViewHolder -> {
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
            is AddPlaylistViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemsAmount) {
            PlaylistItemType.TYPE_ADD_PLAYLIST_ITEM.ordinal
        } else {
            PlaylistItemType.TYPE_PLAYLIST_ITEM.ordinal
        }
    }

    class PlaylistViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val root = binding.root
        val popupMenu = binding.ibPopUpMenu

        fun bind(playlist: PlaylistWrapper) {
            binding.apply {
                var i = 0
//                while (i < 4) {
//                    if (!playlist.playlist.list.isNullOrEmpty() && playlist.playlist.list[i].cover != null) {
//                        ivFirstPlaceholder.setImageBitmap(playlist.playlist.list[i].cover)
//                    }
//                    i++
//                }
                tvSongTitle.text = playlist.playlistName
            }
        }
    }

    class AddPlaylistViewHolder(
        private val binding: ItemAddPlaylistBinding,
        private val onAddItemClickListener: OnAddItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener { onAddItemClickListener.onAddItemClick() }
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