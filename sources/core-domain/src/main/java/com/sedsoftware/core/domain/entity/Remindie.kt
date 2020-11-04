package com.sedsoftware.core.domain.entity

import com.sedsoftware.core.domain.type.RemindiePeriod
import com.sedsoftware.core.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime

data class Remindie(
    val created: Long,
    val title: String,
    val shoot: LocalDateTime,
    val type: RemindieType,
    val periodical: Boolean = false,
    val period: RemindiePeriod = RemindiePeriod.None
)
