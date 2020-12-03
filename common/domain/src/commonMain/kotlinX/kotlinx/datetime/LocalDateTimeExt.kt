@file:Suppress("MagicNumber")

package kotlinx.datetime

import com.sedsoftware.common.domain.constant.MonthDays
import com.sedsoftware.common.domain.constant.MonthNumbers
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.primitive.days
import com.sedsoftware.common.primitive.isLeap
import kotlin.time.ExperimentalTime
import kotlin.time.days

fun LocalDateTime.sameDayAs(other: LocalDateTime): Boolean =
    sameYearAs(other) && dayOfYear == other.dayOfYear

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

    val lower = lowerBound.getDayStart()
    val upper = upperBound.getDayEnd()

    return other in lower..upper
}

fun LocalDateTime.sameMonthAs(other: LocalDateTime): Boolean =
    sameYearAs(other) && month == other.month

fun LocalDateTime.sameYearAs(other: LocalDateTime): Boolean =
    year == other.year

@Suppress("ComplexMethod")
fun LocalDateTime.plusPeriod(period: RemindiePeriod, each: Int, timeZone: TimeZone): LocalDateTime =
    when (period) {
        RemindiePeriod.HOURLY -> {
            toInstant(timeZone).plus(each, DateTimeUnit.HOUR, timeZone).toLocalDateTime(timeZone)
        }
        RemindiePeriod.DAILY -> {
            toInstant(timeZone).plus(each, DateTimeUnit.DAY, timeZone).toLocalDateTime(timeZone)
        }
        RemindiePeriod.WEEKLY -> {
            toInstant(timeZone).plus(each, DateTimeUnit.WEEK, timeZone).toLocalDateTime(timeZone)
        }
        RemindiePeriod.MONTHLY -> {
            val yearModifier = (monthNumber + each) / Month.values().size
            val nextYear = year + yearModifier

            var nextMonthNumber = (monthNumber + each) % Month.values().size
            if (nextMonthNumber == 0) nextMonthNumber++

            val daysInNextMonth = nextMonthNumber.days(nextYear.isLeap)
            val dayOfNextMonth = if (dayOfMonth > daysInNextMonth) daysInNextMonth else dayOfMonth

            LocalDateTime(nextYear, nextMonthNumber, dayOfNextMonth, hour, minute)
        }
        RemindiePeriod.YEARLY -> {
            val nextYear = year + each

            // leap year hack
            when {
                // current is leap - next is leap
                year.isLeap &&
                        monthNumber == MonthNumbers.FEBRUARY &&
                        dayOfMonth == MonthDays.FEBRUARY_LEAP &&
                        nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY_LEAP, hour, minute)

                // current is leap - next is non leap
                year.isLeap &&
                        monthNumber == MonthNumbers.FEBRUARY &&
                        dayOfMonth == MonthDays.FEBRUARY_LEAP &&
                        !nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY, hour, minute)

                // current is non leap - next is leap
                !year.isLeap &&
                        monthNumber == MonthNumbers.FEBRUARY &&
                        dayOfMonth == MonthDays.FEBRUARY &&
                        nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY_LEAP, hour, minute)

                // current is non leap - next is non leap
                !year.isLeap &&
                        monthNumber == MonthNumbers.FEBRUARY &&
                        dayOfMonth == MonthDays.FEBRUARY &&
                        !nextYear.isLeap ->
                    LocalDateTime(nextYear, month, MonthDays.FEBRUARY, hour, minute)

                else -> LocalDateTime(nextYear, month, dayOfMonth, hour, minute)
            }
        }
        RemindiePeriod.NONE -> this
    }

fun LocalDateTime.moveToZone(from: TimeZone, to: TimeZone): LocalDateTime =
    toInstant(from).toLocalDateTime(to)

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

fun LocalDateTime.getDayStart(): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, 0, 0)

fun LocalDateTime.getDayEnd(): LocalDateTime =
    LocalDateTime(year, month, dayOfMonth, 23, 59)

@ExperimentalTime
fun LocalDateTime.getWeekStart(fromSunday: Boolean = false): LocalDateTime {
    val current = dayOfWeek.getOrdinal(fromSunday)
    val diff = current - 1

    val timeZone = TimeZone.currentSystemDefault()
    val weekStart = toInstant(timeZone).minus(diff.days).toLocalDateTime(timeZone)

    return LocalDateTime(weekStart.year, weekStart.month, weekStart.dayOfMonth, 0, 0)
}

@ExperimentalTime
fun LocalDateTime.getWeekEnd(fromSunday: Boolean = false): LocalDateTime {
    val current = dayOfWeek.getOrdinal(fromSunday)
    val diff = 7 - current

    val timeZone = TimeZone.currentSystemDefault()
    val weekStart = toInstant(timeZone).plus(diff.days).toLocalDateTime(timeZone)

    return LocalDateTime(weekStart.year, weekStart.month, weekStart.dayOfMonth, 23, 59)
}

fun LocalDateTime.getMonthStart(): LocalDateTime =
    LocalDateTime(year, month, 1, 0, 0)

fun LocalDateTime.getMonthEnd(): LocalDateTime =
    LocalDateTime(year, month, monthNumber.days(year.isLeap), 23, 59)

fun LocalDateTime.getYearStart(): LocalDateTime =
    LocalDateTime(year, 1, 1, 0, 0)

fun LocalDateTime.getYearEnd(): LocalDateTime =
    LocalDateTime(year, 12, 31, 23, 59)

fun LocalDateTime?.available(): Boolean = this != null
