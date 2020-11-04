package com.sedsoftware.core.domain.helper

import com.sedsoftware.core.domain.type.RemindieType

interface RemindieTypeChecker {
    fun getType(name: String): RemindieType
}
