package com.sedsoftware.common.domain.type

enum class RemindieType {

    // Appointments
    BARBER,
    DENTIST,
    DOCTOR,

    // Places
    AIRPORT,
    CAFE,
    GYM,
    RESTAURANT,

    // Actions
    BUY,
    CALL,
    MEET,
    PAY,
    TALK,
    MESSAGE,

    // No type
    UNKNOWN;

    companion object {
        fun toString(type: RemindieType): String = type.name
        fun fromString(name: String): RemindieType = values().find { it.name == name } ?: UNKNOWN
    }
}
