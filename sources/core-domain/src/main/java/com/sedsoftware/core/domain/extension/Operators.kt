package com.sedsoftware.core.domain.extension

import com.sedsoftware.core.domain.type.RemindiePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.hours

@ExperimentalTime
operator fun LocalDateTime.plus(period: RemindiePeriod): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    return when (period) {
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
            var nextMonthValue = (monthNumber + period.each) % Month.values().size
            if (nextMonthValue == 0) nextMonthValue++
            val daysInNextMonth = nextMonthValue.days(year.isLeap)
            val dayOfNextMonth = if (dayOfMonth > daysInNextMonth) daysInNextMonth else dayOfMonth

            LocalDateTime(year, month, dayOfNextMonth, hour, minute, second, nanosecond)
        }
        is RemindiePeriod.Yearly -> {
            val nextYear = year + period.each
            LocalDateTime(nextYear, month, dayOfMonth, hour, minute, second, nanosecond)
        }
        is RemindiePeriod.None -> this
    }
}
