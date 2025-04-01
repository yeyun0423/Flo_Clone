package com.example.flo
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVpAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return  when(position)  {
            0-> SongFragment()
            1-> DetailFragment()
            else -> VideoFragment()
        }        //조건에 따라 다른 작업을 하도록
    }

    override fun getItemCount(): Int =3
}