package com.sedsoftware.common.tools.base

import com.sedsoftware.common.domain.entity.Shot

interface RemindieAlarmManager {
    fun setAlarm(shot: Shot)
    fun cancelAlarm(shot: Shot)
}
