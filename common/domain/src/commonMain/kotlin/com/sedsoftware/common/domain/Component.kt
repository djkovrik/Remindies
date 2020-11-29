package com.sedsoftware.common.domain

import androidx.compose.runtime.Composable

fun interface Component {

    @Composable
    operator fun invoke()
}
