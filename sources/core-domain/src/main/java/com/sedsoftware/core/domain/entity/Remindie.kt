package com.sedsoftware.core.domain.entity

import com.sedsoftware.core.domain.type.ReminderPeriod
import com.sedsoftware.core.domain.type.ReminderType
import kotlinx.datetime.LocalDateTime

data class Remindie(
    val created: Long,
    val title: String,
    val shoot: LocalDateTime,
    val type: ReminderType,
    val periodical: Boolean = false,
    val period: ReminderPeriod = ReminderPeriod.None
)
