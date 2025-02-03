package com.example.assignment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        val bottomNavigationView = binding.bottomNavigationView

        setViewPagerAdapterForPageType(ViewPagerAdapter.PageType.COLOR)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rainbow_menu -> setViewPagerAdapterForPageType(ViewPagerAdapter.PageType.COLOR)
                R.id.number_menu -> setViewPagerAdapterForPageType(ViewPagerAdapter.PageType.NUMBER)
                R.id.alphabet_menu -> setViewPagerAdapterForPageType(ViewPagerAdapter.PageType.ALPHABET)
            }
            true
        }


    }

    private fun setViewPagerAdapterForPageType(pageType: ViewPagerAdapter.PageType) {
        viewPager.adapter = ViewPagerAdapter(this, pageType)
    }
}