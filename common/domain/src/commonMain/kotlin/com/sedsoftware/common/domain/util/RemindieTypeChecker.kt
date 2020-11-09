package com.sedsoftware.common.domain.util

import com.sedsoftware.common.domain.type.RemindieType

interface RemindieTypeChecker {
    fun getType(name: String): RemindieType
}
