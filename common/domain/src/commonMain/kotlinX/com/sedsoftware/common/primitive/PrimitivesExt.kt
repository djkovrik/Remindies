package com.sedsoftware.common.primitive

import com.sedsoftware.common.domain.constant.LeapDividers
import com.sedsoftware.common.domain.constant.MonthDays
import com.sedsoftware.common.domain.constant.MonthNumbers

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

@Suppress("ComplexMethod")
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
