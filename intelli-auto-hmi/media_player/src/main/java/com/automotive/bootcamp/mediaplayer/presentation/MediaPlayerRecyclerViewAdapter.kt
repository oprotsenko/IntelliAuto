package com.automotive.bootcamp.mediaplayer.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.ItemMediaPlayerAlbumBinding
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import com.automotive.bootcamp.mediaplayer.presentation.data.loadCircleImage

class MediaPlayerRecyclerViewAdapter(private val onMediaItemClickListener: MediaItemClickListener) :
    ListAdapter<MediaAlbum, MediaPlayerRecyclerViewAdapter.AlbumViewHolder>(AlbumDiffCallBack()) {
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
        fun bind(album: MediaAlbum){
            binding.apply {
                ivAlbumArt.loadImage(
                    album.artImage ?:
                     "https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png"
                )
                tvSingerName.text = album.singerName
                tvSongTitle.text = album.songTitle
                root.setOnClickListener {
                    // add smth
                }
            }
        }
    }
}