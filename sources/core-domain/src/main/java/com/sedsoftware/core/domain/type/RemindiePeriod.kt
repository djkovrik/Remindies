package com.sedsoftware.core.domain.type

sealed class RemindiePeriod(val each: Long) {
    object None : RemindiePeriod(0L)
    class Hourly(each: Long) : RemindiePeriod(each)
    class Daily(each: Long) : RemindiePeriod(each)
    class Weekly(each: Long) : RemindiePeriod(each)
    class Monthly(each: Long) : RemindiePeriod(each)
    class Yearly(each: Long) : RemindiePeriod(each)

    override fun toString(): String = "${this.javaClass.simpleName} [$each]"
}
