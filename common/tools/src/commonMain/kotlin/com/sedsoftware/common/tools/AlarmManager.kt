package com.sedsoftware.common.tools

import com.sedsoftware.common.domain.entity.Shot

interface AlarmManager {
    fun setAlarm(shot: Shot)
    fun cancelAlarm(shot: Shot)
}
