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
    }

    @Test
    fun `leap year test`() {
        2020.isLeap().should.be.equal(true)
        2021.isLeap().should.be.equal(false)
    }

    @Test
    fun `test plusPeriod for hours`() {

        // Asserts
        firstDate.plusPeriod(RemindiePeriod.Hourly(1)).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(31)
            hour.should.be.equal(23)
            minute.should.be.equal(59)
        }

        firstDate.plusPeriod(RemindiePeriod.Hourly(2)).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(2)
            dayOfMonth.should.be.equal(1)
            hour.should.be.equal(0)
            minute.should.be.equal(59)
        }

        secondDate.plusPeriod(RemindiePeriod.Hourly(1)).run {
            year.should.be.equal(2021)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(1)
            hour.should.be.equal(0)
            minute.should.be.equal(59)
        }
    }

    @Test
    fun `test plusPeriod for days`() {
        firstDate.plusPeriod(RemindiePeriod.Daily(3)).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(2)
            dayOfMonth.should.be.equal(3)
            hour.should.be.equal(22)
            minute.should.be.equal(59)
        }

        secondDate.plusPeriod(RemindiePeriod.Daily(3)).run {
            year.should.be.equal(2021)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(3)
            hour.should.be.equal(23)
            minute.should.be.equal(59)
        }
    }

    @Test
    fun `test plusPeriod for weeks`() {
        for (i in 1..100) {
            firstDate.plusPeriod(RemindiePeriod.Weekly(i)).run { dayOfWeek.should.be.equal(firstDate.dayOfWeek) }
            secondDate.plusPeriod(RemindiePeriod.Weekly(i)).run { dayOfWeek.should.be.equal(secondDate.dayOfWeek) }
        }
    }

    @Test
    fun `test plusPeriod for month`() {
        secondDate.plusPeriod(RemindiePeriod.Monthly(1)).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(2)).run { dayOfMonth.should.be.equal(29) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(3)).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(4)).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(5)).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(6)).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(7)).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(8)).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(9)).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(10)).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(11)).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(12)).run { dayOfMonth.should.be.equal(31) }
    }

    @Test
    fun `test plusPeriod for years`() {
        for (i in 1..100) {
            firstDate.plusPeriod(RemindiePeriod.Yearly(i)).run { year.should.be.equal(firstDate.year + i) }
            secondDate.plusPeriod(RemindiePeriod.Yearly(i)).run { year.should.be.equal(secondDate.year + i) }
        }
    }
}
