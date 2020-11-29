package com.sedsoftware.common.tools.shared

import com.sedsoftware.common.domain.entity.Shot
import com.sedsoftware.common.tools.base.RemindieAlarmManager

// TODO ("Desktop implementation")
@Suppress("FunctionName")
fun RemindiesAlarmManager(): RemindieAlarmManager = object : RemindieAlarmManager {
    override fun setAlarm(shot: Shot) = Unit
    override fun cancelAlarm(shot: Shot) = Unit
}
