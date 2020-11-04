package com.sedsoftware.core.domain.util

import com.sedsoftware.core.domain.type.RemindieType

interface RemindieTypeChecker {
    fun getType(name: String): RemindieType
}
