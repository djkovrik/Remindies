package com.sedsoftware.common.tools

import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import com.sedsoftware.common.database.RemindieDatabase
import com.sedsoftware.common.domain.entity.Remindie
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.domain.entity.getShots
import com.sedsoftware.common.domain.entity.toNearestShot
import com.sedsoftware.common.domain.entity.updateTimeZone
import com.sedsoftware.common.domain.exception.RemindieDeletionException
import com.sedsoftware.common.domain.exception.RemindieInsertionException
import com.sedsoftware.common.domain.exception.RemindieSchedulingException
import com.sedsoftware.common.domain.exception.ShotsFetchingException
import com.sedsoftware.common.domain.type.Outcome
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
    dependencies: Dependencies,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    private val today: LocalDateTime = Clock.System.now().toLocalDateTime(timeZone)
) {

    interface Dependencies {
        val database: RemindieDatabase
        val manager: RemindieAlarmManager
        val settings: RemindieSettings
    }

    private val repository = RemindiesRepository(dependencies.database)
    private val typeChecker = RemindieTypeChecker()
    private val manager = dependencies.manager
    private val settings = dependencies.settings

    fun add(title: String, date: LocalDateTime, period: RemindiePeriod, each: Int): Single<Outcome<Unit>> =
        singleFromFunction {
            val todayAsLong = today.toInstant(timeZone).toEpochMilliseconds()

            val new = Remindie(
                timestamp = todayAsLong,
                created = today,
                shot = date,
                timeZone = timeZone,
                title = title,
                type = typeChecker.getType(title),
                period = period,
                each = each
            )

            repository.insert(new)
        }
            .subscribeOn(ioScheduler)
            .map { Outcome.Success(Unit) }
            .onErrorReturn {
                Outcome.Error(RemindieInsertionException("Failed to insert new remindie", it))
            }

    fun remove(remindie: Remindie): Single<Outcome<Unit>> =
        singleFromFunction {
            val next = remindie.toNearestShot()
            manager.cancelAlarm(next)
            repository.delete(remindie)
        }
            .subscribeOn(ioScheduler)
            .map { Outcome.Success(Unit) }
            .onErrorReturn {
                Outcome.Error(RemindieDeletionException("Failed to delete remindie", it))
            }

    fun rescheduleNext(): Single<Outcome<Unit>> =
        singleFromFunction {
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
            .map { Outcome.Success(Unit) }
            .onErrorReturn {
                Outcome.Error(RemindieSchedulingException("Failed to reschedule remindies", it))
            }

    fun getShotsForToday(): Single<Outcome<List<Shot>>> =
        singleFromFunction {
            repository.getAll()
                .map { it.toNearestShot() }
                .filter { !it.isFired }
                .filter { it.planned.sameDayAs(today) }
                .sortedBy { it.planned }
        }
            .subscribeOn(ioScheduler)
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch today shots", it))
            }

    fun getShotsForDay(date: LocalDateTime): Single<Outcome<List<Shot>>> =
        singleFromFunction {
            repository.getAll()
                .map { it.toNearestShot() }
                .filter { !it.isFired }
                .filter { it.planned.sameDayAs(date) }
                .sortedBy { it.planned }
        }
            .subscribeOn(ioScheduler)
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch shots for $date", it))
            }

    fun getShotsForCurrentWeek(): Single<Outcome<List<Shot>>> =
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
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch current week shots", it))
            }

    fun getShotsForWeek(date: LocalDateTime): Single<Outcome<List<Shot>>> =
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
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch week shots for $date", it))
            }

    fun getShotsForCurrentMonth(): Single<Outcome<List<Shot>>> =
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
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch current month shots", it))
            }

    fun getShotsForMonth(date: LocalDateTime): Single<Outcome<List<Shot>>> =
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
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch month shots for $date", it))
            }

    fun getShotsForCurrentYear(): Single<Outcome<List<Shot>>> =
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
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch current year shots", it))
            }

    fun getShotsForYear(date: LocalDateTime): Single<Outcome<List<Shot>>> =
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
            .map { Outcome.Success(it) }
            .onErrorReturn {
                Outcome.Error(ShotsFetchingException("Failed to fetch year shots for $date", it))
            }
}
