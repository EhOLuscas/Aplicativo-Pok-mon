package com.example.testeapi.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testeapi.ui.components.PokemonItem
import com.example.testeapi.ui.components.checkInternet
import com.example.testeapi.ui.viewmodel.PokemonViewModel

@Composable
fun PokemonFavoriteScreen(
    navController: NavController,
    viewModel: PokemonViewModel = viewModel()
) {
    val query by viewModel.query.collectAsState()
    val types by viewModel.types.collectAsState()
    var selectedType by remember { mutableStateOf("") }
    val favorites by viewModel.getFavorites().collectAsState(emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val pageSize = 8
    var currentFavoritePage by remember { mutableIntStateOf(0) }

    val pagedFavorites = favorites.chunked(pageSize)
    val hasNextPage = currentFavoritePage < pagedFavorites.lastIndex
    val hasPreviousPage = currentFavoritePage > 0

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF2F2F2)
    ) {
        if (favorites.isEmpty()) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
            ) {
                Row(
                    modifier = Modifier.height(90.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pokémon Favoritos",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 30.sp
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pagedFavorites.getOrNull(currentFavoritePage)?.let { page ->
                        items(page, key = { it.name }) { pokemon ->
                            PokemonItem(
                                pokemon.id,
                                pokemon.name,
                                pokemon.sprite,
                                pokemon.type,
                                onClick = {
                                    navController.navigate("pokemonDetail/${pokemon.name}")
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { currentFavoritePage-- },
                        enabled = hasPreviousPage,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        )
                    ) {
                        Text("Anterior")
                    }

                    Text("Página ${currentFavoritePage + 1}")

                    Button(
                        onClick = { currentFavoritePage++ },
                        enabled = hasNextPage,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        )
                    ) {
                        Text("Próxima")
                    }
                }
            }
        }
    }
}