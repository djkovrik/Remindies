package com.sedsoftware.common.domain.extension

import com.sedsoftware.common.domain.constant.LeapDividers
import com.sedsoftware.common.domain.constant.MonthDays
import com.sedsoftware.common.domain.constant.MonthNumbers
import com.sedsoftware.common.domain.type.RemindiePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.days
import kotlin.time.hours

val Int.isLeap: Boolean
    get() = when {
        this % LeapDividers.LEAP_DIVIDER == 0 -> {
            when {
                this % LeapDividers.LEAP_YEAR_MOD1 == 0 -> this % LeapDividers.LEAP_YEAR_MOD2 == 0
                else -> true
            }
        }
        else -> false
    }

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

fun LocalDateTime.sameDayAs(other: LocalDateTime): Boolean =
    year == other.year && dayOfYear == other.dayOfYear

@ExperimentalTime
fun LocalDateTime.plusPeriod(period: RemindiePeriod, timeZone: TimeZone): LocalDateTime =
    when (period) {
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
            val yearModifier = (monthNumber + period.each) / Month.values().size
            val nextYear = year + yearModifier

            var nextMonthNumber = (monthNumber + period.each) % Month.values().size
            if (nextMonthNumber == 0) nextMonthNumber++

            val daysInNextMonth = nextMonthNumber.days(nextYear.isLeap)
            val dayOfNextMonth = if (dayOfMonth > daysInNextMonth) daysInNextMonth else dayOfMonth

            LocalDateTime(nextYear, nextMonthNumber, dayOfNextMonth, hour, minute, second, nanosecond)
        }
        is RemindiePeriod.Yearly -> {
            val nextYear = year + period.each

            // leap year hack
            when {
                // current is leap - next is leap
                year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY_LEAP && nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY_LEAP, hour, minute, second, nanosecond)
                // current is leap - next is non leap
                year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY_LEAP && !nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY, hour, minute, second, nanosecond)
                // current is non leap - next is leap
                !year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY && nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY_LEAP, hour, minute, second, nanosecond)
                // current is non leap - next is non leap
                !year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY && !nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY, hour, minute, second, nanosecond)
                else ->
                    LocalDateTime(nextYear, month, dayOfMonth, hour, minute, second, nanosecond)
            }
        }
        is RemindiePeriod.None -> this
    }
