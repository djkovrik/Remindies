package com.sedsoftware.common.screens.calendar

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.sedsoftware.common.database.RemindieDatabase
import com.sedsoftware.common.domain.Component
import com.sedsoftware.common.tools.base.RemindieAlarmManager
import com.sedsoftware.common.tools.base.RemindieSettings

interface ShowCalendar : Component {

    interface Dependencies {
        val storeFactory: StoreFactory
        val database: RemindieDatabase
        val manager: RemindieAlarmManager
        val settings: RemindieSettings
    }
}
