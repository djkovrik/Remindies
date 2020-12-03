package com.sedsoftware.common.screens.add.store

import com.arkivanov.mvikotlin.core.store.Store
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.screens.add.store.AddNewOneStore.Intent
import com.sedsoftware.common.screens.add.store.AddNewOneStore.Label
import com.sedsoftware.common.screens.add.store.AddNewOneStore.State
import kotlinx.datetime.LocalDateTime

internal interface AddNewOneStore : Store<Intent, State, Label> {

    sealed class Intent {
        data class SetTitle(val value: String) : Intent()
        data class SetShotTime(val value: LocalDateTime) : Intent()
        data class SetPeriodEach(val value: Int) : Intent()
        data class SetPeriodical(val value: Boolean) : Intent()
        data class SetPeriod(val value: RemindiePeriod) : Intent()
        object Save : Intent()
    }

    data class State(
        val title: String = "",
        val shot: LocalDateTime? = null,
        val periodical: Boolean = false,
        val saveEnabled: Boolean = false,
        val period: RemindiePeriod = RemindiePeriod.NONE,
        val each: Int = 0
    )

    sealed class Label {
        data class ErrorCaught(val exception: Exception) : Label()
    }
}
