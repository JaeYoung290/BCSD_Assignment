package com.example.assignment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ColorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = View(requireContext())
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val colorOrder = arguments?.getInt(COLOR_ORDER) ?: 0
        val colors = listOf(
            Color.RED,
            Color.rgb(255, 127, 0),
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.rgb(75, 0, 130),
            Color.rgb(137, 119, 173)
        )
        view.setBackgroundColor(colors[colorOrder])

        return view
    }

    companion object {
        private const val COLOR_ORDER = "order"

        fun newInstance(order: Int): ColorFragment {
            val fragment = ColorFragment()
            val bundle = Bundle()
            bundle.putInt(COLOR_ORDER, order)
            fragment.arguments = bundle
            return fragment
        }
    }
}