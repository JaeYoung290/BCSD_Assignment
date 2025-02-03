package com.example.assignment

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TextFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val textView = TextView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            gravity = Gravity.CENTER
            textSize = 80F
            setTypeface(typeface, Typeface.BOLD)

            text = arguments?.getString(TEXT_ORDER) ?: ""
        }

        return textView
    }

    companion object {
        private const val TEXT_ORDER = "order"

        fun newInstance(order: String): TextFragment {
            val fragment = TextFragment()
            val bundle = Bundle()
            bundle.putString(TEXT_ORDER, order)
            fragment.arguments = bundle
            return fragment
        }
    }
}