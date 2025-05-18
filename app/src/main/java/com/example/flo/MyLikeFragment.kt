package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyLikeFragment : Fragment() {

    private lateinit var adapter: LockerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase

    private lateinit var bottomSheet: LinearLayout
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MyLikeFragment", " onCreateView 호출됨")

        val view = inflater.inflate(R.layout.fragment_mylike, container, false)

        db = AppDatabase.getInstance(requireContext())

        recyclerView = view.findViewById(R.id.rv_locker_album)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = LockerAdapter(db.albumDao().getLikedAlbums())
        recyclerView.adapter = adapter

        bottomSheet = requireActivity().findViewById(R.id.editBarLayout)
        bottomNav = requireActivity().findViewById(R.id.main_bnv)

        val selectAllCheckBox = view.findViewById<CheckBox>(R.id.selectAllCheckBox)
        selectAllCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val updatedList = db.albumDao().getLikedAlbums().map {
                it.copy(isChecked = isChecked)
            }
            adapter.updateList(updatedList)

            if (isChecked) {
                bottomSheet.visibility = View.VISIBLE
                bottomNav.visibility = View.GONE
            } else {
                bottomSheet.visibility = View.GONE
                bottomNav.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyLikeFragment", "onViewCreated 호출됨")
        refreshList()
    }

    fun refreshList() {
        Log.d("MyLikeFragment", "refreshList() 호출됨")
        val newList = db.albumDao().getLikedAlbums()
        Log.d("MyLikeFragment", " 가져온 album 수: ${newList.size}")
        adapter.updateList(newList)
        Log.d("MyLikeFragment", "adapter.updateList() 호출 완료")
    }
}
