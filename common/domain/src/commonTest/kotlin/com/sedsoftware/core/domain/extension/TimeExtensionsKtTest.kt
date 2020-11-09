package com.sedsoftware.core.domain.extension

import com.sedsoftware.common.domain.entity.Remindie
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.domain.extension.isLeap
import com.sedsoftware.common.domain.extension.plusPeriod
import com.sedsoftware.common.domain.extension.sameDayAs
import com.sedsoftware.common.domain.extension.toNearestShot
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
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
        assertTrue { 2020.isLeap }
        assertFalse { 2021.isLeap }
    }

    @Test
    fun `test plusPeriod for hours`() {

        // Asserts
        firstDate.plusPeriod(RemindiePeriod.Hourly(1), currentTimeZone).run {
            assertEquals(year, 2020)
            assertEquals(monthNumber, 1)
            assertEquals(dayOfMonth, 31)
            assertEquals(hour, 23)
            assertEquals(minute, 59)
        }

        firstDate.plusPeriod(RemindiePeriod.Hourly(2), currentTimeZone).run {
            assertEquals(year, 2020)
            assertEquals(monthNumber, 2)
            assertEquals(dayOfMonth, 1)
            assertEquals(hour, 0)
            assertEquals(minute, 59)
        }

        secondDate.plusPeriod(RemindiePeriod.Hourly(1), currentTimeZone).run {
            assertEquals(year, 2021)
            assertEquals(monthNumber, 1)
            assertEquals(dayOfMonth, 1)
            assertEquals(hour, 0)
            assertEquals(minute, 59)
        }
    }

    @Test
    fun `test plusPeriod for days`() {
        firstDate.plusPeriod(RemindiePeriod.Daily(3), currentTimeZone).run {
            assertEquals(year, 2020)
            assertEquals(monthNumber, 2)
            assertEquals(dayOfMonth, 3)
            assertEquals(hour, 22)
            assertEquals(minute, 59)
        }

        secondDate.plusPeriod(RemindiePeriod.Daily(3), currentTimeZone).run {
            assertEquals(year, 2021)
            assertEquals(monthNumber, 1)
            assertEquals(dayOfMonth, 3)
            assertEquals(hour, 23)
            assertEquals(minute, 59)
        }

        midday.plusPeriod(RemindiePeriod.Daily(3), currentTimeZone).run {
            assertEquals(year, 2021)
            assertEquals(monthNumber, 1)
            assertEquals(dayOfMonth, 17)
            assertEquals(hour, 1)
            assertEquals(minute, 2)
        }
    }

    @Test
    fun `test plusPeriod for weeks`() {
        for (i in 1..100) {
            firstDate.plusPeriod(RemindiePeriod.Weekly(i), currentTimeZone).run {
                assertEquals(dayOfWeek, firstDate.dayOfWeek)
            }
            secondDate.plusPeriod(RemindiePeriod.Weekly(i), currentTimeZone).run {
                assertEquals(dayOfWeek, secondDate.dayOfWeek)
            }
            midday.plusPeriod(RemindiePeriod.Weekly(i), currentTimeZone).run {
                assertEquals(dayOfWeek, midday.dayOfWeek)
            }
        }
    }

    @Test
    fun `test plusPeriod for month`() {
        secondDate.plusPeriod(RemindiePeriod.Monthly(1), currentTimeZone).run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(2), currentTimeZone).run { assertEquals(dayOfMonth, 28) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(3), currentTimeZone).run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(4), currentTimeZone).run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(5), currentTimeZone).run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(6), currentTimeZone).run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(7), currentTimeZone).run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(8), currentTimeZone).run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(9), currentTimeZone).run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(10), currentTimeZone).run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(11), currentTimeZone).run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(12), currentTimeZone).run { assertEquals(dayOfMonth, 31) }

        midday.plusPeriod(RemindiePeriod.Monthly(1), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(2), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(3), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(4), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(5), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(6), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(7), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(8), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(9), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(10), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(11), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(12), currentTimeZone).run { assertEquals(dayOfMonth, 14) }
    }

    @Test
    fun `test plusPeriod for years`() {
        for (i in 1..100) {
            firstDate.plusPeriod(RemindiePeriod.Yearly(i), currentTimeZone).run {
                assertEquals(year, firstDate.year + i)
            }
            secondDate.plusPeriod(RemindiePeriod.Yearly(i), currentTimeZone).run {
                assertEquals(year, secondDate.year + i)
            }
            midday.plusPeriod(RemindiePeriod.Yearly(i), currentTimeZone).run {
                assertEquals(year, midday.year + i)
            }
        }
    }

    @Test
    fun `test sameDayAs checker`() {
        assertEquals(today1.sameDayAs(today2), true)
        assertEquals(today1.sameDayAs(tomorrow1), false)
        assertEquals(today1.sameDayAs(tomorrow2), false)
        assertEquals(today2.sameDayAs(today1), true)
        assertEquals(today2.sameDayAs(tomorrow1), false)
        assertEquals(today2.sameDayAs(tomorrow2), false)
        assertEquals(tomorrow1.sameDayAs(tomorrow2), true)
        assertEquals(tomorrow1.sameDayAs(today1), false)
        assertEquals(tomorrow1.sameDayAs(today2), false)
        assertEquals(tomorrow2.sameDayAs(tomorrow1), true)
        assertEquals(tomorrow2.sameDayAs(today1), false)
        assertEquals(tomorrow2.sameDayAs(today2), false)
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

        assertEquals(
            remindie1.toNearestShot(today1),
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

        assertEquals(
            remindie2.toNearestShot(today2),
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

        assertEquals(
            remindie3.toNearestShot(today3),
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

        assertEquals(
            remindie4.toNearestShot(today4),
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

        assertEquals(
            remindie5.toNearestShot(today5),
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

        assertEquals(
            remindie6.toNearestShot(today6),
            Shot(
                remindie = remindie6,
                planned = LocalDateTime(2022, 1, 7, 18, 33),
                isFired = false
            )
        )
    }
}
