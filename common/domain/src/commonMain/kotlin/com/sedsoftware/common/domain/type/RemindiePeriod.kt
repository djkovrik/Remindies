package com.sedsoftware.common.domain.type

sealed class RemindiePeriod(val each: Int) {
    object None : RemindiePeriod(0)
    class Hourly(each: Int) : RemindiePeriod(each)
    class Daily(each: Int) : RemindiePeriod(each)
    class Weekly(each: Int) : RemindiePeriod(each)
    class Monthly(each: Int) : RemindiePeriod(each)
    class Yearly(each: Int) : RemindiePeriod(each)

    companion object {
        fun toString(period: RemindiePeriod): String =
            when (period) {
                is None -> "${NONE_STR}_${period.each}"
                is Hourly -> "${HOUR_STR}_${period.each}"
                is Daily -> "${DAY_STR}_${period.each}"
                is Weekly -> "${WEEK_STR}_${period.each}"
                is Monthly -> "${MONTH_STR}_${period.each}"
                is Yearly -> "${YEAR_STR}_${period.each}"
            }

        fun fromString(str: String): RemindiePeriod {
            val marker = str.substringBefore("_")
            val each = str.substringAfter("_").toInt()

            return when (marker) {
                HOUR_STR -> Hourly(each)
                DAY_STR -> Daily(each)
                WEEK_STR -> Weekly(each)
                MONTH_STR -> Monthly(each)
                YEAR_STR -> Yearly(each)
                else -> None
            }
        }

        private const val NONE_STR = "N"
        private const val HOUR_STR = "H"
        private const val DAY_STR = "D"
        private const val WEEK_STR = "W"
        private const val MONTH_STR = "M"
        private const val YEAR_STR = "Y"
    }
}
