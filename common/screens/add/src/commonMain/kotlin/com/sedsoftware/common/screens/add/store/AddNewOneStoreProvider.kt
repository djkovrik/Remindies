package com.sedsoftware.common.screens.add.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.screens.add.store.AddNewOneStore.Intent
import com.sedsoftware.common.screens.add.store.AddNewOneStore.Label
import com.sedsoftware.common.screens.add.store.AddNewOneStore.State
import com.sedsoftware.common.tools.RemindiesController
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.available
import kotlin.time.ExperimentalTime

internal class AddNewOneStoreProvider @ExperimentalTime constructor(
    private val storeFactory: StoreFactory,
    private val controller: RemindiesController
) {

    fun provide(): AddNewOneStore =
        object : AddNewOneStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddNewOneStore",
            initialState = State(),
            executorFactory = ::AddNewOneExecutor,
            reducer = AddNewOneReducer
        ) {}

    private sealed class Result {
        data class TitleEntered(val value: String) : Result()
        data class ShotTimeSelected(val value: LocalDateTime) : Result()
        data class PeriodEachChanged(val value: Int) : Result()
        data class PeriodicalSet(val value: Boolean) : Result()
        data class PeriodSelected(val value: RemindiePeriod) : Result()
        object DataUpdated : Result()
    }

    private inner class AddNewOneExecutor : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetTitle -> dispatch(Result.TitleEntered(intent.value))
                is Intent.SetShotTime -> dispatch(Result.ShotTimeSelected(intent.value))
                is Intent.SetPeriodEach -> dispatch(Result.PeriodEachChanged(intent.value))
                is Intent.SetPeriodical -> dispatch(Result.PeriodicalSet(intent.value))
                is Intent.SetPeriod -> dispatch(Result.PeriodSelected(intent.value))
                is Intent.Save -> saveToDb(getState).subscribeScoped()
            }

            if (intent !is Intent.Save) {
                dispatch(Result.DataUpdated)
            }
        }

        private fun saveToDb(getState: () -> State): Completable {
            TODO()
        }
    }

    private object AddNewOneReducer : Reducer<State, Result> {
        override fun State.reduce(result: Result): State = when (result) {
            is Result.TitleEntered -> copy(
                title = result.value,
            )
            is Result.ShotTimeSelected -> copy(
                shot = result.value,
            )
            is Result.PeriodEachChanged -> copy(
                each = if (result.value > 0) result.value else 0,
            )
            is Result.PeriodicalSet -> copy(
                periodical = result.value,
            )
            is Result.PeriodSelected -> copy(
                period = result.value,
            )
            is Result.DataUpdated -> copy(
                each = if (period != RemindiePeriod.None && each == 0) 1 else each,
                saveEnabled = title.isNotEmpty() && shot.available()
            )
        }
    }
}
