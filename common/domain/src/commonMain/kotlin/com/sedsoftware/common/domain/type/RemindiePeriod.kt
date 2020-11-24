package com.sedsoftware.common.domain.type

sealed class RemindiePeriod(val each: Int) {
    object None : RemindiePeriod(0)
    class Hourly(each: Int) : RemindiePeriod(each)
    class Daily(each: Int) : RemindiePeriod(each)
    class Weekly(each: Int) : RemindiePeriod(each)
    class Monthly(each: Int) : RemindiePeriod(each)
    class Yearly(each: Int) : RemindiePeriod(each)

    override fun toString(): String = "${this.javaClass.simpleName} [$each]"
}
