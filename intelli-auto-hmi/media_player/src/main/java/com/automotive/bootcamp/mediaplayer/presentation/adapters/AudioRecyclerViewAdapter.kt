package com.automotive.bootcamp.mediaplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.common.extensions.loadImage
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.ItemAudioBinding
import com.automotive.bootcamp.mediaplayer.presentation.MediaItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.OnItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AudioRecyclerViewAdapter(
    private val onMediaItemClickListener: MediaItemClickListener,
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<AudioWrapper, AudioRecyclerViewAdapter.AudioViewHolder>(AudioDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_audio,
            parent,
            false
        )
        return AudioViewHolder(ItemAudioBinding.bind(view))
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        with(holder) {
            bind(getItem(bindingAdapterPosition))
            binding.root.setOnClickListener {
                onMediaItemClickListener.onMediaClick(bindingAdapterPosition)
            }
            popupMenu.setOnClickListener {
                onItemClickListener.onItemClick(popupMenu, bindingAdapterPosition)
            }
        }
    }

    class AudioViewHolder(val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val popupMenu = binding.ibPopUpMenu

        fun bind(album: AudioWrapper) {
            binding.apply {
                album.audio.cover?.let { url ->
                    ivAlbumArt.loadImage(url)
                }
                tvSingerName.text = album.audio.artist
                tvSongTitle.text = album.audio.title
                ivFavourite.visibility = if (album.isFavourite) View.VISIBLE else View.GONE
            }
        }
    }

    class AudioDiffCallBack : DiffUtil.ItemCallback<AudioWrapper>() {
        override fun areItemsTheSame(oldItem: AudioWrapper, newItem: AudioWrapper): Boolean =
            oldItem.audio.id == newItem.audio.id

        override fun areContentsTheSame(oldItem: AudioWrapper, newItem: AudioWrapper): Boolean =
            oldItem == newItem
    }
}