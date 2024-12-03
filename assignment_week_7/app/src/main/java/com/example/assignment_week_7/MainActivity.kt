package com.example.assignment_week_7

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NameAdapter
    private val names = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        adapter = NameAdapter(
            names,
            onItemClick = { position -> showDeleteDialog(position) },
            onItemLongClick = { position -> showEditDialog(position) }
        )
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupAddButton() {
        val nameEditText = findViewById<EditText>(R.id.editText)
        findViewById<Button>(R.id.addButton).setOnClickListener {
            val name = nameEditText.text.toString()
            if (name.isNotEmpty()) {
                names.add(name)
                adapter.notifyItemInserted(names.size - 1)
                nameEditText.text.clear()
            }
        }
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("삭제")
            .setMessage("${names[position]}을(를) 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                names.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showEditDialog(position: Int) {
        val editText = EditText(this)
        editText.setText(names[position])

        AlertDialog.Builder(this)
            .setTitle("수정")
            .setView(editText)
            .setPositiveButton("확인") { _, _ ->
                val newName = editText.text.toString()
                if (newName.isNotEmpty()) {
                    names[position] = newName
                    adapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }
}