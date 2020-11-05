package com.sedsoftware.core.domain

import com.sedsoftware.core.domain.entity.Remindie
import com.sedsoftware.core.domain.exception.RemindieDeletionException
import com.sedsoftware.core.domain.exception.RemindieInsertionException
import com.sedsoftware.core.domain.repository.RemindiesRepository
import com.sedsoftware.core.domain.type.Outcome
import com.sedsoftware.core.domain.type.RemindiePeriod
import com.sedsoftware.core.domain.util.AlarmManager
import com.sedsoftware.core.domain.util.RemindieTypeChecker
import com.sedsoftware.core.domain.util.TimeDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

interface RemindiesManager {
    val manager: AlarmManager
    val provider: TimeDataProvider
    val repository: RemindiesRepository
    val typeChecker: RemindieTypeChecker

    suspend fun add(title: String, date: LocalDateTime, period: RemindiePeriod): Outcome<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val now = Clock.System.now().toLocalDateTime(provider.timeZone)
                val nowLong = now.toInstant(provider.timeZone).toEpochMilliseconds()

                val new = Remindie(
                    id = nowLong,
                    created = now,
                    shot = date,
                    title = title,
                    type = typeChecker.getType(title),
                    period = period
                )

                repository.insert(new)

                Outcome.Success(Unit)
            } catch (exception: Exception) {
                Outcome.Error(RemindieInsertionException("Failed to add new remindie", exception))
            }
        }

    suspend fun remove(remindie: Remindie): Outcome<Unit> =
        withContext(Dispatchers.IO) {
            try {
                repository.delete(remindie)
                Outcome.Success(Unit)
            } catch (exception: Exception) {
                Outcome.Error(RemindieDeletionException("Failed to delete remindie", exception))
            }
        }

//    suspend fun scheduleNext(): Outcome<Unit>
//    suspend fun delete(remindie: Remindie): Outcome<Unit>
//    suspend fun getRemindiesForToday(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForDay(date: LocalDateTime): Outcome<List<Remindie>>
//    suspend fun getRemindiesForCurrentWeek(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForWeek(date: LocalDateTime): Outcome<List<Remindie>>
//    suspend fun getRemindiesForCurrentMonth(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForMonth(date: LocalDateTime): Outcome<List<Remindie>>
//    suspend fun getRemindiesForCurrentYear(): Outcome<List<Remindie>>
//    suspend fun getRemindiesForYear(year: Int): Outcome<List<Remindie>>
}
