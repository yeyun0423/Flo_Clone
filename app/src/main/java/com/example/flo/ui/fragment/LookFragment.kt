package com.example.flo.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.ui.adapter.ChartAdapter
import com.example.flo.R
import com.example.flo.data.ChartItem

class LookFragment : Fragment() {

    private lateinit var selectedTab: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_look, container, false)

        val tabChart = view.findViewById<TextView>(R.id.tabChart)
        val tabVideo = view.findViewById<TextView>(R.id.tabVideo)
        val tabGenre = view.findViewById<TextView>(R.id.tabGenre)
        val tabMood = view.findViewById<TextView>(R.id.tabMood)
        val tabSituation = view.findViewById<TextView>(R.id.tabSituation)
        val tabAudio = view.findViewById<TextView>(R.id.tabAudio)

        val tabs = listOf(tabChart, tabVideo, tabGenre, tabMood, tabSituation, tabAudio)

        selectedTab = tabChart // 기본 선택된 탭

        tabs.forEach { tab ->
            tab.setOnClickListener {
                selectTab(tab, tabs)
            }
        }
        val chartRecyclerView = view.findViewById<RecyclerView>(R.id.chartRecyclerView)
        chartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val chartItems = List(10) { i -> ChartItem("Item $i") }
        val adapter = ChartAdapter(chartItems)

        chartRecyclerView.adapter = adapter


        return view
    }

    private fun selectTab(selected: TextView, allTabs: List<TextView>) {
        allTabs.forEach { tab ->
            if (tab == selected) {
                tab.setBackgroundColor(Color.parseColor("#3E5EFF"))
                tab.setTextColor(Color.WHITE)
            } else {
                tab.setBackgroundColor(Color.parseColor("#EEEEEE"))
                tab.setTextColor(Color.parseColor("#888888"))
            }
        }
    }
}