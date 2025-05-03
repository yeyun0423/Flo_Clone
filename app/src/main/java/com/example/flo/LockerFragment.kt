package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LockerFragment : Fragment() {

    private lateinit var albumRecyclerView: RecyclerView
    private lateinit var lockerAdapter: LockerAdapter
    private lateinit var albumList: ArrayList<Album>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_locker, container, false)

        albumRecyclerView = view.findViewById(R.id.rv_locker_album)
        albumRecyclerView.layoutManager = LinearLayoutManager(context)

        albumList = arrayListOf(
            Album("사랑이라 했던 말 속에서", "캔트비블루", R.drawable.img_album_exp2),
            Album("somebody", "디오", R.drawable.img_album_exp2),
            Album("pain", "하현상", R.drawable.img_album_exp2),
            Album("O", "코드 쿤스트", R.drawable.img_album_exp2),
            Album("Cruise", "BOYCOLD", R.drawable.img_album_exp2),
            Album("55", "코드 쿤스트", R.drawable.img_album_exp2)
        )

        lockerAdapter = LockerAdapter(albumList)
        albumRecyclerView.adapter = lockerAdapter

        return view
    }
}
