package kotlinx.datetime

import com.sedsoftware.common.domain.constant.MonthDays
import com.sedsoftware.common.domain.constant.MonthNumbers
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.primitive.days
import com.sedsoftware.common.primitive.isLeap
import kotlin.time.ExperimentalTime
import kotlin.time.days

fun LocalDateTime.sameDayAs(other: LocalDateTime): Boolean =
    year == other.year && dayOfYear == other.dayOfYear

@ExperimentalTime
fun LocalDateTime.sameWeekAs(
    other: LocalDateTime,
    startFromSunday: Boolean = false
): Boolean {
    val timeZone = TimeZone.currentSystemDefault()

    val startFrom = if (startFromSunday) DayOfWeek.SUNDAY else DayOfWeek.MONDAY
    val endWith = if (startFromSunday) DayOfWeek.SATURDAY else DayOfWeek.SUNDAY

    var lowerBound = this
    while (lowerBound.dayOfWeek.getOrdinal(startFromSunday) > startFrom.getOrdinal(startFromSunday)) {
        lowerBound = lowerBound.toInstant(timeZone).minus(1.days).toLocalDateTime(timeZone)
    }

    var upperBound = this
    while (upperBound.dayOfWeek.getOrdinal(startFromSunday) < endWith.getOrdinal(startFromSunday)) {
        upperBound = upperBound.toInstant(timeZone).plus(1.days).toLocalDateTime(timeZone)
    }

    val lower = lowerBound.shiftToStart()
    val upper = upperBound.shiftToEnd()

    return other in lower..upper
}

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

fun LocalDateTime.moveToZone(from: TimeZone, to: TimeZone): LocalDateTime =
    toInstant(from).toLocalDateTime(to)

@Suppress("MagicNumber")
private fun DayOfWeek.getOrdinal(fromSunday: Boolean): Int =
    when (this) {
        DayOfWeek.MONDAY -> if (fromSunday) 2 else 1
        DayOfWeek.TUESDAY -> if (fromSunday) 3 else 2
        DayOfWeek.WEDNESDAY -> if (fromSunday) 4 else 3
        DayOfWeek.THURSDAY -> if (fromSunday) 5 else 4
        DayOfWeek.FRIDAY -> if (fromSunday) 6 else 5
        DayOfWeek.SATURDAY -> if (fromSunday) 7 else 6
        DayOfWeek.SUNDAY -> if (fromSunday) 1 else 7
    }

private fun LocalDateTime.shiftToStart(): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, 0, 0)

@Suppress("MagicNumber")
private fun LocalDateTime.shiftToEnd(): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, 23, 59)