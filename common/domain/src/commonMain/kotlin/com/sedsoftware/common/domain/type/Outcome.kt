package com.sedsoftware.common.domain.type

sealed class Outcome<out T : Any> {

    data class Success<out T : Any>(val data: T) : Outcome<T>()
    data class Error(val throwable: Throwable) : Outcome<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$throwable]"
        }
    }
}
