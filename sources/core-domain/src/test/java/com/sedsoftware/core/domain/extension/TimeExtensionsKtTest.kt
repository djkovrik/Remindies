package com.sedsoftware.core.domain.extension

import com.sedsoftware.core.domain.entity.Remindie
import com.sedsoftware.core.domain.entity.Shot
import com.sedsoftware.core.domain.type.RemindiePeriod
import com.sedsoftware.core.domain.type.RemindieType
import com.winterbe.expekt.should
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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
        val midday = LocalDateTime(year = 2021, monthNumber = 1, dayOfMonth = 14, hour = 1, minute = 2)

        val currentTimeZone: TimeZone = TimeZone.currentSystemDefault()

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
        firstDate.plusPeriod(RemindiePeriod.Hourly(1), currentTimeZone).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(31)
            hour.should.be.equal(23)
            minute.should.be.equal(59)
        }

        firstDate.plusPeriod(RemindiePeriod.Hourly(2), currentTimeZone).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(2)
            dayOfMonth.should.be.equal(1)
            hour.should.be.equal(0)
            minute.should.be.equal(59)
        }

        secondDate.plusPeriod(RemindiePeriod.Hourly(1), currentTimeZone).run {
            year.should.be.equal(2021)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(1)
            hour.should.be.equal(0)
            minute.should.be.equal(59)
        }
    }

    @Test
    fun `test plusPeriod for days`() {
        firstDate.plusPeriod(RemindiePeriod.Daily(3), currentTimeZone).run {
            year.should.be.equal(2020)
            monthNumber.should.be.equal(2)
            dayOfMonth.should.be.equal(3)
            hour.should.be.equal(22)
            minute.should.be.equal(59)
        }

        secondDate.plusPeriod(RemindiePeriod.Daily(3), currentTimeZone).run {
            year.should.be.equal(2021)
            monthNumber.should.be.equal(1)
            dayOfMonth.should.be.equal(3)
            hour.should.be.equal(23)
            minute.should.be.equal(59)
        }

        midday.plusPeriod(RemindiePeriod.Daily(3), currentTimeZone).run {
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
            firstDate.plusPeriod(RemindiePeriod.Weekly(i), currentTimeZone).run {
                dayOfWeek.should.be.equal(firstDate.dayOfWeek)
            }
            secondDate.plusPeriod(RemindiePeriod.Weekly(i), currentTimeZone).run {
                dayOfWeek.should.be.equal(secondDate.dayOfWeek)
            }
            midday.plusPeriod(RemindiePeriod.Weekly(i), currentTimeZone).run {
                dayOfWeek.should.be.equal(secondDate.dayOfWeek)
            }
        }
    }

    @Test
    fun `test plusPeriod for month`() {
        secondDate.plusPeriod(RemindiePeriod.Monthly(1), currentTimeZone).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(2), currentTimeZone).run { dayOfMonth.should.be.equal(28) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(3), currentTimeZone).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(4), currentTimeZone).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(5), currentTimeZone).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(6), currentTimeZone).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(7), currentTimeZone).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(8), currentTimeZone).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(9), currentTimeZone).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(10), currentTimeZone).run { dayOfMonth.should.be.equal(31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(11), currentTimeZone).run { dayOfMonth.should.be.equal(30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(12), currentTimeZone).run { dayOfMonth.should.be.equal(31) }

        midday.plusPeriod(RemindiePeriod.Monthly(1), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(2), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(3), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(4), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(5), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(6), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(7), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(8), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(9), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(10), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(11), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
        midday.plusPeriod(RemindiePeriod.Monthly(12), currentTimeZone).run { dayOfMonth.should.be.equal(14) }
    }

    @Test
    fun `test plusPeriod for years`() {
        for (i in 1..100) {
            firstDate.plusPeriod(RemindiePeriod.Yearly(i), currentTimeZone).run {
                year.should.be.equal(firstDate.year + i)
            }
            secondDate.plusPeriod(RemindiePeriod.Yearly(i), currentTimeZone).run {
                year.should.be.equal(secondDate.year + i)
            }
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

    @Test
    fun `toNearestShot test`() {
        val timeZone = TimeZone.currentSystemDefault()

        // Oneshot - not fired
        val today1 = LocalDateTime(2020, 11, 7, 13, 13)
        val created1 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot1 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie1 = Remindie(
            id = 1,
            created = created1,
            shot = shot1,
            timeZone = timeZone,
            title = "Oneshot - not fired",
            type = RemindieType.CALL,
            period = RemindiePeriod.None
        )

        remindie1.toNearestShot(today1).should.be.equal(
            Shot(
                remindie = remindie1,
                planned = shot1,
                isFired = false
            )
        )

        // Oneshot - fired
        val today2 = LocalDateTime(2020, 11, 7, 22, 13)
        val created2 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot2 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie2 = Remindie(
            id = 2,
            created = created2,
            shot = shot2,
            timeZone = timeZone,
            title = "Oneshot - fired",
            type = RemindieType.CALL,
            period = RemindiePeriod.None
        )

        remindie2.toNearestShot(today2).should.be.equal(
            Shot(
                remindie = remindie2,
                planned = shot2,
                isFired = true
            )
        )

        // Periodical
        val today3 = LocalDateTime(2020, 11, 7, 22, 13)
        val created3 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot3 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie3 = Remindie(
            id = 3,
            created = created3,
            shot = shot3,
            timeZone = timeZone,
            title = "Oneshot - hourly 3",
            type = RemindieType.CALL,
            period = RemindiePeriod.Hourly(3)
        )

        remindie3.toNearestShot(today3).should.be.equal(
            Shot(
                remindie = remindie3,
                planned = LocalDateTime(2020, 11, 8, 0, 33),
                isFired = false
            )
        )

        val today4 = LocalDateTime(2020, 11, 7, 22, 13)
        val created4 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot4 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie4 = Remindie(
            id = 4,
            created = created4,
            shot = shot4,
            timeZone = timeZone,
            title = "Oneshot - daily 2",
            type = RemindieType.CALL,
            period = RemindiePeriod.Daily(2)
        )

        remindie4.toNearestShot(today4).should.be.equal(
            Shot(
                remindie = remindie4,
                planned = LocalDateTime(2020, 11, 9, 18, 33),
                isFired = false
            )
        )

        val today5 = LocalDateTime(2020, 11, 7, 22, 13)
        val created5 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot5 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie5 = Remindie(
            id = 5,
            created = created5,
            shot = shot5,
            timeZone = timeZone,
            title = "Oneshot - weekly 2",
            type = RemindieType.CALL,
            period = RemindiePeriod.Weekly(2)
        )

        remindie5.toNearestShot(today5).should.be.equal(
            Shot(
                remindie = remindie5,
                planned = LocalDateTime(2020, 11, 21, 18, 33),
                isFired = false
            )
        )

        val today6 = LocalDateTime(2020, 11, 7, 22, 13)
        val created6 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot6 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie6 = Remindie(
            id = 6,
            created = created6,
            shot = shot6,
            timeZone = timeZone,
            title = "Oneshot - monthly 2",
            type = RemindieType.CALL,
            period = RemindiePeriod.Monthly(14)
        )

        remindie6.toNearestShot(today6).should.be.equal(
            Shot(
                remindie = remindie6,
                planned = LocalDateTime(2022, 1, 7, 18, 33),
                isFired = false
            )
        )
    }
}
