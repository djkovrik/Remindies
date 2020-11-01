package com.sedsoftware.core.domain.entity

import com.sedsoftware.core.domain.type.ReminderPeriod
import com.sedsoftware.core.domain.type.ReminderType
import kotlinx.datetime.LocalDateTime

data class Remindie(
    val title: String,
    val created: LocalDateTime,
    val fire: LocalDateTime,
    val type: ReminderType,
    val oneShoot: Boolean = true,
    val period: ReminderPeriod = ReminderPeriod.None
)
