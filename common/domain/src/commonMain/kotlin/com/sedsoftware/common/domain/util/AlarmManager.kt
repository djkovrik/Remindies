package com.sedsoftware.common.domain.util

import com.sedsoftware.common.domain.entity.Shot

interface AlarmManager {
    fun setAlarm(shot: Shot)
    fun cancelAlarm(shot: Shot)
}
