package com.example.assignment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface PermissionCallback {
    fun onPermissionGranted(permission: String)
    fun onPermissionDenied(permission: String) {}
}

class PermissionHelper(
    private val activity: AppCompatActivity,
    private val callback: PermissionCallback
) : ConfirmDialogInterface {
    private companion object {
        const val BASE_VERSION_CODE = Build.VERSION_CODES.M
    }

    private var requestPermission: Array<String> = emptyArray()
    private val toastHelper = ToastHelper(activity)

    // 단일 권한 요청
    private val singlePermissionLauncher: ActivityResultLauncher<String> by lazy {
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                toastHelper.showToast("권한이 허용되었습니다.")
                callback.onPermissionGranted(requestPermission[0])
            } else {
                shouldShowPermissionRationale(arrayOf(requestPermission[0]))
                callback.onPermissionDenied(requestPermission[0])
            }
        }
    }

    // 다중 권한 요청
    private val multiplePermissionsLauncher: ActivityResultLauncher<Array<String>> by lazy {
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions: Map<String, Boolean> ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                toastHelper.showToast("모든 권한이 허용되었습니다.")
                requestPermission.forEach { callback.onPermissionGranted(it) }
            } else {
                permissions.forEach { (permission, isGranted) ->
                    if (isGranted) {
                        callback.onPermissionGranted(permission)
                    } else {
                        callback.onPermissionDenied(permission)
                    }
                }
                shouldShowPermissionRationale(requestPermission)
            }
        }
    }

    // 단일 권한 허용 여부 확인
    @SuppressLint("ObsoleteSdkInt")
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < BASE_VERSION_CODE || ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 다중 권한 허용 여부 확인
    @SuppressLint("ObsoleteSdkInt")
    private fun hasPermissions(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < BASE_VERSION_CODE) {
            return true
        }
        return permissions.all { hasPermission(it) }
    }

    // 단일 권한 요청
    private fun requestSinglePermission(permission: String) =
        singlePermissionLauncher.launch(permission)

    // 다중 권한 요청
    private fun requestMultiplePermission(permissions: Array<String>) {
        val permissionArray = getNonGrantedPermissions(permissions)
        multiplePermissionsLauncher.launch(permissionArray)
    }

    // 요청된 권한 중에 허용되지 않은 권한 리스트 반환
    private fun getNonGrantedPermissions(permissions: Array<String>): Array<String> {
        return permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    }

    fun shouldShowPermissionRationale(permissions: Array<String>) {
        val descriptionPermission = permissions.filter {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (descriptionPermission.isNotEmpty()) {
            val title = "권한이 필요합니다."
            val content = "권한을 허용하시겠습니까?"

            val dialog =
                ConfirmDialog(this, title, content, "확인", if (permissions.size == 1) 1 else 2)
            dialog.isCancelable = false
            activity.let { dialog.show(it.supportFragmentManager, "ConfirmDialog") }
        } else {
            toastHelper.showToast("권한이 거부되었습니다. 설정에서 권한을 허용해주세요.")
        }
    }

    override fun onClickYesButton(id: Int) {
        when (id) {
            1 -> singlePermissionLauncher.launch(requestPermission[0])
            2 -> multiplePermissionsLauncher.launch(requestPermission)
        }
        if (id == 1) {
            if (!hasPermission(requestPermission[0]))
                showPermissionRequiredFragment()
        } else if (id == 2) {
            if (!hasPermissions(requestPermission)) {
                showPermissionRequiredFragment()
            }
        }
    }

    override fun onClickNoButton(id: Int) {
        showPermissionRequiredFragment()
    }

    fun showPermissionRequiredFragment() {
        val fragment = RedirectSettingsFragment()
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

    // 권한 처리 메서드
    fun checkAndRequestPermissions(permission: Any) {
        when (permission) {
            is String -> {
                if (permission.isEmpty()) {
                    toastHelper.showToast("요청할 권한이 없습니다.")
                    return
                }
                requestPermission = arrayOf(permission)
                if (hasPermission(permission)) {
                    return
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission
                        )
                    ) {
                        requestSinglePermission(permission)
                    } else {
                        shouldShowPermissionRationale(arrayOf(permission))
                    }
                }
            }

            is Array<*> -> {
                @Suppress("UNCHECKED_CAST")
                val permissionArray = permission as? Array<String>
                if (permissionArray.isNullOrEmpty()) {
                    toastHelper.showToast("요청할 권한이 없습니다.")
                    return
                }
                requestPermission = permissionArray
                if (hasPermissions(permission)) {
                    return
                } else {
                    if (permissionArray.none {
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                it
                            )
                        }) {
                        requestMultiplePermission(permissionArray)
                    } else {
                        shouldShowPermissionRationale(permissionArray)
                    }
                }
            }

            else -> {
                toastHelper.showToast("잘못된 권한 요청입니다.")
            }
        }
    }
}

class ToastHelper(private val context: Context) {
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}