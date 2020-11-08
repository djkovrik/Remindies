package com.sedsoftware.core.domain.util

import com.sedsoftware.core.domain.entity.Shot

interface AlarmManager {
    suspend fun setAlarm(shot: Shot)
    suspend fun cancelAlarm(shot: Shot)
}
