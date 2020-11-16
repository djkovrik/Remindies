package com.sedsoftware.common.domain.entity

import kotlinx.datetime.TimeZone
import kotlinx.datetime.moveToZone
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun Shot.updateTimeZone(current: TimeZone = TimeZone.currentSystemDefault()): Shot {

    if (remindie.timeZone == current) {
        return this
    }

    return copy(planned = this.planned.moveToZone(remindie.timeZone, current))
}
