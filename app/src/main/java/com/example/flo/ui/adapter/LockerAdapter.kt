package com.example.flo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.R
import com.example.flo.data.Album

class LockerAdapter(albumList: List<Album>) :
    RecyclerView.Adapter<LockerAdapter.ViewHolder>() {

    private val albumList = ArrayList(albumList)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumImage: ImageView = view.findViewById(R.id.iv_album_cover)
        val title: TextView = view.findViewById(R.id.tv_album_title)
        val singer: TextView = view.findViewById(R.id.tv_album_singer)
        val moreBtn: ImageView = view.findViewById(R.id.iv_album_more)
        val toggleBtn: ImageView = view.findViewById(R.id.btn_toggle)
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

        holder.toggleBtn.setImageResource(
            if (album.isToggled) R.drawable.btn_toggle_on else R.drawable.btn_toggle_off
        )

        holder.toggleBtn.setOnClickListener {
            album.isToggled = !album.isToggled
            holder.toggleBtn.setImageResource(
                if (album.isToggled) R.drawable.btn_toggle_on else R.drawable.btn_toggle_off
            )
        }

        holder.moreBtn.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                albumList.removeAt(pos)
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, albumList.size)
            }
        }


    }


    override fun getItemCount(): Int = albumList.size


    fun updateList(newList: List<Album>) {
        Log.d("LockerAdapter", "updateList 실행됨, 새 리스트 크기: ${newList.size}")
        albumList.clear()
        albumList.addAll(newList)
        notifyDataSetChanged()
    }
}