package com.sedsoftware.common.screens.add

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.sedsoftware.common.database.RemindieDatabase
import com.sedsoftware.common.domain.Component
import com.sedsoftware.common.tools.base.RemindieAlarmManager
import com.sedsoftware.common.tools.base.RemindieSettings

interface AddNewRemindie : Component {

    interface Dependencies {
        val storeFactory: StoreFactory
        val database: RemindieDatabase
        val manager: RemindieAlarmManager
        val settings: RemindieSettings
        val addNewOutput: Consumer<Output>
    }

    sealed class Output {
        object Added : Output()
    }
}
