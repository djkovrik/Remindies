package com.sedsoftware.core.domain.util

import com.sedsoftware.core.domain.entity.Shot

interface AlarmManager {
    fun setAlarm(shot: Shot)
    fun cancelAlarm(shot: Shot)
}
