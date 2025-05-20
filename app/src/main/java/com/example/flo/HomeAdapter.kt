package com.example.flo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter(
    private val albumList: List<Album>,
    private val itemClick: (Album) -> Unit // 클릭 콜백 전달받음
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumImg: ImageView = view.findViewById(R.id.iv_album_cover)
        val albumTitle: TextView = view.findViewById(R.id.tv_album_title)
        val albumSinger: TextView = view.findViewById(R.id.tv_album_singer)

        fun bind(album: Album) {
            albumImg.setImageResource(album.coverImg)
            albumTitle.text = album.title
            albumSinger.text = album.singer

            itemView.setOnClickListener {
                itemClick(album) // 클릭 시 콜백 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount(): Int = albumList.size
}
