package com.example.flo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.ui.adapter.LockerAdapter
import com.example.flo.data.database.AppDatabase
import com.example.flo.databinding.FragmentMysaveBinding

class MySaveFragment : Fragment() {

    private lateinit var binding: FragmentMysaveBinding
    private lateinit var db: AppDatabase
    private lateinit var lockerAdapter: LockerAdapter
    private val userId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMysaveBinding.inflate(inflater, container, false)
        db = AppDatabase.Companion.getInstance(requireContext())

        setupRecyclerView()
        loadLikedAlbums()

        return binding.root
    }

    private fun setupRecyclerView() {
        lockerAdapter = LockerAdapter(emptyList())
        binding.rvAlbumList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lockerAdapter
        }
    }

    private fun loadLikedAlbums() {
        val likedAlbumIds = db.likeDao().getLikedAlbumIds(userId)
        val likedAlbums = likedAlbumIds.mapNotNull { albumId ->
            db.albumDao().getAlbumById(albumId)
        }

        lockerAdapter.updateList(likedAlbums)
    }
}