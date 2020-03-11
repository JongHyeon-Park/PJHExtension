package com.bradpark.mylibrary.Utils

import android.content.Context
import android.os.Environment
import android.os.StatFs


/**
 * 디스크 용량 관련 정보
 */
class DiskState {

    companion object {
        const val ERROR: Long = -1

        private fun externalMemoryAvailable(): Boolean {
            return android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED
        }

        @JvmStatic
        fun getAvailableInternalMemorySize(): Long {
            return if (externalMemoryAvailable()) {
                val path = Environment.getExternalStorageDirectory()
                val state = StatFs(path.path)
                val blockSize: Long
                val availableBlocks: Long
                blockSize = state.blockSizeLong
                availableBlocks = state.availableBlocksLong
                availableBlocks * blockSize
            } else {
                ERROR
            }
        }

        @JvmStatic
        fun getTotalInternalMemorySize(): Long {
            return if (externalMemoryAvailable()) {
                val path = Environment.getExternalStorageDirectory()
                val state = StatFs(path.path)
                val blockSize: Long
                val totalBlocks: Long
                blockSize = state.blockSizeLong
                totalBlocks = state.blockCountLong
                totalBlocks * blockSize
            } else {
                ERROR
            }
        }

        @JvmStatic
        fun getAvailableExternalMemorySize(context: Context, path: String): Long {
            return if(FileUtil.isAvailableSDCard(context)) {
                try {
                    val state = StatFs(path)
                    val blockSize: Long
                    val availableBlocks: Long
                    blockSize = state.blockSizeLong
                    availableBlocks = state.availableBlocksLong
                    availableBlocks * blockSize
                }catch (e : Exception) {
                    ERROR
                }
            } else {
                ERROR
            }
        }

        @JvmStatic
        fun getTotalExternalMemorySize(context: Context, path: String): Long {
            return if (externalMemoryAvailable()) {
                try {
                    val state = StatFs(path)
                    val blockSize: Long
                    val totalBlocks: Long
                    blockSize = state.blockSizeLong
                    totalBlocks = state.blockCountLong
                    totalBlocks * blockSize
                } catch (e : Exception) {
                    ERROR
                }
            } else {
                ERROR
            }
        }
    }

}

