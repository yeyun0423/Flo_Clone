package com.example.flo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MySaveFragment : Fragment() {
    private lateinit var adapter: LockerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mysave, container, false)

        db = AppDatabase.getInstance(requireContext())

        recyclerView = view.findViewById(R.id.rv_locker_album)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val albums = db.albumDao().getAllAlbums()
        adapter = LockerAdapter(albums)
        recyclerView.adapter = adapter
        return view
    }
}