package com.sedsoftware.common.screens.calendar.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.sedsoftware.common.database.RemindieDatabase
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.domain.exception.CurrentMonthShotsFetchingException
import com.sedsoftware.common.domain.exception.CurrentWeekShotsFetchingException
import com.sedsoftware.common.domain.exception.CurrentYearShotsFetchingException
import com.sedsoftware.common.domain.type.RemindieCalendarMode
import com.sedsoftware.common.screens.calendar.store.ShowCalendarStore.Intent
import com.sedsoftware.common.screens.calendar.store.ShowCalendarStore.Label
import com.sedsoftware.common.screens.calendar.store.ShowCalendarStore.State
import com.sedsoftware.common.tools.RemindiesController
import com.sedsoftware.common.tools.base.RemindieAlarmManager
import com.sedsoftware.common.tools.base.RemindieSettings
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal class ShowCalendarStoreProvider(
    private val storeFactory: StoreFactory,
    database: RemindieDatabase,
    manager: RemindieAlarmManager,
    settings: RemindieSettings
) {

    private val controller = RemindiesController(database, manager, settings)

    fun provide(): ShowCalendarStore =
        object : ShowCalendarStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ShowCalendarStore",
            initialState = State(),
            executorFactory = ::ShowCalendarExecutor,
            reducer = ShowCalendarReducer
        ) {}

    private sealed class Result {
        data class DateChosen(val date: LocalDateTime) : Result()
        data class ModeChosen(val mode: RemindieCalendarMode) : Result()
        data class ShotsAvailable(val shots: List<Shot>) : Result()
    }

    private inner class ShowCalendarExecutor : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ShowCurrentWeek ->
                    controller.getShotsForCurrentWeek()
                        .observeOn(mainScheduler)
                        .subscribeScoped(
                            onSuccess = { dispatch(Result.ShotsAvailable(it)) },
                            onError = { publish(Label.ErrorCaught(CurrentWeekShotsFetchingException(it))) }
                        )
                is Intent.ShowCurrentMonth ->
                    controller.getShotsForCurrentMonth()
                        .observeOn(mainScheduler)
                        .subscribeScoped(
                            onSuccess = { dispatch(Result.ShotsAvailable(it)) },
                            onError = { publish(Label.ErrorCaught(CurrentMonthShotsFetchingException(it))) }
                        )
                is Intent.ShowCurrentYear ->
                    controller.getShotsForCurrentYear()
                        .observeOn(mainScheduler)
                        .subscribeScoped(
                            onSuccess = { dispatch(Result.ShotsAvailable(it)) },
                            onError = { publish(Label.ErrorCaught(CurrentYearShotsFetchingException(it))) }
                        )
            }
        }
    }

    private object ShowCalendarReducer : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.DateChosen -> copy(
                date = result.date
            )
            is Result.ModeChosen -> copy(
                mode = result.mode
            )
            is Result.ShotsAvailable -> copy(
                schedule = result.shots
            )
        }
    }
}
