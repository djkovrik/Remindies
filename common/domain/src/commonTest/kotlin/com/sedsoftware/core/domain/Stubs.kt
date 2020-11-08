package com.sedsoftware.core.domain

import com.sedsoftware.core.domain.entity.Remindie
import com.sedsoftware.core.domain.type.RemindiePeriod
import com.sedsoftware.core.domain.type.RemindieType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

object Stubs {

    // 5.11.2020 20:55 - Thursday
    private val today: LocalDateTime = LocalDateTime(2020, 11, 5, 10, 20)
    private val timeZone: TimeZone = TimeZone.currentSystemDefault()

    val remindiesInOwnTimeZone: List<Remindie> = listOf(
        Remindie(
            id = 1,
            created = today,
            shot = LocalDateTime(2020, 11, 6, 15, 35),
            timeZone = timeZone,
            title = "Oneshot - tomorrow",
            type = RemindieType.AIRPORT,
            period = RemindiePeriod.None
        ),

        Remindie(
            id = 2,
            created = today,
            shot = LocalDateTime(2020, 11, 5, 12, 0),
            timeZone = timeZone,
            title = "Each 3 hours from today starting at 12:00",
            type = RemindieType.CALL,
            period = RemindiePeriod.Hourly(3)
        ),

        Remindie(
            id = 3,
            created = today,
            shot = LocalDateTime(2020, 11, 6, 8, 0),
            timeZone = timeZone,
            title = "Daily - from tomorrow at 8:00",
            type = RemindieType.CAFE,
            period = RemindiePeriod.Daily(1)
        ),

        Remindie(
            id = 4,
            created = today,
            shot = LocalDateTime(2020, 11, 6, 11, 22),
            timeZone = timeZone,
            title = "Daily - from tomorrow each 3 days at 11:22",
            type = RemindieType.CALL,
            period = RemindiePeriod.Daily(3)
        ),

        Remindie(
            id = 5,
            created = today,
            shot = LocalDateTime(2020, 11, 7, 21, 30),
            timeZone = timeZone,
            title = "Each week from Saturday at 21:30",
            type = RemindieType.GYM,
            period = RemindiePeriod.Weekly(1)
        ),

        Remindie(
            id = 6,
            created = today,
            shot = LocalDateTime(2020, 11, 8, 16, 0),
            timeZone = timeZone,
            title = "Each two weeks from Sunday at 16:00",
            type = RemindieType.DOCTOR,
            period = RemindiePeriod.Weekly(2)
        ),

        Remindie(
            id = 7,
            created = today,
            shot = LocalDateTime(2020, 11, 8, 18, 0),
            timeZone = timeZone,
            title = "Pay rent each month at 18:00",
            type = RemindieType.PAY,
            period = RemindiePeriod.Monthly(1)
        ),

        Remindie(
            id = 8,
            created = today,
            shot = LocalDateTime(2020, 12, 31, 23, 0),
            timeZone = timeZone,
            title = "Congratulations each New Year night ^-^ at 23:00",
            type = RemindieType.CALL,
            period = RemindiePeriod.Yearly(1)
        ),
    )
}
