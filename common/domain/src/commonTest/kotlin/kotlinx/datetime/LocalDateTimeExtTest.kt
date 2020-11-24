package kotlinx.datetime

import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.primitive.days
import com.sedsoftware.common.primitive.isLeap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

@ExperimentalTime
class LocalDateTimeExtTest {
    companion object {
        // 31.01.2020 22:59
        val firstDate =
            LocalDateTime(year = 2020, monthNumber = 1, dayOfMonth = 31, hour = 22, minute = 59)

        // 31.12.2020 23:59
        val secondDate =
            LocalDateTime(year = 2020, monthNumber = 12, dayOfMonth = 31, hour = 23, minute = 59)

        // 14.01.2021 01:02
        val midday =
            LocalDateTime(year = 2021, monthNumber = 1, dayOfMonth = 14, hour = 1, minute = 2)

        val currentTimeZone: TimeZone = TimeZone.currentSystemDefault()

        val today1 =
            LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 5, hour = 22, minute = 59)
        val today2 =
            LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 5, hour = 23, minute = 59)
        val tomorrow1 =
            LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 6, hour = 0, minute = 1)
        val tomorrow2 =
            LocalDateTime(year = 2020, monthNumber = 11, dayOfMonth = 6, hour = 23, minute = 59)
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
    fun `test plusPeriod for months`() {
        secondDate.plusPeriod(RemindiePeriod.Monthly(1), currentTimeZone)
            .run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(2), currentTimeZone)
            .run { assertEquals(dayOfMonth, 28) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(3), currentTimeZone)
            .run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(4), currentTimeZone)
            .run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(5), currentTimeZone)
            .run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(6), currentTimeZone)
            .run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(7), currentTimeZone)
            .run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(8), currentTimeZone)
            .run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(9), currentTimeZone)
            .run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(10), currentTimeZone)
            .run { assertEquals(dayOfMonth, 31) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(11), currentTimeZone)
            .run { assertEquals(dayOfMonth, 30) }
        secondDate.plusPeriod(RemindiePeriod.Monthly(12), currentTimeZone)
            .run { assertEquals(dayOfMonth, 31) }

        midday.plusPeriod(RemindiePeriod.Monthly(1), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(2), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(3), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(4), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(5), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(6), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(7), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(8), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(9), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(10), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(11), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
        midday.plusPeriod(RemindiePeriod.Monthly(12), currentTimeZone)
            .run { assertEquals(dayOfMonth, 14) }
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
    fun `test sameWeekAs`() {
        val sunday1 = LocalDateTime(2020, 11, 29, 1, 2)
        val monday1 = LocalDateTime(2020, 11, 30, 3, 4)
        val tuesday = LocalDateTime(2020, 12, 1, 5, 6)
        val wednesday = LocalDateTime(2020, 12, 2, 7, 8)
        val thursday = LocalDateTime(2020, 12, 3, 9, 10)
        val friday = LocalDateTime(2020, 12, 4, 11, 12)
        val saturday = LocalDateTime(2020, 12, 5, 13, 14)
        val sunday2 = LocalDateTime(2020, 12, 6, 15, 16)
        val monday2 = LocalDateTime(2020, 12, 7, 17, 18)

        assertFalse { sunday1.sameWeekAs(monday1) }
        assertFalse { sunday1.sameWeekAs(tuesday) }
        assertFalse { sunday1.sameWeekAs(wednesday) }
        assertFalse { sunday1.sameWeekAs(thursday) }
        assertFalse { sunday1.sameWeekAs(friday) }
        assertFalse { sunday1.sameWeekAs(saturday) }
        assertFalse { sunday1.sameWeekAs(sunday2) }
        assertFalse { sunday1.sameWeekAs(monday2) }
        assertFalse { sunday2.sameWeekAs(monday2) }

        assertTrue { monday1.sameWeekAs(tuesday) }
        assertTrue { monday1.sameWeekAs(wednesday) }
        assertTrue { monday1.sameWeekAs(thursday) }
        assertTrue { monday1.sameWeekAs(friday) }
        assertTrue { monday1.sameWeekAs(saturday) }
        assertTrue { monday1.sameWeekAs(sunday2) }

        assertTrue { tuesday.sameWeekAs(wednesday) }
        assertTrue { tuesday.sameWeekAs(thursday) }
        assertTrue { tuesday.sameWeekAs(friday) }
        assertTrue { tuesday.sameWeekAs(saturday) }
        assertTrue { tuesday.sameWeekAs(sunday2) }

        assertTrue { wednesday.sameWeekAs(thursday) }
        assertTrue { wednesday.sameWeekAs(friday) }
        assertTrue { wednesday.sameWeekAs(saturday) }
        assertTrue { wednesday.sameWeekAs(sunday2) }

        assertTrue { thursday.sameWeekAs(friday) }
        assertTrue { thursday.sameWeekAs(saturday) }
        assertTrue { thursday.sameWeekAs(sunday2) }

        assertTrue { friday.sameWeekAs(saturday) }
        assertTrue { friday.sameWeekAs(sunday2) }

        assertTrue { saturday.sameWeekAs(sunday2) }
    }

    @Test
    fun `test sameWeekAs with sunday as first day`() {
        val sunday1 = LocalDateTime(2020, 11, 29, 1, 2)
        val monday1 = LocalDateTime(2020, 11, 30, 3, 4)
        val tuesday = LocalDateTime(2020, 12, 1, 5, 6)
        val wednesday = LocalDateTime(2020, 12, 2, 7, 8)
        val thursday = LocalDateTime(2020, 12, 3, 9, 10)
        val friday = LocalDateTime(2020, 12, 4, 11, 12)
        val saturday = LocalDateTime(2020, 12, 5, 13, 14)
        val sunday2 = LocalDateTime(2020, 12, 6, 15, 16)

        assertFalse { saturday.sameWeekAs(sunday2, true) }
        assertFalse { sunday2.sameWeekAs(saturday, true) }

        assertTrue { sunday1.sameWeekAs(monday1, true) }
        assertTrue { sunday1.sameWeekAs(tuesday, true) }
        assertTrue { sunday1.sameWeekAs(wednesday, true) }
        assertTrue { sunday1.sameWeekAs(thursday, true) }
        assertTrue { sunday1.sameWeekAs(friday, true) }
        assertTrue { sunday1.sameWeekAs(saturday, true) }

        assertTrue { monday1.sameWeekAs(tuesday, true) }
        assertTrue { monday1.sameWeekAs(wednesday, true) }
        assertTrue { monday1.sameWeekAs(thursday, true) }
        assertTrue { monday1.sameWeekAs(friday, true) }
        assertTrue { monday1.sameWeekAs(saturday, true) }

        assertTrue { tuesday.sameWeekAs(wednesday, true) }
        assertTrue { tuesday.sameWeekAs(thursday, true) }
        assertTrue { tuesday.sameWeekAs(friday, true) }
        assertTrue { tuesday.sameWeekAs(saturday, true) }

        assertTrue { wednesday.sameWeekAs(thursday, true) }
        assertTrue { wednesday.sameWeekAs(friday, true) }
        assertTrue { wednesday.sameWeekAs(saturday, true) }

        assertTrue { thursday.sameWeekAs(friday, true) }
        assertTrue { thursday.sameWeekAs(saturday, true) }

        assertTrue { friday.sameWeekAs(saturday, true) }
    }

    @Test
    fun `sameMonthAs test`() {
        val date1 = LocalDateTime(2020, 11, 2, 1, 2)
        val date2 = LocalDateTime(2020, 11, 30, 3, 4)
        val date3 = LocalDateTime(2020, 12, 17, 9, 10)

        assertTrue { date1.sameMonthAs(date2) }
        assertTrue { date2.sameMonthAs(date1) }

        assertFalse { date1.sameMonthAs(date3) }
        assertFalse { date2.sameMonthAs(date3) }

        assertFalse { date3.sameMonthAs(date1) }
        assertFalse { date3.sameMonthAs(date1) }
    }

    @Test
    fun `sameYearAs test`() {
        val date1 = LocalDateTime(2020, 11, 2, 1, 2)
        val date2 = LocalDateTime(2020, 11, 30, 3, 4)
        val date3 = LocalDateTime(2077, 12, 17, 9, 10)

        assertTrue { date1.sameYearAs(date2) }
        assertTrue { date2.sameYearAs(date1) }

        assertFalse { date1.sameYearAs(date3) }
        assertFalse { date2.sameYearAs(date3) }

        assertFalse { date3.sameYearAs(date1) }
        assertFalse { date3.sameYearAs(date1) }
    }

    @Test
    fun `moveToZone test`() {
        val baseDate = LocalDateTime(2020, 11, 10, 1, 23)

        for (i in 1..12) {
            baseDate.moveToZone(TimeZone.of("GMT"), TimeZone.of("GMT+$i")).run {
                assertEquals(hour, 1 + i)
                assertEquals(minute, 23)
            }
        }
    }

    @Test
    fun `getWeekStart test`() {
        val sunday1 = LocalDateTime(2020, 12, 27, 1, 2)
        val monday1 = LocalDateTime(2020, 12, 28, 3, 4)
        val thursday = LocalDateTime(2020, 12, 31, 9, 10)
        val friday = LocalDateTime(2021, 1, 1, 11, 12)
        val sunday2 = LocalDateTime(2021, 1, 3, 15, 16)
        val monday2 = LocalDateTime(2021, 1, 4, 17, 18)

        assertEquals(sunday1.getWeekStart(true).year, 2020)
        assertEquals(sunday1.getWeekStart(true).monthNumber, 12)
        assertEquals(sunday1.getWeekStart(true).dayOfMonth, 27)

        assertEquals(monday1.getWeekStart(true).year, 2020)
        assertEquals(monday1.getWeekStart(true).monthNumber, 12)
        assertEquals(monday1.getWeekStart(true).dayOfMonth, 27)

        assertEquals(monday1.getWeekStart(false).year, 2020)
        assertEquals(monday1.getWeekStart(false).monthNumber, 12)
        assertEquals(monday1.getWeekStart(false).dayOfMonth, 28)

        assertEquals(thursday.getWeekStart(true).year, 2020)
        assertEquals(thursday.getWeekStart(true).monthNumber, 12)
        assertEquals(thursday.getWeekStart(true).dayOfMonth, 27)

        assertEquals(thursday.getWeekStart(false).year, 2020)
        assertEquals(thursday.getWeekStart(false).monthNumber, 12)
        assertEquals(thursday.getWeekStart(false).dayOfMonth, 28)

        assertEquals(friday.getWeekStart(true).year, 2020)
        assertEquals(friday.getWeekStart(true).monthNumber, 12)
        assertEquals(friday.getWeekStart(true).dayOfMonth, 27)

        assertEquals(friday.getWeekStart(false).year, 2020)
        assertEquals(friday.getWeekStart(false).monthNumber, 12)
        assertEquals(friday.getWeekStart(false).dayOfMonth, 28)

        assertEquals(sunday2.getWeekStart(false).year, 2020)
        assertEquals(sunday2.getWeekStart(false).monthNumber, 12)
        assertEquals(sunday2.getWeekStart(false).dayOfMonth, 28)

        assertEquals(monday2.getWeekStart(true).year, 2021)
        assertEquals(monday2.getWeekStart(true).monthNumber, 1)
        assertEquals(monday2.getWeekStart(true).dayOfMonth, 3)
    }

    @Test
    fun `getWeekEnd test`() {
        val sunday1 = LocalDateTime(2020, 12, 27, 1, 2)
        val monday1 = LocalDateTime(2020, 12, 28, 3, 4)
        val thursday = LocalDateTime(2020, 12, 31, 9, 10)

        assertEquals(sunday1.getWeekEnd(true).year, 2021)
        assertEquals(sunday1.getWeekEnd(true).monthNumber, 1)
        assertEquals(sunday1.getWeekEnd(true).dayOfMonth, 2)

        assertEquals(monday1.getWeekEnd(false).year, 2021)
        assertEquals(monday1.getWeekEnd(false).monthNumber, 1)
        assertEquals(monday1.getWeekEnd(false).dayOfMonth, 3)

        assertEquals(thursday.getWeekEnd(true).year, 2021)
        assertEquals(thursday.getWeekEnd(true).monthNumber, 1)
        assertEquals(thursday.getWeekEnd(true).dayOfMonth, 2)

        assertEquals(thursday.getWeekEnd(false).year, 2021)
        assertEquals(thursday.getWeekEnd(false).monthNumber, 1)
        assertEquals(thursday.getWeekEnd(false).dayOfMonth, 3)
    }

    @Test
    fun `getMonthStart test`() {
        val date1 = LocalDateTime(2020, 10, 1, 1, 2)
        val date2 = LocalDateTime(2020, 10, 16, 3, 4)
        val date3 = LocalDateTime(2020, 10, 29, 5, 6)

        assertEquals(date1.getMonthStart().year, date1.year)
        assertEquals(date2.getMonthStart().year, date1.year)
        assertEquals(date3.getMonthStart().year, date1.year)

        assertEquals(date1.getMonthStart().monthNumber, date1.monthNumber)
        assertEquals(date2.getMonthStart().monthNumber, date2.monthNumber)
        assertEquals(date3.getMonthStart().monthNumber, date3.monthNumber)

        assertEquals(date1.getMonthStart().dayOfMonth, 1)
        assertEquals(date2.getMonthStart().dayOfMonth, 1)
        assertEquals(date3.getMonthStart().dayOfMonth, 1)

        assertEquals(date1.getMonthStart().hour, 0)
        assertEquals(date2.getMonthStart().hour, 0)
        assertEquals(date3.getMonthStart().hour, 0)

        assertEquals(date1.getMonthStart().minute, 0)
        assertEquals(date2.getMonthStart().minute, 0)
        assertEquals(date3.getMonthStart().minute, 0)
    }

    @Test
    fun `getMonthEnd test`() {
        val date1 = LocalDateTime(2020, 1, 1, 1, 2)
        val date2 = LocalDateTime(2020, 2, 16, 3, 4)
        val date3 = LocalDateTime(2020, 3, 29, 5, 6)

        assertEquals(date1.getMonthEnd().year, date1.year)
        assertEquals(date2.getMonthEnd().year, date1.year)
        assertEquals(date3.getMonthEnd().year, date1.year)

        assertEquals(date1.getMonthEnd().monthNumber, date1.monthNumber)
        assertEquals(date2.getMonthEnd().monthNumber, date2.monthNumber)
        assertEquals(date3.getMonthEnd().monthNumber, date3.monthNumber)

        assertEquals(date1.getMonthEnd().dayOfMonth, date1.monthNumber.days(date1.year.isLeap))
        assertEquals(date2.getMonthEnd().dayOfMonth, date2.monthNumber.days(date2.year.isLeap))
        assertEquals(date3.getMonthEnd().dayOfMonth, date3.monthNumber.days(date3.year.isLeap))

        assertEquals(date1.getMonthEnd().hour, 23)
        assertEquals(date2.getMonthEnd().hour, 23)
        assertEquals(date3.getMonthEnd().hour, 23)

        assertEquals(date1.getMonthEnd().minute, 59)
        assertEquals(date2.getMonthEnd().minute, 59)
        assertEquals(date3.getMonthEnd().minute, 59)
    }
}
