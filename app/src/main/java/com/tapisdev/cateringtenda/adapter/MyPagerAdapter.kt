package com.tapisdev.cateringtenda.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tapisdev.Tendatenda.fragment.TabTendaFragment
import com.tapisdev.cateringtenda.fragment.TabCateringFragment

/**
 * Created by Avin on 22/09/2018.
 */
class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        TabCateringFragment(),
        TabTendaFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Catering"
            1 -> "Paket Alat Pesta"
            else -> "-"
        }
    }
}
