package com.sedsoftware.core.domain.entity

import kotlinx.datetime.Month

data class RemindiesMonth(
    val month: Month,
    val schedule: Map<Int, List<Remindie>>
)
