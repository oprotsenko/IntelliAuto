package com.automotive.bootcamp.mediaplayer.presentation

import androidx.recyclerview.widget.DiffUtil
import com.automotive.bootcamp.mediaplayer.presentation.data.MediaAlbum

class AlbumDiffCallBack : DiffUtil.ItemCallback<MediaAlbum>() {
    override fun areItemsTheSame(oldItem: MediaAlbum, newItem: MediaAlbum): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MediaAlbum, newItem: MediaAlbum): Boolean =
        oldItem == newItem
}