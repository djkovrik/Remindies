package com.sedsoftware.common.tools.shared

import android.content.Context
import com.sedsoftware.common.tools.base.RemindieSettings

@Suppress("FunctionName")
fun RemindiesSettings(context: Context): RemindieSettings =
    object : RemindieSettings {

        private val preferences =
            context.applicationContext.getSharedPreferences("Remindies", Context.MODE_PRIVATE)

        override var timeZoneDependent: Boolean
            get() = preferences.getBoolean(TIME_ZONE_KEY, false)
            set(value) {
                preferences.edit().putBoolean(TIME_ZONE_KEY, value).apply()
            }

        override var startFromSunday: Boolean
            get() = preferences.getBoolean(START_FROM_SUNDAY_KEY, false)
            set(value) {
                preferences.edit().putBoolean(START_FROM_SUNDAY_KEY, value).apply()
            }

        private val TIME_ZONE_KEY = "pref_time_zone"
        private val START_FROM_SUNDAY_KEY = "pref_from_sunday"
    }
