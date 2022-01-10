package com.automotive.bootcamp.mediaplayer.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum
import kotlinx.android.synthetic.main.media_player_album.view.*

class MediaPlayerRecyclerViewAdapter ( private val context: Context) :
    ListAdapter<MediaAlbum, MediaPlayerRecyclerViewAdapter.AlbumViewHolder>(AsyncDifferConfig.Builder(AlbumDiffCallBack()).build()) {
    private var list: List<MediaAlbum> = ArrayList()

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        //  holder.albumArt. = load an image to the Image View
        holder.songTitle.text = list[position].songTitle
        holder.singerName.text = list[position].singerName
        holder.rootView.setOnClickListener {
            // add smth
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.media_player_album,
                parent,
                false
            )
        )
    }

    class AlbumViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val rootView = v.clRootView
        val albumArt = v.ivAlbumArt
        val songTitle = v.tvSongTitle
        val singerName = v.tvSingerName
    }
}