package com.sedsoftware.core.domain.entity

import com.sedsoftware.core.domain.type.RemindiePeriod
import com.sedsoftware.core.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

data class Remindie(
    val id: Long,
    val created: LocalDateTime,
    val shot: LocalDateTime,
    val timeZone: TimeZone,
    val title: String,
    val type: RemindieType,
    val period: RemindiePeriod
)
