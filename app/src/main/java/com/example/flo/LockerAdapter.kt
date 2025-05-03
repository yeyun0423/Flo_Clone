package com.example.flo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LockerAdapter(private val albumList: ArrayList<Album>) :
    RecyclerView.Adapter<LockerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumImage: ImageView = view.findViewById(R.id.iv_album_cover)
        val title: TextView = view.findViewById(R.id.tv_album_title)
        val singer: TextView = view.findViewById(R.id.tv_album_singer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_locker_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]
        holder.albumImage.setImageResource(album.coverImg)
        holder.title.text = album.title
        holder.singer.text = album.singer
    }

    override fun getItemCount(): Int = albumList.size
}
