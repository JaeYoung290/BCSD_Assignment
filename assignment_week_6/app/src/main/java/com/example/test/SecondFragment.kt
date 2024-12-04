package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback

class SecondFragment : Fragment() {

    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            count = it.getInt(ARG_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        val randomValue = (0..count).random()
        val randomTextView: TextView = view.findViewById(R.id.randomTextView)
        randomTextView.text = randomValue.toString()

        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack()
            }
        })

        return view
    }

    companion object {
        private const val ARG_COUNT = "count"

        @JvmStatic
        fun newInstance(count: Int) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COUNT, count)
                }
            }
    }
}