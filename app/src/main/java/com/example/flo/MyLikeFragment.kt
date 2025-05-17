package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyLikeFragment : Fragment() {

    private lateinit var adapter: LockerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mylike, container, false)

        db = AppDatabase.getInstance(requireContext())

        recyclerView = view.findViewById(R.id.rv_locker_album)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val likedAlbums = db.albumDao().getLikedAlbums()
        adapter = LockerAdapter(likedAlbums)
        recyclerView.adapter = adapter

        return view
    }
}
