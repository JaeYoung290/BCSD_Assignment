package com.example.assignment

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.assignment.databinding.FragmentRedirectSettingsBinding

class RedirectSettingsFragment : Fragment() {
    private var _binding: FragmentRedirectSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRedirectSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToSettings.setOnClickListener {
            openSettingScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermissionAndNavigate()
    }

    private fun checkPermissionAndNavigate() {
        val permissionHelper =
            PermissionHelper(requireActivity() as AppCompatActivity, object : PermissionCallback {
                override fun onPermissionGranted(permission: String) {
                    parentFragmentManager.popBackStack()
                }

                override fun onPermissionDenied(permission: String) {
                    return
                }
            })

        if (permissionHelper.hasPermission(Manifest.permission.READ_MEDIA_AUDIO)) {
            parentFragmentManager.popBackStack()
        }
    }


    private fun openSettingScreen() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = Uri.parse("package:" + context?.packageName)
            context?.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}