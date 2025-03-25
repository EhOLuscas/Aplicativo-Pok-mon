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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.testeapi.ui.components.FiltroTipos
import com.example.testeapi.ui.components.PokemonItem
import com.example.testeapi.ui.components.SearchBar
import com.example.testeapi.ui.viewmodel.PokemonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonViewModel = viewModel()
) {
    val query by viewModel.query.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val pagedPokemon by viewModel.pagedPokemon.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val types by viewModel.types.collectAsState()
    var selectedType by remember { mutableStateOf("") }
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF2F2F2)
    ) {
        if(loading){
            LoadingScreen()
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
            ) {
                Row(
                    modifier = Modifier.height(90.dp)
                ) {
                    SearchBar(
                        query = query,
                        onQueryChange = viewModel::onQueryChange
                    )

                    FiltroTipos(
                        items = listOf("Todos") + types,
                        selectedItem = selectedType,
                        onItemSelect = { type ->
                            selectedType = type
                            viewModel.onTypeFilterChange(if (type == "Todos") "" else type)
                        }
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        onClick = {
                            isClickable = false

                            navController.navigate("favorites")

                            coroutineScope.launch {
                                delay(500)
                                isClickable = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Favoritos")
                    }
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
                    items(pagedPokemon) { pokemon ->
                        val type = pokemon.types.firstOrNull()?.type?.name ?: "Desconhecido"
                        PokemonItem(
                            pokemon.id,
                            pokemon.name,
                            pokemon.sprites.frontDefault,
                            type,
                            onClick = {
                                navController.navigate("pokemonDetail/${pokemon.name}")
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { viewModel.previousPage() },
                        enabled = uiState.currentPage > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        )
                    ) {
                        Text("Anterior")
                    }

                    Text("Página ${uiState.currentPage + 1}")

                    Button(
                        onClick = { viewModel.nextPage() },
                        enabled = uiState.hasNextPage,
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