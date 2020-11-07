package com.sedsoftware.core.domain.extension

import com.sedsoftware.core.domain.constant.LeapDividers
import com.sedsoftware.core.domain.constant.MonthDays
import com.sedsoftware.core.domain.constant.MonthNumbers
import kotlinx.datetime.LocalDateTime

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
