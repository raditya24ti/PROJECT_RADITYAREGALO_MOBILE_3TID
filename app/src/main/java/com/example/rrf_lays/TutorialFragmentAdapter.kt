package com.example.rrf_lays.tutorial

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// Adapter khusus untuk ViewPager2 pada Onboard Screen
class TutorialFragmentAdapter(
    activity: TutorialMessageActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {

    // Menentukan jumlah total halaman (slide)
    override fun getItemCount(): Int {
        return fragments.size
    }

    // Menampilkan fragment sesuai dengan urutan posisinya (0, 1, 2)
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}