package com.automotive.bootcamp.mediaplayer.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.ItemMediaPlayerAlbumBinding
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.common.extensions.loadImage

class MediaPlayerRecyclerViewAdapter(private val onMediaItemClickListener: MediaItemClickListener) :
    ListAdapter<Song, MediaPlayerRecyclerViewAdapter.AlbumViewHolder>(AlbumDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view: View =  LayoutInflater.from(parent.context).inflate(
            R.layout.item_media_player_album,
            parent,
            false)
        return AlbumViewHolder(ItemMediaPlayerAlbumBinding.bind(view))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.root.setOnClickListener {
            onMediaItemClickListener.onMediaClickListener(position)
        }
    }

    class AlbumViewHolder(private val binding: ItemMediaPlayerAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        val root = binding.root
        fun bind(album: Song){
            binding.apply {
                ivAlbumArt.loadImage(
                    album.cover ?:
                     "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png"
                )
                tvSingerName.text = album.artist
                tvSongTitle.text = album.title
                root.setOnClickListener {
                    // add smth
                }
            }
        }
    }

    class AlbumDiffCallBack : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean =
            oldItem == newItem
    }
}