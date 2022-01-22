package com.automotive.bootcamp.mediaplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.ItemAudioBinding
import com.automotive.bootcamp.mediaplayer.presentation.MediaItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.OnItemClickListener
import com.automotive.bootcamp.mediaplayer.presentation.models.AudioWrapper

class AudioRecyclerViewAdapter(
    private val onMediaItemClickListener: MediaItemClickListener,
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<AudioWrapper, AudioRecyclerViewAdapter.SongViewHolder>(SongDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_audio,
            parent,
            false
        )
        return SongViewHolder(ItemAudioBinding.bind(view))
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
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

    class SongViewHolder(private val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val root = binding.root
        val popupMenu = binding.ibPopUpMenu

        fun bind(album: AudioWrapper) {
            binding.apply {
                if (album.audio.cover != null) {
                    ivAlbumArt.setImageBitmap(album.audio.cover)
                }
                tvSingerName.text = album.audio.artist
                tvSongTitle.text = album.audio.title
                ivFavourite.visibility = if (album.isFavourite) View.VISIBLE else View.GONE
            }
        }
    }

    class SongDiffCallBack : DiffUtil.ItemCallback<AudioWrapper>() {
        override fun areItemsTheSame(oldItem: AudioWrapper, newItem: AudioWrapper): Boolean =
            oldItem.audio.id == newItem.audio.id

        override fun areContentsTheSame(oldItem: AudioWrapper, newItem: AudioWrapper): Boolean =
            oldItem == newItem
    }
}