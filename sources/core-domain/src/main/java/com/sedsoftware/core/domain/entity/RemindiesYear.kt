package com.sedsoftware.core.domain.entity

import kotlinx.datetime.Month

data class RemindiesYear(
    val year: Int,
    val schedule: Map<Month, RemindiesMonth>
)
