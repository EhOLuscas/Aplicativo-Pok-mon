package com.example.testeapi.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testeapi.ui.screen.CardDetailsScreen
import com.example.testeapi.ui.screen.PokemonFavoriteScreen
import com.example.testeapi.ui.screen.PokemonListScreen
import com.example.testeapi.ui.viewmodel.PokemonViewModel

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val isConnected = remember { checkInternet(context) }
    val navController = rememberNavController()

    val startDestination = if (isConnected) "pokemonList" else "favorites"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("pokemonList") {
            PokemonListScreen(navController)
        }

        composable("favorites") {
            PokemonFavoriteScreen(navController)
        }

        composable(
            "pokemonDetail/{pokemonName}",
            arguments = listOf(
                navArgument("pokemonName") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: return@composable
            CardDetailsScreen(pokemonName)
        }
    }
}