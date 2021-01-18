package com.sedsoftware.common.domain.exception

class CurrentWeekShotsFetchingException(cause: Throwable) : Exception("Failed to fetch current week shots", cause)
class CurrentMonthShotsFetchingException(cause: Throwable) : Exception("Failed to fetch current month shots", cause)
class CurrentYearShotsFetchingException(cause: Throwable) : Exception("Failed to fetch current year shots", cause)
class TodayShotsFetchingException(cause: Throwable) : Exception("Failed to fetch today shots", cause)
