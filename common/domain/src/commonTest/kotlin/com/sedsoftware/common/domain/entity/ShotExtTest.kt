package com.sedsoftware.common.domain.entity

import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class ShotExtTest {

    @Test
    fun `updateTimeZone test`() {
        val currentTimeZone = TimeZone.of("GMT+2")
        val newTimeZone = TimeZone.of("GMT+3")

        val today = LocalDateTime(2020, 11, 7, 22, 13)
        val created = LocalDateTime(2020, 11, 7, 12, 13)
        val shot = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie = Remindie(
            id = 1234,
            created = today,
            shot = shot,
            timeZone = currentTimeZone,
            title = "Daily at 18:33, next shot at 8/11/2020 at 18:33",
            type = RemindieType.CAFE,
            period = RemindiePeriod.Daily(1)
        )

        val initialShot = remindie.toNearestShot(today = today)
        val newTimeZoneShot = remindie.toNearestShot(today = today).updateTimeZone(newTimeZone)

        // initial
        assertEquals(initialShot.planned.hour, 18)
        assertEquals(initialShot.planned.minute, 33)

        // updated
        assertEquals(newTimeZoneShot.planned.hour, 19)
        assertEquals(newTimeZoneShot.planned.minute, 33)
    }
}