package com.example.assignment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val type: PageType) :
    FragmentStateAdapter(fragmentActivity) {
    enum class PageType {
        COLOR, NUMBER, ALPHABET
    }

    override fun getItemCount(): Int {
        return when (type) {
            PageType.COLOR -> 7
            PageType.NUMBER -> 11
            PageType.ALPHABET -> 26
        }
    }

    override fun createFragment(order: Int): Fragment {
        return when (type) {
            PageType.COLOR -> ColorFragment.newInstance(order)
            PageType.NUMBER -> TextFragment.newInstance((0..10).toList()[order].toString())
            PageType.ALPHABET -> TextFragment.newInstance(('A'..'Z').toList()[order].toString())
        }
    }
}