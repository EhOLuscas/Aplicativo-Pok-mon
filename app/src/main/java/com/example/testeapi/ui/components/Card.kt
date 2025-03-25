package com.example.testeapi.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.testeapi.R
import com.example.testeapi.data.remote.models.Pokemon
import com.example.testeapi.data.remote.models.PokemonDetails
import com.example.testeapi.room.PokemonEntity
import com.example.testeapi.ui.viewmodel.PokemonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PokemonItem(
    id: Int,
    name: String,
    sprite: String,
    type: String,
    onClick: () -> Unit,
    viewModel: PokemonViewModel = viewModel()
){
    val isLiked by viewModel.isPokemonSaved(name).collectAsState(false)
    val animateColor: Color by animateColorAsState(if (isLiked) Color.Red else Color.Black)
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(
                width = 10.dp,
                color = borderColorByType(type),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = isClickable) {
                isClickable = false

                onClick()

                coroutineScope.launch {
                    delay(500)
                    isClickable = true
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(Color.LightGray)
    ) {
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = R.drawable.png_pokebola,
                        contentDescription = null,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .fillMaxSize()
                    )
                    AsyncImage(
                        model = sprite,
                        contentDescription = null,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .padding(8.dp)
                    )
                }
                Text(
                    text = name.replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            IconButton(
                onClick = {
                    val pokemonEntity = PokemonEntity(
                        id = id,
                        name = name,
                        type = type,
                        sprite = sprite
                    )

                    if (isLiked) {
                        viewModel.removePokemon(pokemonEntity)
                    } else {
                        viewModel.addPokemon(pokemonEntity)
                    }

                    viewModel.printFavorites()
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(64.dp)
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = animateColor
                )
            }
        }
    }
}