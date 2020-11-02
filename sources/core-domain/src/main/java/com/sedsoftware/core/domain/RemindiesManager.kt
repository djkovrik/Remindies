package com.sedsoftware.core.domain

import com.sedsoftware.core.domain.entity.Remindie
import com.sedsoftware.core.domain.repository.RemindiesRepository
import com.sedsoftware.core.domain.type.Outcome
import kotlinx.datetime.LocalDateTime

interface RemindiesManager {
    val controller: AlarmController
    val repository: RemindiesRepository

    suspend fun scheduleNext(): Outcome<Unit>
    suspend fun getRemindiesForToday(): Outcome<List<Remindie>>
    suspend fun getRemindiesForDay(date: LocalDateTime): Outcome<List<Remindie>>
    suspend fun getRemindiesForCurrentWeek(): Outcome<List<Remindie>>
    suspend fun getRemindiesForWeek(date: LocalDateTime): Outcome<List<Remindie>>
    suspend fun getRemindiesForCurrentMonth(): Outcome<List<Remindie>>
    suspend fun getRemindiesForMonth(date: LocalDateTime): Outcome<List<Remindie>>
    suspend fun getRemindiesForCurrentYear(): Outcome<List<Remindie>>
    suspend fun getRemindiesForYear(year: Int): Outcome<List<Remindie>>
}
