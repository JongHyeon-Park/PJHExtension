package com.bradpark.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.provider.DocumentsContract
import androidx.core.content.ContextCompat

class FileUtil {

    companion object {

        @JvmStatic
        fun isAvailableSDCard(context: Context) : Boolean {
            var storageSize = 0
            var storageManager: StorageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                var storageVolumeList:List<StorageVolume> =  storageManager.storageVolumes
                storageSize = storageVolumeList.size
            } else {
                val storage = ContextCompat.getExternalFilesDirs(context, null)
                for (index in storage.indices) {
                    if (storage[index] != null) {
                        storageSize++
                    }
                }
            }

            return storageSize > 1
        }


        @JvmStatic
        fun getPathToUriRoot(context: Context, uri: Uri?): String {
            if (uri == null) {
                return ""
            }

            if (DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if ("com.android.externalstorage.documents" == uri?.authority) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory().toString() + "/" + split[1] + "/"
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                        } else {    // This is for checking SD Card
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    } else {
                        val storage = ContextCompat.getExternalFilesDirs(context, null)
//                        Logger.e("sigu", "dir size = ${storage.size}")
                        return if (storage.size > 1) {
                            "${storage[1].parent.replace("/Android/data/", "").replace(context.packageName, "")}"
                        } else {
                            "${storage[0].parent.replace("/Android/data/", "").replace(context.packageName, "")}"
                        }
                    }
                }
            }
            return ""
        }

        @JvmStatic
        fun getSdCardUuid(context: Context) : String {
            var storageManager: StorageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                var storageVolumeList:List<StorageVolume> =  storageManager.storageVolumes
                if (storageVolumeList.size > 1) {
//                    return storageVolumeList[1].uuid
                    return if (storageVolumeList[1].uuid != null) {
                        storageVolumeList[1].uuid!!
                    } else {
                        ""
                    }
                }
            } else {
                val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val getVolumeListMethod = storageManager.javaClass.getMethod("getVolumeList")
                val volumes = getVolumeListMethod.invoke(storageManager) as Array<Any>

                for (volume in volumes) {
                    val getStateMethod = volume.javaClass.getMethod("getState")
                    val mState = getStateMethod.invoke(volume) as String

                    val isPrimaryMethod = volume.javaClass.getMethod("isPrimary")
                    val mPrimary = isPrimaryMethod.invoke(volume) as Boolean

                    if (!mPrimary && mState == "mounted") {
                        val getUuidMethod = volume.javaClass.getMethod("getUuid")
                        val mUuid = getUuidMethod.invoke(volume) as String
                        if (mUuid != null) {
                            return mUuid
                        }
                    }
                }
            }
            return ""
        }
    }
}