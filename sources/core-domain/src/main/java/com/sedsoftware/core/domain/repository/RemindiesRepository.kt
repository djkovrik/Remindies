package com.sedsoftware.core.domain.repository

import com.sedsoftware.core.domain.entity.Remindie
import kotlinx.datetime.LocalDateTime

interface RemindiesRepository {
    suspend fun add(remindie: Remindie)
    suspend fun delete(remindie: Remindie)
    suspend fun next(): Remindie
    suspend fun getForDay(date: LocalDateTime): List<Remindie>
    suspend fun getForWeek(date: LocalDateTime): List<Remindie>
    suspend fun getForMonth(date: LocalDateTime): List<Remindie>
    suspend fun getForYear(date: LocalDateTime): List<Remindie>
}
