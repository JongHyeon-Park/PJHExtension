package com.bradpark.mylibrary.Utils

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import java.io.File
import java.util.concurrent.TimeUnit

class Utils {

    interface StartLectureCallback {
        fun onComplete(success: Boolean,message : String ="")
    }

    interface UpdateResultCallback {
        fun onComplete(hasUpdate: Boolean,checkNotice: Boolean,msg: String, url: String)
    }

    interface ReturnProgressCallback {
        fun onComplete(success: Boolean, rate : String?, lectureSno : String)
        fun onFailed(success: Boolean, code : String?, message : String?, info: String?)
    }

    interface ReturnPushCallback {
        fun onComplete(success: Boolean, message: String)
        fun onFailed(message: String?)
        fun onOnlyB2BBuild()
    }

    companion object {

        /**
         * Remove All Fragment
         *
         * @param fragmentManager
         */
        @JvmStatic
        fun removeAllFragment(fragmentManager: androidx.fragment.app.FragmentManager) {
            for (i in 0 until fragmentManager.backStackEntryCount) {
                fragmentManager.popBackStack()
            }
        }

        /**
         * Replace Fragment
         *
         * @param fragmentManager
         * @param fragment
         * @param frameId
         */
        @JvmStatic
        fun replaceFragment(fragmentManager: androidx.fragment.app.FragmentManager,
                            fragment: androidx.fragment.app.Fragment, frameId: Int, tag: String, addBackStack: Boolean) {
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(frameId, fragment, tag)
            if (addBackStack) {
                transaction.addToBackStack(null)
            } else {
                transaction.disallowAddToBackStack()
            }
            try {
                transaction.commit()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }

        }

        /**
         * Add Fragment
         *
         * @param fragmentManager
         * @param fragment
         * @param frameId
         */
        @JvmStatic
        fun addFragment(fragmentManager: androidx.fragment.app.FragmentManager,
                        fragment: androidx.fragment.app.Fragment, frameId: Int, tag: String, addBackStack: Boolean) {
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, fragment, tag)
            if (addBackStack) {
                transaction.addToBackStack(tag)
            } else {
                transaction.disallowAddToBackStack()
            }
            transaction.commit()
        }

        /**
         * Add Fragment
         *
         * @param fragmentManager
         * @param fragment
         * @param frameId
         * @param tag
         */
        @JvmStatic
        fun addFragment(fragmentManager: androidx.fragment.app.FragmentManager,
                        fragment: androidx.fragment.app.Fragment, frameId: Int, tag: String) {
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, fragment, tag)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        @JvmStatic
        fun removeFragment(fragmentManager: androidx.fragment.app.FragmentManager, tag: String) {
            val fragment: androidx.fragment.app.Fragment? = fragmentManager.findFragmentByTag(tag)
            if (fragment != null) {
                val transaction = fragmentManager.beginTransaction()
                transaction.remove(fragment)
                transaction.commit()
            }
        }

        /**
         * Check Package installed
         *
         * @param packageName
         * @param packageManager
         */
        @JvmStatic
        fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
            return try {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        /**
         * 앱 버전정보 반환
         *
         * @param context
         * @return
         */
        @JvmStatic
        fun getAppVersionName(context: Context): String {
            try {
                val pi: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                return pi.versionName
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }


        @JvmStatic
        fun deleteFiles(dirPath: String) {
            val file = File(dirPath)

            if (file.isDirectory) {
                val childFileList = file.listFiles()
                childFileList.forEach {
                    it.delete()
                }
            }

//            file.delete()
        }



        @JvmStatic
        fun getImageUrl(url: String?, siteCode: String?): String {
            if (url == null || url.isEmpty()) {
                return siteCode ?: ""
            } else {
                return url ?: ""
            }
        }

        @JvmStatic
        fun compareVersion(currentVersion: String, nowVersion: String): Boolean
        {
            var ret = false
            var currentVersionList = currentVersion.split(".")
            var nowVersionList = nowVersion.split(".")
            if(currentVersionList.size != nowVersionList.size){
                return ret
            } else {
                var count = 0
                for (i in 0 until currentVersionList.size)
                {
                    if(currentVersionList[i].toInt() < nowVersionList[i].toInt())
                    {
                        ret = true
                        break
                    } else if(currentVersionList[i].toInt() > nowVersionList[i].toInt()) {
                        ret = false
                        break
                    } else {
                        count++
                        continue
                    }
                }
                if(count >= 3)
                    ret = false
            }
            return ret
        }

        @JvmStatic
        fun getDeviceNavigationHeight(context: Context): Int{           //디바이스 네비게이션 높이 구하는 함수
            var resources = context.getResources()
            var resourceId = resources?.getIdentifier("navigation_bar_height", "dimen", "android")
            var height = 0
            if(resourceId?:0 > 0)
                height = resources?.getDimensionPixelSize(resourceId?:0)?:0
            return height
        }

        @JvmStatic
        fun getLectureMinuteSecondToMillis(minute: String?,second: String?): Long{           //해당 강의 분,초를 밀리세컨드초 구하는 함수
            var calcMin: Long = 0
            var calcSec: Long = 0
            var retTotal: Long
            if(!minute.isNullOrEmpty()) {
                val min = minute.toLong()
                calcMin = TimeUnit.MINUTES.toMillis(min)
            }
            if(!second.isNullOrEmpty()) {
                val sec = second.toLong()
                calcSec = TimeUnit.SECONDS.toMillis(sec)
            }
            retTotal = calcMin + calcSec
            return retTotal
        }


        @JvmStatic
        fun isNotificationChannelEnabled(context: Context,channelId: String): Boolean{           //chanel notification 막기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(!TextUtils.isEmpty(channelId)) {
                    var manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    var channel = manager?.getNotificationChannel(channelId)
                    return channel.importance != NotificationManager.IMPORTANCE_NONE
                }
                return false
            } else {
                return NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
        }

        @JvmStatic
        fun byteToHexString(byteArray: ByteArray): String {
            var value = ""
            byteArray.forEach {
                value += String.format("%02X", it)
            }
            return value
        }

    }
}

