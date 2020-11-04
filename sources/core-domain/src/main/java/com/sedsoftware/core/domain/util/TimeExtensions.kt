package com.sedsoftware.core.domain.util

import com.sedsoftware.core.domain.type.RemindiePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.hours

val Any.timeZone: TimeZone
    get() = TimeZone.currentSystemDefault()

@ExperimentalTime
fun LocalDateTime.plusPeriod(period: RemindiePeriod): LocalDateTime = when (period) {
    is RemindiePeriod.Hourly -> {
        toInstant(timeZone).plus(period.each.hours).toLocalDateTime(timeZone)
    }
    is RemindiePeriod.Daily -> {
        toInstant(timeZone).plus(period.each.days).toLocalDateTime(timeZone)
    }
    is RemindiePeriod.Weekly -> {
        toInstant(timeZone).plus(period.each.days * DayOfWeek.values().size).toLocalDateTime(timeZone)
    }
    is RemindiePeriod.Monthly -> {
        val nextMonth = month + period.each
        LocalDateTime(year, nextMonth, dayOfMonth, hour, minute, second, nanosecond)
    }
    is RemindiePeriod.Yearly -> {
        val nextYear = year + 1
        LocalDateTime(nextYear, month, dayOfMonth, hour, minute, second, nanosecond)
    }
    is RemindiePeriod.None -> this
}
