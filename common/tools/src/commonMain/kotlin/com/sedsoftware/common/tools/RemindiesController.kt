package com.sedsoftware.common.tools

import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import com.sedsoftware.common.database.RemindieDatabase
import com.sedsoftware.common.domain.entity.Remindie
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.domain.entity.getShots
import com.sedsoftware.common.domain.entity.toNearestShot
import com.sedsoftware.common.domain.entity.updateTimeZone
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.tools.base.RemindieAlarmManager
import com.sedsoftware.common.tools.base.RemindieSettings
import com.sedsoftware.common.tools.shared.RemindieTypeChecker
import com.sedsoftware.common.tools.shared.RemindiesRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.getMonthEnd
import kotlinx.datetime.getMonthStart
import kotlinx.datetime.getWeekEnd
import kotlinx.datetime.getWeekStart
import kotlinx.datetime.getYearEnd
import kotlinx.datetime.getYearStart
import kotlinx.datetime.sameDayAs
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@ExperimentalTime
class RemindiesController(
    private val database: RemindieDatabase,
    private val manager: RemindieAlarmManager,
    private val settings: RemindieSettings,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    private val today: LocalDateTime = Clock.System.now().toLocalDateTime(timeZone)
) {

    private val repository = RemindiesRepository(database)
    private val typeChecker = RemindieTypeChecker()

    fun add(title: String, shot: LocalDateTime, period: RemindiePeriod, each: Int): Completable =
        completableFromFunction {
            val todayAsLong = today.toInstant(timeZone).toEpochMilliseconds()

            val new = Remindie(
                timestamp = todayAsLong,
                created = today,
                shot = shot,
                timeZone = timeZone,
                title = title,
                type = typeChecker.getType(title),
                period = period,
                each = each
            )

            repository.insert(new)
        }
            .subscribeOn(ioScheduler)

    fun remove(remindie: Remindie): Completable =
        completableFromFunction {
            val next = remindie.toNearestShot()
            manager.cancelAlarm(next)
            repository.delete(remindie)
        }
            .subscribeOn(ioScheduler)

    fun rescheduleNext(): Completable =
        completableFromFunction {
            val shots = repository.getAll()
                .map { it.toNearestShot() }
                .filter { !it.isFired }
                .sortedBy { it.planned }

            if (shots.isNotEmpty()) {
                val next = if (settings.timeZoneDependent) {
                    shots.first().updateTimeZone(timeZone)
                } else {
                    shots.first()
                }

                manager.setAlarm(next)
            }
        }
            .subscribeOn(ioScheduler)

    fun getShotsForToday(): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .map { it.toNearestShot() }
                .filter { !it.isFired }
                .filter { it.planned.sameDayAs(today) }
                .sortedBy { it.planned }
        }
            .subscribeOn(ioScheduler)

    fun getShotsForDay(date: LocalDateTime): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .map { it.toNearestShot() }
                .filter { !it.isFired }
                .filter { it.planned.sameDayAs(date) }
                .sortedBy { it.planned }
        }
            .subscribeOn(ioScheduler)

    fun getShotsForCurrentWeek(): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .fold(mutableListOf<Shot>(), { acc, remindie ->
                    acc.apply {
                        addAll(
                            remindie.getShots(
                                from = today.getWeekStart(settings.startFromSunday),
                                to = today.getWeekEnd(settings.startFromSunday),
                                today = today
                            )
                        )
                    }
                })
                .sortedBy { it.planned }

        }
            .subscribeOn(ioScheduler)

    fun getShotsForWeek(date: LocalDateTime): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .fold(mutableListOf<Shot>(), { acc, remindie ->
                    acc.apply {
                        addAll(
                            remindie.getShots(
                                from = date.getWeekStart(settings.startFromSunday),
                                to = date.getWeekEnd(settings.startFromSunday),
                                today = today
                            )
                        )
                    }
                })
                .sortedBy { it.planned }

        }
            .subscribeOn(ioScheduler)

    fun getShotsForCurrentMonth(): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .fold(mutableListOf<Shot>(), { acc, remindie ->
                    acc.apply {
                        addAll(
                            remindie.getShots(
                                from = today.getMonthStart(),
                                to = today.getMonthEnd(),
                                today = today
                            )
                        )
                    }
                })
                .sortedBy { it.planned }

        }
            .subscribeOn(ioScheduler)

    fun getShotsForMonth(date: LocalDateTime): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .fold(mutableListOf<Shot>(), { acc, remindie ->
                    acc.apply {
                        addAll(
                            remindie.getShots(
                                from = date.getMonthStart(),
                                to = date.getMonthEnd(),
                                today = today
                            )
                        )
                    }
                })
                .sortedBy { it.planned }

        }
            .subscribeOn(ioScheduler)

    fun getShotsForCurrentYear(): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .fold(mutableListOf<Shot>(), { acc, remindie ->
                    acc.apply {
                        addAll(
                            remindie.getShots(
                                from = today.getYearStart(),
                                to = today.getYearEnd(),
                                today = today
                            )
                        )
                    }
                })
                .sortedBy { it.planned }

        }
            .subscribeOn(ioScheduler)

    fun getShotsForYear(date: LocalDateTime): Single<List<Shot>> =
        singleFromFunction {
            repository.getAll()
                .fold(mutableListOf<Shot>(), { acc, remindie ->
                    acc.apply {
                        addAll(
                            remindie.getShots(
                                from = date.getYearStart(),
                                to = date.getYearEnd(),
                                today = today
                            )
                        )
                    }
                })
                .sortedBy { it.planned }

        }
            .subscribeOn(ioScheduler)
}
