package com.sedsoftware.core.domain.entity

import kotlinx.datetime.LocalDateTime

data class Shot(
    val remindie: Remindie,
    val planned: LocalDateTime,
    val isFired: Boolean
)
