package com.sedsoftware.core.domain.extension

import com.sedsoftware.core.domain.entity.Remindie
import com.sedsoftware.core.domain.entity.Shot
import com.sedsoftware.core.domain.type.RemindiePeriod
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun Remindie.toNearestShot(today: LocalDateTime = Clock.System.now().toLocalDateTime(timeZone)): Shot {

    // Not fired yet
    if (shot > today) {
        return Shot(remindie = this, planned = shot, isFired = false)
    }

    // Already fired for oneshot
    if (period == RemindiePeriod.None) {
        return Shot(remindie = this, planned = shot, isFired = true)
    }

    var closest: LocalDateTime = shot

    while (closest < today) {
        closest = closest.plusPeriod(period, timeZone)
    }

    return Shot(remindie = this, planned = closest, isFired = false)
}
