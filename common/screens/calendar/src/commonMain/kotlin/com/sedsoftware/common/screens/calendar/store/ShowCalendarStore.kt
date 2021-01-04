package com.sedsoftware.common.screens.calendar.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.domain.type.RemindieCalendarMode
import com.sedsoftware.common.screens.calendar.store.ShowCalendarStore.Intent
import com.sedsoftware.common.screens.calendar.store.ShowCalendarStore.Label
import com.sedsoftware.common.screens.calendar.store.ShowCalendarStore.State
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface ShowCalendarStore : Store<Intent, State, Label> {

    sealed class Intent {
        object ShowCurrentWeek : Intent()
        object ShowCurrentMonth : Intent()
        object ShowCurrentYear : Intent()
    }

    data class State(
        val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        val mode: RemindieCalendarMode = RemindieCalendarMode.WEEK,
        val schedule: List<Shot> = emptyList()
    )

    sealed class Label {
        data class ErrorCaught(val throwable: Throwable) : Label()
    }
}
