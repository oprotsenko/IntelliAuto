package com.automotive.bootcamp.mediaplayer.presentation

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.automotive.bootcamp.mediaplayer.R
import com.automotive.bootcamp.mediaplayer.databinding.ItemSongBinding
import com.automotive.bootcamp.mediaplayer.domain.models.Song
import com.automotive.bootcamp.mediaplayer.domain.models.SongWrapper

class SongsRecyclerViewAdapter(private val onMediaItemClickListener: MediaItemClickListener) :
    ListAdapter<SongWrapper, SongsRecyclerViewAdapter.SongViewHolder>(SongDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_song,
            parent,
            false
        )
        return SongViewHolder(ItemSongBinding.bind(view))
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
            root.setOnClickListener {
                onMediaItemClickListener.onMediaClickListener(position)
            }
        }
    }

    class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val root = binding.root

        fun bind(album: SongWrapper) {
            binding.apply {
                ivAlbumArt.setImageBitmap(album.song.cover ?:
                BitmapFactory.decodeFile("https://27mi124bz6zg1hqy6n192jkb-wpengine.netdna-ssl.com/wp-content/uploads/2019/10/Our-Top-10-Songs-About-School-768x569.png"))
                tvSingerName.text = album.song.artist
                tvSongTitle.text = album.song.title
            }
        }
    }

    class SongDiffCallBack : DiffUtil.ItemCallback<SongWrapper>() {
        override fun areItemsTheSame(oldItem: SongWrapper, newItem: SongWrapper): Boolean =
            oldItem.song.id == newItem.song.id

        override fun areContentsTheSame(oldItem: SongWrapper, newItem: SongWrapper): Boolean =
            oldItem == newItem
    }
}