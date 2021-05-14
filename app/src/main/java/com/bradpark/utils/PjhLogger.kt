package com.bradpark.utils

import android.util.Log

/**
 * PjhLogger
 * Log 관련 담당
 */
class PjhLogger {
    companion object {
        val isShow: Boolean = com.bradpark.config.PjhConfig.mDebugMode

        private fun buildLogMsg(message: String): String {
            val ste: StackTraceElement = Thread.currentThread().stackTrace[4]

            return "[[${ste.fileName}>${ste.methodName}>#${ste.lineNumber}]] $message"
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

        fun longMsg(tag: String, msg: String) {
            if (isShow) {
                val maxLogSize = 1000
                val msgCnt = msg.length

                for (i in 0..msgCnt / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > msgCnt) msgCnt else end
                    Log.d(tag,
                        buildLogMsg(
                            msg.substring(start, end)
                        )
                    )
                }
            }
        }

        fun longMsg2(tag: String, msg: String) {
            if (isShow) {
                val maxLogSize = 1000
                val msgCnt = msg.length

                for (i in 0..msgCnt / maxLogSize) {
                    val start = i * maxLogSize
                    var end = (i + 1) * maxLogSize
                    end = if (end > msgCnt) msgCnt else end
                    Log.d(tag, msg.substring(start, end))
                }
            }
        }
    }
}