package com.sedsoftware.common.tools.shared

import com.sedsoftware.common.domain.type.RemindieType

class RemindieTypeChecker {

    private val types = mapOf(
        RemindieType.BARBER to setOf("барбер", "barber"),
        RemindieType.DENTIST to setOf("стоматолог", "dentist"),
        RemindieType.DOCTOR to setOf("врач", "doctor"),
        RemindieType.AIRPORT to setOf("аэропорт", "вылет", "airport", "flight"),
        RemindieType.CAFE to setOf("кафе", "cafe"),
        RemindieType.GYM to setOf("тренировка", "фитнес", "training", "fitness", "gym"),
        RemindieType.RESTAURANT to setOf("ресторан", "restaurant"),
        RemindieType.BUY to setOf("купить", "buy"),
        RemindieType.CALL to setOf("позвонить", "звонок", "call"),
        RemindieType.MEET to setOf("встреча", "встретить", "meet", "meeting"),
        RemindieType.PAY to setOf("заплатить", "оплатить", "pay"),
        RemindieType.TALK to setOf("поговорить", "talk"),
        RemindieType.MESSAGE to setOf("написать", "write", "message"),
    )


    fun getType(name: String): RemindieType {
        name
            .split(" ")
            .map { it.trim(',', '.', '!', '?', ':', ';', '#', ' ').toLowerCase() }
            .forEach { word ->
                types.forEach { (type, variants) ->
                    if (variants.contains(word)) return type
                }
            }

        return RemindieType.UNKNOWN
    }
}
