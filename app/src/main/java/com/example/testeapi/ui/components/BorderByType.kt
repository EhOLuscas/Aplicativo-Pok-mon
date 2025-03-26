package com.example.testeapi.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun borderColorByType(type: String): Color{
    return when (type.lowercase()) {
        "fire" -> Color(0xFFFFA756)
        "water" -> Color(0xFF58ABF6)
        "grass" -> Color(0xFF8BBE8A)
        "electric" -> Color(0xFFFAE078)
        "psychic" -> Color(0xFFFA92B2)
        "ice" -> Color(0xFF91D8DF)
        "dragon" -> Color(0xFF7383B9)
        "dark" -> Color(0xFF6F6E78)
        "fairy" -> Color(0xFFEDA3E1)
        "normal" -> Color(0xFFB5B9C4)
        "bug" -> Color(0xFF9AC12F)
        "fighting" -> Color(0xFFCE416B)
        "rock" -> Color(0xFFC5B78C)
        "ground" -> Color(0xFFD97845)
        "poison" -> Color(0xFFB667CF)
        "ghost" -> Color(0xFF5269AD)
        "flying" -> Color(0XFF89AAE3)
        "steel" -> Color(0XFF5A8EA2)
        else -> Color.LightGray
    }
}