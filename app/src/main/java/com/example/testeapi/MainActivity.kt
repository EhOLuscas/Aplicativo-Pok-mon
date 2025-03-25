package com.example.testeapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.testeapi.room.AppDatabase
import com.example.testeapi.ui.components.AppNavigation
import com.example.testeapi.ui.components.SetNavigationBarColor
import com.example.testeapi.ui.theme.TesteApiTheme
import com.example.testeapi.ui.viewmodel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TesteApiTheme {
                AppNavigation()
                SetNavigationBarColor(Color.White)
            }
        }
    }
}

@Composable
fun Greeting() {
    Text(
        text = "Hello Lucas!"
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}