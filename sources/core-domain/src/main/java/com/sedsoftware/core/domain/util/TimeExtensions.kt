package com.sedsoftware.core.domain.util

import com.sedsoftware.core.domain.type.MonthDays
import com.sedsoftware.core.domain.type.MonthNumber
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
        var nextMonthValue = (monthNumber + period.each) % Month.values().size
        if (nextMonthValue == 0) nextMonthValue++
        val leap = year.isLeap()
        val daysInNextMonth = nextMonthValue.days(leap)
        val dayOfNextMonth = if (dayOfMonth > daysInNextMonth) daysInNextMonth else dayOfMonth

        LocalDateTime(year, month, dayOfNextMonth, hour, minute, second, nanosecond)
    }
    is RemindiePeriod.Yearly -> {
        val nextYear = year + period.each
        LocalDateTime(nextYear, month, dayOfMonth, hour, minute, second, nanosecond)
    }
    is RemindiePeriod.None -> this
}

fun Int.days(leap: Boolean): Int =
    when (this) {
        MonthNumber.JANUARY -> MonthDays.JANUARY
        MonthNumber.FEBRUARY -> if (leap) MonthDays.FEBRUARY_LEAP else MonthDays.FEBRUARY
        MonthNumber.MARCH -> MonthDays.MARCH
        MonthNumber.APRIL -> MonthDays.APRIL
        MonthNumber.MAY -> MonthDays.MAY
        MonthNumber.JUNE -> MonthDays.JUNE
        MonthNumber.JULY -> MonthDays.JULY
        MonthNumber.AUGUST -> MonthDays.AUGUST
        MonthNumber.SEPTEMBER -> MonthDays.SEPTEMBER
        MonthNumber.OCTOBER -> MonthDays.OCTOBER
        MonthNumber.NOVEMBER -> MonthDays.NOVEMBER
        MonthNumber.DECEMBER -> MonthDays.DECEMBER
        else -> error("Wrong month value")
    }

fun Int.isLeap(): Boolean =
    when {
        this % TimeConstants.LEAP_DIVIDER == 0 -> {
            when {
                this % TimeConstants.LEAP_YEAR_MOD1 == 0 -> this % TimeConstants.LEAP_YEAR_MOD2 == 0
                else -> true
            }
        }
        else -> false
    }
