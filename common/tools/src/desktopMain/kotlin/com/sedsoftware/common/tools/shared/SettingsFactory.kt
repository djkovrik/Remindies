package com.sedsoftware.common.tools.shared

import com.sedsoftware.common.tools.base.RemindieSettings

// TODO ("Desktop implementation")
@Suppress("FunctionName")
fun RemindiesSettings(): RemindieSettings = object : RemindieSettings {

    private var internalTimeZoneDependent: Boolean = false
    private var internalStartFromSunday: Boolean = false

    override var timeZoneDependent: Boolean
        get() = internalTimeZoneDependent
        set(value) {
            internalTimeZoneDependent = value
        }

    override var startFromSunday: Boolean
        get() = internalStartFromSunday
        set(value) {
            internalStartFromSunday = value
        }
}
