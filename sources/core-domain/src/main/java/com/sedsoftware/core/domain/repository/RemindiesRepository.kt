package com.sedsoftware.core.domain.repository

import com.sedsoftware.core.domain.entity.Remindie
import com.sedsoftware.core.domain.type.RemindiePeriod
import kotlinx.datetime.LocalDateTime

interface RemindiesRepository {
    suspend fun addOneShoot(title: String, shoot: LocalDateTime)
    suspend fun addPeriodical(title: String, period: RemindiePeriod)
    suspend fun delete(remindie: Remindie)
    suspend fun next(): Remindie
    suspend fun getForDay(date: LocalDateTime): List<Remindie>
    suspend fun getForWeek(date: LocalDateTime): List<Remindie>
    suspend fun getForMonth(date: LocalDateTime): List<Remindie>
    suspend fun getForYear(date: LocalDateTime): List<Remindie>
}
