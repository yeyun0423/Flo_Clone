package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.util.Log


class LockerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_locker, container, false)

        val tabList = view.findViewById<TextView>(R.id.lockerTabListTv)
        val tabLike = view.findViewById<TextView>(R.id.lockerTabLikeTv)

        val db = AppDatabase.getInstance(requireContext())

        // 기본 더미 데이터 생성
        if (db.albumDao().count() == 0) {
            db.albumDao().insert(
                Album(id = 1, title = "사랑이라 했던 말 속에서", singer = "캔트비블루", coverImg = R.drawable.img_album_exp2)
            )
            db.albumDao().insert(
                Album(id = 2, title = "somebody", singer = "디오", coverImg = R.drawable.img_album_exp3)
            )
            db.albumDao().insert(
                Album(id = 3, title = "pain", singer = "하현상", coverImg = R.drawable.img_album_exp4)
            )
        }
        /*
         // Firebase에서 좋아요 상태 동기화
         val firebase = FirebaseDatabase.getInstance()
         val likeRef = firebase.getReference("likes")
         val userId = "test_user"

         likeRef.child(userId).get().addOnSuccessListener { snapshot ->
             val likedIds = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.toSet()
             val albumList = db.albumDao().getAllAlbums()
             albumList.forEach { it.isLiked = it.id in likedIds }
             albumList.forEach { db.albumDao().updateLikeStatus(it.id, it.isLiked) }
             Log.d("LockerFragment", "Firebase에서 좋아요 상태 동기화 완료")

             // 동기화가 끝난 후에 프래그먼트 표시!
             childFragmentManager.beginTransaction()
                 .replace(R.id.lockerContentFrame, MyListFragment())
                 .commit()
         }
 */

        // RoomDB에서 바로 프래그먼트 표시
        val albumList = db.albumDao().getAllAlbums()
        albumList.forEach {
            Log.d("LockerFragment", "Album: ${it.title}, isLiked: ${it.isLiked}")
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.lockerContentFrame, MyListFragment())
            .commit()


        // 리스트 탭 클릭 시 다시 MyListFragment 보여주기
        tabList.setOnClickListener {
            childFragmentManager.beginTransaction()
                .replace(R.id.lockerContentFrame, MyListFragment())
                .commit()
        }

        tabLike.setOnClickListener {
            val fragment = MyLikeFragment()
            Log.d("LockerFragment", "MyLikeFragment 완전 새로 생성 및 붙이기")
            childFragmentManager.beginTransaction()
                .replace(R.id.lockerContentFrame, fragment)
                .commit()
        }

        return view
    }
}
