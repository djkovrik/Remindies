package com.sedsoftware.common.domain.entity

import com.sedsoftware.common.domain.type.RemindiePeriod
import com.sedsoftware.common.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.getDayEnd
import kotlinx.datetime.getDayStart
import kotlin.test.Test
import kotlin.test.assertEquals

class RemindieExtTest {

    @Test
    fun `toNearestShot test`() {
        val timeZone = TimeZone.currentSystemDefault()

        // Oneshot - not fired
        val today1 = LocalDateTime(2020, 11, 7, 13, 13)
        val created1 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot1 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie1 = Remindie(
            id = 1,
            created = created1,
            shot = shot1,
            timeZone = timeZone,
            title = "Oneshot - not fired",
            type = RemindieType.CALL,
            period = RemindiePeriod.None
        )

        assertEquals(
            remindie1.toNearestShot(today1),
            Shot(
                remindie = remindie1,
                planned = shot1,
                isFired = false
            )
        )

        // Oneshot - fired
        val today2 = LocalDateTime(2020, 11, 7, 22, 13)
        val created2 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot2 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie2 = Remindie(
            id = 2,
            created = created2,
            shot = shot2,
            timeZone = timeZone,
            title = "Oneshot - fired",
            type = RemindieType.CALL,
            period = RemindiePeriod.None
        )

        assertEquals(
            remindie2.toNearestShot(today2),
            Shot(
                remindie = remindie2,
                planned = shot2,
                isFired = true
            )
        )

        // Periodical
        val today3 = LocalDateTime(2020, 11, 7, 22, 13)
        val created3 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot3 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie3 = Remindie(
            id = 3,
            created = created3,
            shot = shot3,
            timeZone = timeZone,
            title = "Oneshot - hourly 3",
            type = RemindieType.CALL,
            period = RemindiePeriod.Hourly(3)
        )

        assertEquals(
            remindie3.toNearestShot(today3),
            Shot(
                remindie = remindie3,
                planned = LocalDateTime(2020, 11, 8, 0, 33),
                isFired = false
            )
        )

        val today4 = LocalDateTime(2020, 11, 7, 22, 13)
        val created4 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot4 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie4 = Remindie(
            id = 4,
            created = created4,
            shot = shot4,
            timeZone = timeZone,
            title = "Oneshot - daily 2",
            type = RemindieType.CALL,
            period = RemindiePeriod.Daily(2)
        )

        assertEquals(
            remindie4.toNearestShot(today4),
            Shot(
                remindie = remindie4,
                planned = LocalDateTime(2020, 11, 9, 18, 33),
                isFired = false
            )
        )

        val today5 = LocalDateTime(2020, 11, 7, 22, 13)
        val created5 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot5 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie5 = Remindie(
            id = 5,
            created = created5,
            shot = shot5,
            timeZone = timeZone,
            title = "Oneshot - weekly 2",
            type = RemindieType.CALL,
            period = RemindiePeriod.Weekly(2)
        )

        assertEquals(
            remindie5.toNearestShot(today5),
            Shot(
                remindie = remindie5,
                planned = LocalDateTime(2020, 11, 21, 18, 33),
                isFired = false
            )
        )

        val today6 = LocalDateTime(2020, 11, 7, 22, 13)
        val created6 = LocalDateTime(2020, 11, 7, 12, 13)
        val shot6 = LocalDateTime(2020, 11, 7, 18, 33)

        val remindie6 = Remindie(
            id = 6,
            created = created6,
            shot = shot6,
            timeZone = timeZone,
            title = "Oneshot - monthly 2",
            type = RemindieType.CALL,
            period = RemindiePeriod.Monthly(14)
        )

        assertEquals(
            remindie6.toNearestShot(today6),
            Shot(
                remindie = remindie6,
                planned = LocalDateTime(2022, 1, 7, 18, 33),
                isFired = false
            )
        )
    }

    @Test
    fun `getShots test`() {
        val timeZone = TimeZone.currentSystemDefault()
        val created = LocalDateTime(2020, 11, 22, 10, 0)
        val today = LocalDateTime(2020, 11, 23, 18, 0)

        val remindie1 = Remindie(
            id = 1,
            created = created,
            shot = LocalDateTime(2020, 11, 23, 22, 0),
            timeZone = timeZone,
            title = "Daily at 22:00",
            type = RemindieType.CALL,
            period = RemindiePeriod.Daily(1)
        )

        val remindie2 = Remindie(
            id = 2,
            created = created,
            shot = LocalDateTime(2020, 10, 27, 1, 2),
            timeZone = timeZone,
            title = "Weekly at 12:13",
            type = RemindieType.CALL,
            period = RemindiePeriod.Weekly(1)
        )

        val remindie3 = Remindie(
            id = 3,
            created = created,
            shot = LocalDateTime(2020, 12, 11, 23, 34),
            timeZone = timeZone,
            title = "Monthly at 23:34",
            type = RemindieType.CALL,
            period = RemindiePeriod.Monthly(1)
        )

        // test daily
        val shots1 = remindie1.getShots(
            from = LocalDateTime(2020, 11, 20, 22, 0).getDayStart(),
            to = LocalDateTime(2020, 11, 29, 22, 0).getDayEnd(),
            today = today
        )

        assertEquals(shots1.size, 7)

        // test weekly
        val shots2 = remindie2.getShots(
            from = LocalDateTime(2020, 11, 20, 22, 0).getDayStart(),
            to = LocalDateTime(2020, 12, 6, 22, 0).getDayEnd(),
            today = today
        )

        assertEquals(shots2.size, 2)

        // test monthly
        val shots3 = remindie3.getShots(
            from = LocalDateTime(2020, 11, 20, 22, 0).getDayStart(),
            to = LocalDateTime(2021, 3, 30, 12, 12).getDayEnd(),
            today = today
        )

        assertEquals(shots3.size, 4)
    }
}
