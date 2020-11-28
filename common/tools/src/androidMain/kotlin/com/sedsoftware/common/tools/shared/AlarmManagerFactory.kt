package com.sedsoftware.common.tools.shared

import android.app.AlarmManager
import android.content.Context
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.tools.base.RemindieAlarmManager

@Suppress("FunctionName")
fun RemindiesAlarmManager(context: Context): RemindieAlarmManager =
    object : RemindieAlarmManager {

        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        override fun setAlarm(shot: Shot) {
            TODO("Not yet implemented")
        }

        override fun cancelAlarm(shot: Shot) {
            TODO("Not yet implemented")
        }
    }
