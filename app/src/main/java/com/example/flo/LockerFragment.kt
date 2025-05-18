package com.example.flo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.flo.MyLikeFragment
import android.util.Log



class LockerFragment : Fragment() {
    private var myLikeFragment: MyLikeFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_locker, container, false)


        val tabList = view.findViewById<TextView>(R.id.lockerTabListTv)
        val tabLike = view.findViewById<TextView>(R.id.lockerTabLikeTv)



        val db = AppDatabase.getInstance(requireContext())

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

        childFragmentManager.beginTransaction()
            .replace(R.id.lockerContentFrame, MyListFragment())
            .commit()


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


