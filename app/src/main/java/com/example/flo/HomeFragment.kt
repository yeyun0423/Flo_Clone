package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        db = AppDatabase.getInstance(requireContext())

        val albumList = db.albumDao().getAllAlbums()

        binding.homeTodayMusicOverseaHs.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeAdapter(albumList) { album ->
                val albumFragment = AlbumFragment().apply {
                    arguments = Bundle().apply {
                        putInt("albumId", album.id)
                        putString("title", album.title)
                        putString("singer", album.singer)
                        putInt("coverImg", album.coverImg)
                    }
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, albumFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return binding.root
    }
}
