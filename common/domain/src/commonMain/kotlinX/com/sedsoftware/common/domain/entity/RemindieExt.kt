package com.sedsoftware.common.domain.entity

import com.sedsoftware.common.domain.type.RemindiePeriod
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plusPeriod
import kotlinx.datetime.toLocalDateTime

fun Remindie.toNearestShot(today: LocalDateTime = Clock.System.now().toLocalDateTime(timeZone)): Shot {

    // Not fired yet
    if (shot > today) {
        return Shot(remindie = this, planned = shot, isFired = false)
    }

    // Already fired for oneshot
    if (period == RemindiePeriod.NONE) {
        return Shot(remindie = this, planned = shot, isFired = true)
    }

    var closest: LocalDateTime = shot

    while (closest < today) {
        closest = closest.plusPeriod(period, each, timeZone)
    }

    return Shot(remindie = this, planned = closest, isFired = false)
}

fun Remindie.getShots(from: LocalDateTime, to: LocalDateTime, today: LocalDateTime): List<Shot> {
    val result = mutableListOf<Shot>()

    if (shot > to) {
        return result
    }

    var temp = shot

    while (temp <= to) {
        if (temp in from..to && temp > created) {
            result.add(
                Shot(
                    remindie = this,
                    planned = temp,
                    isFired = temp < today
                )
            )
        }

        temp = temp.plusPeriod(period, each, timeZone)
    }

    return result
}
