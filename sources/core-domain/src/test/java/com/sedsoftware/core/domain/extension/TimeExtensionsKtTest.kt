package com.sedsoftware.core.domain.extension

import com.sedsoftware.core.domain.type.RemindiePeriod
import com.winterbe.expekt.should
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class TimeExtensionsKtTest {

    companion object {
        // 31.01.2020 22:59
        val firstDate = LocalDateTime(year = 2020, monthNumber = 1, dayOfMonth = 31, hour = 22, minute = 59)

        // 31.12.2020 23:59
        val secondDate = LocalDateTime(year = 2020, monthNumber = 12, dayOfMonth = 31, hour = 23, minute = 59)

        // 14.01.2021 01:02
        val someWhereInTheMiddle = LocalDateTime(year = 2021, monthNumber = 1, dayOfMonth = 14, hour = 1, minute = 2)

        val today1 = LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 5, hour = 22, minute = 59)
        val today2 = LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 5, hour = 23, minute = 59)
        val tomorrow1 = LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 6, hour = 0, minute = 1)
        val tomorrow2 = LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 6, hour = 23, minute = 59)
    }

    @Test
    fun `leap year test`() {
        2020.isLeap.should.be.equal(true)
        2021.isLeap.should.be.equal(false)
    }

    @Test
    fun `test plusPeriod for hours`() {

        // Asserts
        (firstDate + RemindiePeriod.Hourly(1)).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(31)
            hour.should.be.equal(23)
            minute.should.be.equal(59)
        }

        (firstDate + RemindiePeriod.Hourly(2)).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(2)
            dayOfMonth.should.be.equal(1)
            hour.should.be.equal(0)
            minute.should.be.equal(59)
        }

        (secondDate + RemindiePeriod.Hourly(1)).run {
            year.should.be.equal(2021)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(1)
            hour.should.be.equal(0)
            minute.should.be.equal(59)
        }
    }

    @Test
    fun `test plusPeriod for days`() {
        (firstDate + RemindiePeriod.Daily(3)).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(2)
            dayOfMonth.should.be.equal(3)
            hour.should.be.equal(22)
            minute.should.be.equal(59)
        }

        (secondDate + RemindiePeriod.Daily(3)).run {
            year.should.be.equal(2021)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(3)
            hour.should.be.equal(23)
            minute.should.be.equal(59)
        }

        (someWhereInTheMiddle + RemindiePeriod.Daily(3)).run {
            year.should.be.equal(2021)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(17)
            hour.should.be.equal(1)
            minute.should.be.equal(2)
        }
    }

    @Test
    fun `test plusPeriod for weeks`() {
        for (i in 1..100) {
            (firstDate + RemindiePeriod.Weekly(i)).run {
                dayOfWeek.should.be.equal(firstDate.dayOfWeek)
            }
            (secondDate + RemindiePeriod.Weekly(i)).run {
                dayOfWeek.should.be.equal(secondDate.dayOfWeek)
            }
            (someWhereInTheMiddle + RemindiePeriod.Weekly(i)).run {
                dayOfWeek.should.be.equal(secondDate.dayOfWeek)
            }
        }
    }

    @Test
    fun `test plusPeriod for month`() {
        (secondDate + RemindiePeriod.Monthly(1)).run { dayOfMonth.should.be.equal(31) }
        (secondDate + RemindiePeriod.Monthly(2)).run { dayOfMonth.should.be.equal(29) }
        (secondDate + RemindiePeriod.Monthly(3)).run { dayOfMonth.should.be.equal(31) }
        (secondDate + RemindiePeriod.Monthly(4)).run { dayOfMonth.should.be.equal(30) }
        (secondDate + RemindiePeriod.Monthly(5)).run { dayOfMonth.should.be.equal(31) }
        (secondDate + RemindiePeriod.Monthly(6)).run { dayOfMonth.should.be.equal(30) }
        (secondDate + RemindiePeriod.Monthly(7)).run { dayOfMonth.should.be.equal(31) }
        (secondDate + RemindiePeriod.Monthly(8)).run { dayOfMonth.should.be.equal(31) }
        (secondDate + RemindiePeriod.Monthly(9)).run { dayOfMonth.should.be.equal(30) }
        (secondDate + RemindiePeriod.Monthly(10)).run { dayOfMonth.should.be.equal(31) }
        (secondDate + RemindiePeriod.Monthly(11)).run { dayOfMonth.should.be.equal(30) }
        (secondDate + RemindiePeriod.Monthly(12)).run { dayOfMonth.should.be.equal(31) }

        (someWhereInTheMiddle + RemindiePeriod.Monthly(1)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(2)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(3)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(4)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(5)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(6)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(7)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(8)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(9)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(10)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(11)).run { dayOfMonth.should.be.equal(14) }
        (someWhereInTheMiddle + RemindiePeriod.Monthly(12)).run { dayOfMonth.should.be.equal(14) }
    }

    @Test
    fun `test plusPeriod for years`() {
        for (i in 1..100) {
            (firstDate + RemindiePeriod.Yearly(i)).run { year.should.be.equal(firstDate.year + i) }
            (secondDate + RemindiePeriod.Yearly(i)).run { year.should.be.equal(secondDate.year + i) }
        }
    }

    @Test
    fun `test sameDayAs checker`() {
        today1.sameDayAs(today2).should.be.equal(true)
        today1.sameDayAs(tomorrow1).should.be.equal(false)
        today1.sameDayAs(tomorrow2).should.be.equal(false)
        today2.sameDayAs(today1).should.be.equal(true)
        today2.sameDayAs(tomorrow1).should.be.equal(false)
        today2.sameDayAs(tomorrow2).should.be.equal(false)
        tomorrow1.sameDayAs(tomorrow2).should.be.equal(true)
        tomorrow1.sameDayAs(today1).should.be.equal(false)
        tomorrow1.sameDayAs(today2).should.be.equal(false)
        tomorrow2.sameDayAs(tomorrow1).should.be.equal(true)
        tomorrow2.sameDayAs(today1).should.be.equal(false)
        tomorrow2.sameDayAs(today2).should.be.equal(false)
    }
}
