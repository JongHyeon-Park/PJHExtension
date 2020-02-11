package com.bradpark.mylibrary.Utils

import java.util.*

class DateUtil {
    class DateConst {
        companion object {
            const val THIRTY_DAY = 30
        }
    }

    companion object {
        @JvmStatic
        fun isDurationMonth(beforeMillisecond: Long): Boolean {        // 저장된 날짜에서 현재가 30일 지났는지 판단
            var value = false
            var before = Calendar.getInstance()
            val now = Calendar.getInstance()
            before.timeInMillis = beforeMillisecond
            before.add(Calendar.DATE, DateConst.THIRTY_DAY)        //30일 더하기
            if(before.timeInMillis >= now.timeInMillis) {
                value = true
            }
            return value
        }
    }
}