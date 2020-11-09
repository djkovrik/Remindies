package com.sedsoftware.common.domain

import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import com.sedsoftware.common.domain.entity.Remindie
import com.sedsoftware.common.domain.exception.RemindieDeletionException
import com.sedsoftware.common.domain.exception.RemindieInsertionException
import com.sedsoftware.common.domain.exception.RemindieSchedulingException
import com.sedsoftware.common.domain.extension.toNearestShot
import com.sedsoftware.common.domain.repository.RemindiesRepository
import com.sedsoftware.common.domain.type.Outcome
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.domain.util.AlarmManager
import com.sedsoftware.common.domain.util.RemindieTypeChecker
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

    fun add(title: String, date: LocalDateTime, period: RemindiePeriod): Single<Outcome<Unit>> =
        singleFromFunction {
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
        }
            .subscribeOn(ioScheduler)
            .map { Outcome.Success(Unit) }
            .onErrorReturn { Outcome.Error(RemindieInsertionException("Failed to insert new remindie", it)) }

    suspend fun remove(remindie: Remindie): Single<Outcome<Unit>> =
        singleFromFunction {
            val next = remindie.toNearestShot()
            manager.cancelAlarm(next)
            repository.delete(remindie)
        }
            .subscribeOn(ioScheduler)
            .map { Outcome.Success(Unit) }
            .onErrorReturn { Outcome.Error(RemindieDeletionException("Failed to delete remindie", it)) }

    suspend fun rescheduleNext(): Single<Outcome<Unit>> =
        singleFromFunction {
            val shots = repository.getAll()
                .map { it.toNearestShot() }
                .filter { !it.isFired }
                .sortedBy { it.planned }

            if (shots.isNotEmpty()) {
                val next = shots.first()
                manager.setAlarm(next)
            }
        }
            .subscribeOn(ioScheduler)
            .map { Outcome.Success(Unit) }
            .onErrorReturn { Outcome.Error(RemindieSchedulingException("Failed to reschedule remindies", it)) }

//    TODO
//    get for today
//    get for day
//    get for current week
//    get for week
//    get for current month
//    get for month
//    get for current year
//    get for year
}
