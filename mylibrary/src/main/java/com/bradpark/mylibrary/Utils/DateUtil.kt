package com.bradpark.mylibrary.Utils

import java.util.*

class DateUtil {

    companion object {
        /**
         * 지정된 일수가 지났는지 판단
         * beforeMillisecond 값 이랑 현재 날짜랑 비교하여 판단
         * @param beforeMillisecond
         * @param day
         * @return 지남 true 안지남 false
         */
        @JvmStatic
        fun isDurationDay(beforeMillisecond: Long, day: Int): Boolean {
            var value = false
            var before = Calendar.getInstance()
            val now = Calendar.getInstance()
            before.timeInMillis = beforeMillisecond
            before.add(Calendar.DATE, day)
            if(before.timeInMillis >= now.timeInMillis) {
                value = true
            }
            return value
        }

        /**
         * 지정된 시간이 지났는지 판단
         * beforeMillisecond 값 이랑 현재 날짜랑 비교하여 판단
         * @param beforeMillisecond
         * @param hour
         * @return 지남 true 안지남 false
         */
        @JvmStatic
        fun isDurationHour(beforeMillisecond: Long, hour: Int): Boolean {
            var value = false
            var before = Calendar.getInstance()
            val now = Calendar.getInstance()
            before.timeInMillis = beforeMillisecond
            before.add(Calendar.HOUR,hour)
            if(before.timeInMillis >= now.timeInMillis) {
                value = true
            }
            return value
        }

        /**
         * 지정된 시간(분)이 지났는지 판단
         * beforeMillisecond 값 이랑 현재 날짜랑 비교하여 판단
         * @param beforeMillisecond
         * @param minute
         * @return 지남 true 안지남 false
         */
        @JvmStatic
        fun isDurationMinute(beforeMillisecond: Long, minute: Int): Boolean {
            var value = false
            var before = Calendar.getInstance()
            val now = Calendar.getInstance()
            before.timeInMillis = beforeMillisecond
            before.add(Calendar.MINUTE,minute)
            if(before.timeInMillis >= now.timeInMillis) {
                value = true
            }
            return value
        }
    }
}