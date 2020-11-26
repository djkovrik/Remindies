package com.sedsoftware.desktop

import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers

import kotlinx.coroutines.Dispatchers

fun main() {
    overrideSchedulers(main = Dispatchers.Main::asScheduler)

    Window("Todo") {
        Surface(modifier = Modifier.fillMaxSize()) {
            MaterialTheme {
                DesktopTheme {
                    Greeting("Desktop")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
