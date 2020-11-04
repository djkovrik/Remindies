package com.sedsoftware.core.domain.util

import com.sedsoftware.core.domain.entity.Remindie

interface AlarmController {
    suspend fun setAlarm(remindie: Remindie)
    suspend fun cancelAlarm(remindie: Remindie)
}
