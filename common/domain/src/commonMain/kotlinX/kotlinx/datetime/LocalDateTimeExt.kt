package kotlinx.datetime

import com.sedsoftware.common.domain.constant.MonthDays
import com.sedsoftware.common.domain.constant.MonthNumbers
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.primitive.days
import com.sedsoftware.common.primitive.isLeap
import kotlin.time.ExperimentalTime

fun LocalDateTime.sameDayAs(other: LocalDateTime): Boolean =
    year == other.year && dayOfYear == other.dayOfYear

@ExperimentalTime
fun LocalDateTime.plusPeriod(period: RemindiePeriod, timeZone: TimeZone): LocalDateTime =
    when (period) {
        is RemindiePeriod.Hourly -> {
            toInstant(timeZone).plus(period.each, DateTimeUnit.HOUR, timeZone).toLocalDateTime(timeZone)
        }
        is RemindiePeriod.Daily -> {
            toInstant(timeZone).plus(period.each, DateTimeUnit.DAY, timeZone).toLocalDateTime(timeZone)
        }
        is RemindiePeriod.Weekly -> {
            toInstant(timeZone).plus(period.each, DateTimeUnit.WEEK, timeZone).toLocalDateTime(timeZone)
        }
        is RemindiePeriod.Monthly -> {
            val yearModifier = (monthNumber + period.each) / Month.values().size
            val nextYear = year + yearModifier

            var nextMonthNumber = (monthNumber + period.each) % Month.values().size
            if (nextMonthNumber == 0) nextMonthNumber++

            val daysInNextMonth = nextMonthNumber.days(nextYear.isLeap)
            val dayOfNextMonth = if (dayOfMonth > daysInNextMonth) daysInNextMonth else dayOfMonth

            LocalDateTime(nextYear, nextMonthNumber, dayOfNextMonth, hour, minute)
        }
        is RemindiePeriod.Yearly -> {
            val nextYear = year + period.each

            // leap year hack
            when {
                // current is leap - next is leap
                year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY_LEAP && nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY_LEAP, hour, minute)
                // current is leap - next is non leap
                year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY_LEAP && !nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY, hour, minute)
                // current is non leap - next is leap
                !year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY && nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY_LEAP, hour, minute)
                // current is non leap - next is non leap
                !year.isLeap && monthNumber == MonthNumbers.FEBRUARY && dayOfMonth == MonthDays.FEBRUARY && !nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY, hour, minute)
                else ->
                    LocalDateTime(nextYear, month, dayOfMonth, hour, minute)
            }
        }
        is RemindiePeriod.None -> this
    }
