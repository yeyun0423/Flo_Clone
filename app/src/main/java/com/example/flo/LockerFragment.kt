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
            Album("somebody", "디오", R.drawable.img_album_exp3),
            Album("pain", "하현상", R.drawable.img_album_exp4),
            Album("O", "코드 쿤스트", R.drawable.img_album_exp5),
            Album("Cruise", "BOYCOLD", R.drawable.img_album_exp6),
            Album("55", "코드 쿤스트", R.drawable.img_album_exp)
           ,Album("다정히 내 이름을 부르면", "경서예지", R.drawable.img_album_exp2),
            Album("한 페이지가 될 수 있게", "DAY6", R.drawable.img_album_exp3),
            Album("좋아좋아", "조정석", R.drawable.img_album_exp4),
            Album("비와 당신", "이무진", R.drawable.img_album_exp5),
            Album("모든 날, 모든 순간", "폴킴", R.drawable.img_album_exp6),Album("헤어지자 말해요", "박재정", R.drawable.img_album_exp3),
            Album("LOVE me", "BE'O", R.drawable.img_album_exp4),
            Album("나의 X에게", "경서", R.drawable.img_album_exp6)
        )

        lockerAdapter = LockerAdapter(albumList)
        albumRecyclerView.adapter = lockerAdapter

        return view
    }
    override fun onResume() {
        super.onResume()

        albumList.clear()

        val likedSongs = SongDatabase.getLikedSongs()
        for (i in 0 until likedSongs.size) {
            val song = likedSongs[i]
            val album = Album(
                title = song.title,
                singer = song.singer,
                coverImg = song.imageResId,
                isToggled = false
            )
            albumList.add(album)
        }

        lockerAdapter.notifyDataSetChanged()
    }


}
