package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding


class AlbumFragment : Fragment() {
    private lateinit var binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumBackIv.setOnClickListener { //albumBackIv 누르면 HomeFragment() 로 대체
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
            binding.songLalacLayout.setOnClickListener { //songLalacLayout 누르면 토스트 메세지 보임
            Toast.makeText(activity,"Lalac", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}