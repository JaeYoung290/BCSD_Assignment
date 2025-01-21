package com.example.assignment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.assignment.databinding.DialogConfirmBinding

interface ConfirmDialogInterface {
    fun onClickYesButton(id: Int)
    fun onClickNoButton(id: Int)
}
class ConfirmDialog(
    confirmDialogInterface: ConfirmDialogInterface,
    title: String, content: String?, buttonText: String, id: Int
) : DialogFragment() {
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    private var title: String? = null
    private var content: String? = null
    private var buttonText: String? = null
    private var id: Int? = null

    init {
        this.title = title
        this.content = content
        this.buttonText = buttonText
        this.id = id
        this.confirmDialogInterface = confirmDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.dialogTitleTv.text = title

        if (content == null) {
            binding.dialogDescTv.visibility = View.GONE
        } else {
            binding.dialogDescTv.text = content
        }
        binding.checkBtn.text = buttonText

        if (id == -1) {
            binding.cancelBtn.visibility = View.GONE
        }

        binding.cancelBtn.setOnClickListener {
            this.confirmDialogInterface?.onClickNoButton(id!!)
            dismiss()
        }

        binding.checkBtn.setOnClickListener {
            this.confirmDialogInterface?.onClickYesButton(id!!)
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}