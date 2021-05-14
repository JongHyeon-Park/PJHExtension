package com.bradpark.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat

/**
 * 권한 요청을 위한 object 클래스
 *
 * 사용방법
 * 1. CubeRequestPermission.isPermissionGranted() 권한 요청
 * 2. CubeRequestPermission.onRequestPermissionsResult() 호출
 * 3. CubeRequestPermission.getPermissionResult() 요청한 각 권한의 승인 여부
 * 4. CubeRequestPermission.moveToSetting() 설정 화면으로 이동
 */
object PjhRequestPermission : ActivityCompat.OnRequestPermissionsResultCallback {
    private var permissionRequestCode: Int = 0
    private lateinit var activity: Activity
    private var permissionState: HashMap<String, PermissionState> = hashMapOf()

    /**
     * 권한 요청
     *
     * @param _activity Activity
     * @param permissions ArrayList<String>
     * @param _permissionRequestCode Int
     */
    fun isPermissionGranted(_activity: Activity, permissions: ArrayList<String>, _permissionRequestCode: Int) {
        this.permissionRequestCode = _permissionRequestCode
        this.activity = _activity

        if (!hasPermissions(activity, permissions)) {
            // 권한 없는 경우
            ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), permissionRequestCode)
        }
    }


    /**
     * 권한 요청 결과
     *
     * @param requestCode Int
     * @param permissions Array<out String>
     * @param grantResults IntArray
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty()) {
            for ((index, permission) in permissions.withIndex()) {
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 승인된 경우
                    PjhLogger.e("PermissionsResult", "$permission PERMISSION_GRANTED")
                    permissionState[permission] = PermissionState.GRANTED
                } else if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[index])) {
                        // 권한이 거절된 경우
                        PjhLogger.e("PermissionsResult", "$permission PERMISSION_DENIED")
                        permissionState[permission] = PermissionState.DENIED
                    } else {
                        // 다시 묻지 않음을 선택하거나 기기 정책으로 인해 앱이 해당 권한을 갖지 못하도록 금지했을 경우
                        PjhLogger.e("PermissionsResult", "$permission need setting")
                        permissionState[permission] = PermissionState.SETTING
                    }
                }
            }
        }
    }


    /**
     * 요청한 각 권한의 승인 여부
     *
     * @return HashMap<String, PermissionState>
     */
    fun getPermissionResult(): HashMap<String, PermissionState> {
        return this.permissionState
    }

    /**
     * 설정 화면으로 이동
     *
     * @param setting String?
     */
    fun moveToSetting(setting: String? = null) = run {
        if (setting.isNullOrEmpty()) {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
        } else {
            activity.startActivity(Intent(setting))
        }
    }

    /**
     * 권한 확인
     *
     * @param context Context
     * @param permissions ArrayList<String>
     * @return Boolean
     */
    private fun hasPermissions(context: Context, permissions: java.util.ArrayList<String>): Boolean = permissions.all { permission ->
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    enum class PermissionState {
        GRANTED, DENIED, SETTING;
    }
}