package com.example.test

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var count = 0
    private lateinit var countTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toastButton: Button = findViewById(R.id.toastButton)
        val countButton: Button = findViewById(R.id.countButton)
        val randomButton: Button = findViewById(R.id.randomButton)
        countTextView = findViewById(R.id.countTextView)

        toastButton.setOnClickListener {
            showAlertDialog()
        }

        countButton.setOnClickListener {
            count++
            countTextView.text = count.toString()
        }

        randomButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SecondFragment.newInstance(count))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("AlertDialog")
            .setPositiveButton("Positive") { dialog, _ ->
                count = 0
                countTextView.text = count.toString()
                dialog.dismiss()
            }
            .setNeutralButton("Neutral") { dialog, _ ->
                Toast.makeText(this, "Toast 메시지 출력", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Negative") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}