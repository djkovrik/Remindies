package com.sedsoftware.core.domain.extension

import com.sedsoftware.core.domain.constant.LeapDividers
import com.sedsoftware.core.domain.constant.MonthDays
import com.sedsoftware.core.domain.constant.MonthNumbers
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
fun LocalDateTime.plusPeriod(period: RemindiePeriod, timeZone: TimeZone): LocalDateTime = when (period) {
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

@ExperimentalTime
fun LocalDateTime.sameDayAs(other: LocalDateTime): Boolean =
    year == other.year && dayOfYear == other.dayOfYear

fun Int.days(leap: Boolean): Int =
    when (this) {
        MonthNumbers.JANUARY -> MonthDays.JANUARY
        MonthNumbers.FEBRUARY -> if (leap) MonthDays.FEBRUARY_LEAP else MonthDays.FEBRUARY
        MonthNumbers.MARCH -> MonthDays.MARCH
        MonthNumbers.APRIL -> MonthDays.APRIL
        MonthNumbers.MAY -> MonthDays.MAY
        MonthNumbers.JUNE -> MonthDays.JUNE
        MonthNumbers.JULY -> MonthDays.JULY
        MonthNumbers.AUGUST -> MonthDays.AUGUST
        MonthNumbers.SEPTEMBER -> MonthDays.SEPTEMBER
        MonthNumbers.OCTOBER -> MonthDays.OCTOBER
        MonthNumbers.NOVEMBER -> MonthDays.NOVEMBER
        MonthNumbers.DECEMBER -> MonthDays.DECEMBER
        else -> error("Wrong month value")
    }

fun Int.isLeap(): Boolean =
    when {
        this % LeapDividers.LEAP_DIVIDER == 0 -> {
            when {
                this % LeapDividers.LEAP_YEAR_MOD1 == 0 -> this % LeapDividers.LEAP_YEAR_MOD2 == 0
                else -> true
            }
        }
        else -> false
    }
