package com.sedsoftware.core.domain.type

sealed class ReminderPeriod(val each: Int) {
    object None : ReminderPeriod(0)
    class Hourly(each: Int) : ReminderPeriod(each)
    class Daily(each: Int) : ReminderPeriod(each)
    class Weekly(each: Int) : ReminderPeriod(each)
    class Monthly(each: Int) : ReminderPeriod(each)
    class Yearly(each: Int) : ReminderPeriod(each)

    override fun toString(): String = "${this.javaClass.simpleName} [$each]"
}
