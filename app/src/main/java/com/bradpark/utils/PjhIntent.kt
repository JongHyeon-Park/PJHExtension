package com.bradpark.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class PjhIntent {
    companion object {
        /**
         * 앱이 실행중일 때 알림(노티)를 누르면 그냥 아무런 실행없이 현재 상태의 화면이 유지되고, 실행 중 홈키로 나간 상태라면 홈키를 누르기 직전의 마지막 상태로 앱이 띄워 지는 함수
         *
         * @param context
         * @param activity
         * @return
         */
        @JvmStatic
        fun getBaseNotificationPendingIntent(context: Context, activity: Activity): PendingIntent {
            val intent = Intent(context, activity::class.java)
            return PendingIntent.getActivity(
                context,
                0,
                intent.setAction(
                    Intent.ACTION_MAIN
                ).addCategory(Intent.CATEGORY_LAUNCHER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}