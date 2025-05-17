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

        // 더미 앨범 추가
        if (db.albumDao().getAllAlbums().isEmpty()) {
            db.albumDao().insert(
                Album(1, "사랑이라 했던 말 속에서", "캔트비블루", R.drawable.img_album_exp2)
            )
            db.albumDao().insert(
                Album(2, "somebody", "디오", R.drawable.img_album_exp3)
            )
            db.albumDao().insert(
                Album(3, "pain", "하현상", R.drawable.img_album_exp4)
            )

        }

        val albumList = db.albumDao().getAllAlbums()


        return binding.root
    }
}
