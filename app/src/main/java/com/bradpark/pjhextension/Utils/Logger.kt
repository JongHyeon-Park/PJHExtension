package com.bradpark.pjhextension.Utils

import android.util.Log
import com.bradpark.pjhextension.BuildConfig

class Logger {
    companion object {
        val isShow: Boolean = BuildConfig.DEBUG

        private fun buildLogMsg(message: String): String {
            val ste: StackTraceElement = Thread.currentThread().stackTrace[4]

            return "[[${ste.fileName}>${ste.methodName}>#${ste.lineNumber}]] ${message}"
        }

        fun d(tag: String, msg: String) {
            if (isShow) {
                Log.d(tag,
                    buildLogMsg(
                        msg
                    )
                )
            }
        }

        fun w(tag: String, msg: String) {
            if (isShow) {
                Log.w(tag,
                    buildLogMsg(
                        msg
                    )
                )
            }
        }

        fun e(tag: String, msg: String) {
            if (isShow) {
                Log.e(tag,
                    buildLogMsg(
                        msg
                    )
                )
            }
        }

        fun i(tag: String, msg: String) {
            if (isShow) {
                Log.i(tag,
                    buildLogMsg(
                        msg
                    )
                )
            }
        }

        fun v(tag: String, msg: String) {
            if (isShow) {
                Log.v(tag,
                    buildLogMsg(
                        msg
                    )
                )
            }
        }
    }
}