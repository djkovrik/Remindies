package com.sedsoftware.common.screens.main

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.screens.main.MainScreenStore.Intent
import com.sedsoftware.common.screens.main.MainScreenStore.Label
import com.sedsoftware.common.screens.main.MainScreenStore.State

internal interface MainScreenStore : Store<Intent, State, Label> {

    sealed class Intent {
        object ShowAddNewOne : Intent()
        object HideAddNewOne : Intent()
        object ShowCalendar : Intent()
        object HideCalendar : Intent()
    }

    data class State(
        val addNewOneVisible: Boolean = false,
        val calendarVisible: Boolean = false,
        val emptyScreenVisible: Boolean = true,
        val schedule: List<Shot> = emptyList()
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
