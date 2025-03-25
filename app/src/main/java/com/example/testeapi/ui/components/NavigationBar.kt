package com.example.testeapi.ui.components

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Composable
fun SetNavigationBarColor(color: Color) {
    val activity = LocalActivity.current

    SideEffect {
        if (activity != null) {
            activity.window?.navigationBarColor = color.toArgb()
        }
    }
}