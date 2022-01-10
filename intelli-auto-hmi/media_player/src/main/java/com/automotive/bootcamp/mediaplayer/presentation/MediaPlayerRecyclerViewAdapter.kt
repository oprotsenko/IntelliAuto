package com.automotive.bootcamp.mediaplayer.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.MediaPlayerAlbumBinding
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import kotlinx.android.synthetic.main.media_player_album.view.*

class MediaPlayerRecyclerViewAdapter :
    ListAdapter<MediaAlbum, MediaPlayerRecyclerViewAdapter.AlbumViewHolder>(AsyncDifferConfig.Builder(AlbumDiffCallBack()).build()) {
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view: View =  LayoutInflater.from(parent.context).inflate(
            R.layout.media_player_album,
            parent,
            false)
        return AlbumViewHolder(MediaPlayerAlbumBinding.bind(view))
    }

    class AlbumViewHolder(val binding: MediaPlayerAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: MediaAlbum){
         // v.ivAlbumArt. = album.artUrl;
            binding.apply {
                tvSingerName.text = album.singerName
                tvSongTitle.text = album.songTitle
                root.setOnClickListener {
                    // add smth
                }
            }
        }
    }
}