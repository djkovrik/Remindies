package com.sedsoftware.common.domain.entity

import kotlinx.datetime.TimeZone
import kotlinx.datetime.moveToZone

fun Shot.updateTimeZone(current: TimeZone): Shot {

    if (remindie.timeZone == current) {
        return this
    }

    return copy(planned = this.planned.moveToZone(remindie.timeZone, current))
}
