package com.sedsoftware.core.domain.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

interface TimeDataProvider {
    val timeZone: TimeZone
    val today: LocalDateTime
}
