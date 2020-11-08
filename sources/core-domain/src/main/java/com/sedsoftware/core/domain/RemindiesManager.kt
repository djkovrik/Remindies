package com.sedsoftware.core.domain

import com.sedsoftware.core.domain.entity.Remindie
import com.sedsoftware.core.domain.exception.RemindieDeletionException
import com.sedsoftware.core.domain.exception.RemindieInsertionException
import com.sedsoftware.core.domain.exception.RemindieSchedulingException
import com.sedsoftware.core.domain.extension.toNearestShot
import com.sedsoftware.core.domain.repository.RemindiesRepository
import com.sedsoftware.core.domain.type.Outcome
import com.sedsoftware.core.domain.type.RemindiePeriod
import com.sedsoftware.core.domain.util.AlarmManager
import com.sedsoftware.core.domain.util.RemindieTypeChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@ExperimentalTime
interface RemindiesManager {
    val manager: AlarmManager
    val repository: RemindiesRepository
    val typeChecker: RemindieTypeChecker

    suspend fun add(title: String, date: LocalDateTime, period: RemindiePeriod): Outcome<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val timeZone = TimeZone.currentSystemDefault()
                val today = Clock.System.now().toLocalDateTime(timeZone)
                val todayAsLong = today.toInstant(timeZone).toEpochMilliseconds()

                val new = Remindie(
                    id = todayAsLong,
                    created = today,
                    shot = date,
                    timeZone = timeZone,
                    title = title,
                    type = typeChecker.getType(title),
                    period = period
                )

                repository.insert(new)

                Outcome.Success(Unit)
            } catch (exception: Exception) {
                Outcome.Error(RemindieInsertionException("Failed to insert new remindie", exception))
            }
        }

    suspend fun remove(remindie: Remindie): Outcome<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val today = Clock.System.now().toLocalDateTime(remindie.timeZone)
                val next = remindie.toNearestShot(today)
                manager.cancelAlarm(next)
                repository.delete(remindie)
                Outcome.Success(Unit)
            } catch (exception: Exception) {
                Outcome.Error(RemindieDeletionException("Failed to delete remindie", exception))
            }
        }

    suspend fun rescheduleNext(): Outcome<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val shots = repository.getAll()
                    .map { remindie ->
                        val today = Clock.System.now().toLocalDateTime(remindie.timeZone)
                        remindie.toNearestShot(today)
                    }
                    .filter { !it.isFired }
                    .sortedBy { it.planned }

                if (shots.isNotEmpty()) {
                    val next = shots.first()
                    manager.setAlarm(next)
                }

                Outcome.Success(Unit)
            } catch (exception: Exception) {
                Outcome.Error(RemindieSchedulingException("Failed to reschedule remindies", exception))
            }
        }
//    suspend fun getRemindiesForToday(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForDay(date: LocalDateTime): Outcome<List<Remindie>>
//    suspend fun getRemindiesForCurrentWeek(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForWeek(date: LocalDateTime): Outcome<List<Remindie>>
//    suspend fun getRemindiesForCurrentMonth(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForMonth(date: LocalDateTime): Outcome<List<Remindie>>
//    suspend fun getRemindiesForCurrentYear(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForYear(year: Int): Outcome<List<Remindie>>
}
