package com.sedsoftware.common.screens.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.sedsoftware.common.database.RemindieDatabase
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.domain.exception.TodayShotsFetchingException
import com.sedsoftware.common.screens.main.MainScreenStore.Intent
import com.sedsoftware.common.screens.main.MainScreenStore.Label
import com.sedsoftware.common.screens.main.MainScreenStore.State
import com.sedsoftware.common.tools.RemindiesController
import com.sedsoftware.common.tools.base.RemindieAlarmManager
import com.sedsoftware.common.tools.base.RemindieSettings
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal class MainScreenStoreProvider(
    private val storeFactory: StoreFactory,
    database: RemindieDatabase,
    manager: RemindieAlarmManager,
    settings: RemindieSettings
) {

    private val controller = RemindiesController(database, manager, settings)

    fun provide(): MainScreenStore =
        object : MainScreenStore, Store<Intent, State, Label> by storeFactory.create(
            name = "MainScreenStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper<Action>(Action.LoadShots),
            executorFactory = ::MainScreenExecutor,
            reducer = MainScreenReducer
        ) {}

    private sealed class Action {
        object LoadShots : Action()
    }

    private sealed class Result {
        data class ShotsAvailable(val shots: List<Shot>) : Result()
        object AddNewOneDisplayed : Result()
        object AddNewOneHidden : Result()
        object CalendarDisplayed : Result()
        object CalendarHidden : Result()
    }

    private inner class MainScreenExecutor : ReaktiveExecutor<Intent, Action, State, Result, Label>() {
        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.LoadShots -> {
                    controller.getShotsForToday()
                        .observeOn(mainScheduler)
                        .subscribeScoped(
                            onSuccess = { dispatch(Result.ShotsAvailable(it)) },
                            onError = { publish(Label.ErrorCaught(TodayShotsFetchingException(it))) }
                        )
                }
            }
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ShowAddNewOne -> dispatch(Result.AddNewOneDisplayed)
                is Intent.HideAddNewOne -> dispatch(Result.AddNewOneHidden)
                is Intent.ShowCalendar -> dispatch(Result.CalendarDisplayed)
                is Intent.HideCalendar -> dispatch(Result.CalendarHidden)
            }
        }
    }

    private object MainScreenReducer : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.ShotsAvailable -> copy(
                emptyScreenVisible = result.shots.isNotEmpty(),
                schedule = result.shots
            )
            is Result.AddNewOneDisplayed -> copy(
                addNewOneVisible = true
            )
            is Result.AddNewOneHidden -> copy(
                addNewOneVisible = false
            )
            is Result.CalendarDisplayed -> copy(
                calendarVisible = true
            )
            is Result.CalendarHidden -> copy(
                calendarVisible = false
            )
        }
    }
}
