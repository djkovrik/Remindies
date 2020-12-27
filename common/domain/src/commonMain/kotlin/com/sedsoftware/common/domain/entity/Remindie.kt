package com.sedsoftware.common.domain.entity

import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

data class Remindie(
    val id: Long = 0,
    val timestamp: Long,
    val created: LocalDateTime,
    val shot: LocalDateTime,
    val timeZone: TimeZone,
    val title: String,
    val type: RemindieType,
    val period: RemindiePeriod,
    val each: Int
)
