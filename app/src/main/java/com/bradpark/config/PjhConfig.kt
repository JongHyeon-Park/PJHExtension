package com.bradpark.config

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import com.bradpark.crypto.ChCrypto
import com.bradpark.extensions.byteArrayToString
import com.bradpark.extensions.stringToByteArray
import java.util.*

class PjhConfig {
    companion object {
        var mDebugMode: Boolean = false

        private var mNetWorkStatus: PjhNetWorkStatus = PjhNetWorkStatus.OFFLINE

        private lateinit var mContext: Context

        private const val PREF_FILENAME = "com.bradpark.pdhLibrary"

        private const val PREF_LANGUAGE = "pref_language"
        private const val PREF_AUTO_ID = "pref_id"
        private const val PREF_AUTO_PASSWORD = "pref_password"
        private const val PREF_AUTO_CHECKED = "pref_password"
        private const val PREF_MAIN_COLOR = "pref_maincolor"
        private const val PREF_TIME_ZONE = "pref_timezone"

        var systemNavigationColor = 0

        var configTimeZone: TimeZone = Calendar.getInstance().timeZone
        var configCalendar: Calendar = Calendar.getInstance()
        var timeZoneStr: String
            get() = getPrefs().getString(PREF_TIME_ZONE, Calendar.getInstance().timeZone.toString()) ?: Calendar.getInstance().timeZone.toString()
            set(value) {
                getPrefs().edit().putString(PREF_TIME_ZONE, value).apply()
                configTimeZone = TimeZone.getTimeZone(value)
                configCalendar = Calendar.getInstance(configTimeZone)
            }

        var autoId: String?
            get() =  ChCrypto.decrypt(getPrefs().getString(PREF_AUTO_ID,null)?.stringToByteArray)
            set(value) = getPrefs().edit().putString(PREF_AUTO_ID, ChCrypto.encrypt(value!!)?.byteArrayToString).apply()

        fun autoIdRemove() = getPrefs().edit().remove(PREF_AUTO_ID).apply()

        var autoPassword: String?
            get() =  ChCrypto.decrypt(getPrefs().getString(PREF_AUTO_PASSWORD,null)?.stringToByteArray)
            set(value) = getPrefs().edit().putString(PREF_AUTO_PASSWORD, ChCrypto.encrypt(value!!)?.byteArrayToString).apply()

        fun autoPasswordRemove() = getPrefs().edit().remove(PREF_AUTO_PASSWORD).apply()

        var autoChecked: Boolean
            get() =  getPrefs().getBoolean(PREF_AUTO_CHECKED,false)
            set(value) = getPrefs().edit().putBoolean(PREF_AUTO_CHECKED,value).apply()

        fun autoCheckRemove() = getPrefs().edit().remove(PREF_AUTO_CHECKED).apply()

        var mainColor: Int
            get() =  getPrefs().getInt(PREF_MAIN_COLOR, Color.BLACK)
            set(value) = getPrefs().edit().putInt(PREF_MAIN_COLOR,value).apply()

        /**
         * 필수 호출 함수 - PjhApp
         * @param context Context
         * @param buildValue Boolean
         */
        @JvmStatic
        fun setUp(context: Context,buildValue: Boolean) {
            this.mContext = context
            this.mDebugMode = buildValue
        }

        @JvmStatic
        fun getPrefs(): SharedPreferences {
            return mContext.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        }
    }
    enum class PjhNetWorkStatus {
        ONLINE, OFFLINE;
    }

    enum class PjhSnackBarType {
        SUCCESS, INFORMATION, WARNING, FAIL;
    }

    enum class PjhSnackPositionType {
        NORMAL, MAIN_BUTTON, BOTTOM_NAVIGATION, NOTCH;
    }

    enum class PjhSnackNavigationBarStatus {
        Yes, No;
    }

    enum class PjhTmfDialog {
        ONE_TITLE, ONE_NO_TITLE,
        TWO_TITLE, TWO_NO_TITLE,
        THREE_TITLE, THREE_NO_TITLE;
    }

    enum class PjhDialogButton {
        HORIZONTAL_CANCEL, HORIZONTAL_CONFIRM,
        VERTICAL_CANCEL, VERTICAL_CONFIRM,
        VERTICAL_DELETE;
    }

    enum class PjhBottomSheetType {
        Calendar, TextInput,
        Action, ListSearch,
        List, Etc;
    }

    enum class PjhBottomSheetButtonType {
        None, One,
        Two;
    }

    enum class PjhBottomSheetButtonEnable {
        Enable,
        Disable;
    }

    enum class PjhDialogType {
        Title, NoTitle;
    }

    enum class PjhDialogButtonType {
        One, Two,
        Three;
    }
}

