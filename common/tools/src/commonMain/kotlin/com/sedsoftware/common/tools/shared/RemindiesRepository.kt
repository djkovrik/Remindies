package com.sedsoftware.common.tools.shared

import com.sedsoftware.common.database.RemindieDatabase
import com.sedsoftware.common.domain.entity.Remindie
import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

class RemindiesRepository(
    private val database: RemindieDatabase
) {

    fun insert(remindie: Remindie) {
        database.remindieDatabaseQueries
            .insert(
                timestamp = remindie.timestamp,
                created = remindie.created.toString(),
                shot = remindie.shot.toString(),
                timeZone = remindie.timeZone.id,
                title = remindie.title,
                type = RemindieType.toString(remindie.type),
                period = RemindiePeriod.toString(remindie.period)
            )
    }

    fun delete(remindie: Remindie) {
        database.remindieDatabaseQueries
            .delete(remindie.id)
    }

    fun getAll(): List<Remindie> =
        database.remindieDatabaseQueries
            .selectAll { id, timestamp, created, shot, timeZone, title, type, period ->
                Remindie(
                    id = id,
                    timestamp = timestamp,
                    created = LocalDateTime.parse(created),
                    shot = LocalDateTime.parse(shot),
                    timeZone = TimeZone.of(timeZone),
                    title = title,
                    type = RemindieType.fromString(type),
                    period = RemindiePeriod.fromString(period)
                )
            }
            .executeAsList()
}
