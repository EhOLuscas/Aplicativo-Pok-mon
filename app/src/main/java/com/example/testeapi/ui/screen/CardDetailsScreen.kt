package com.example.testeapi.ui.screen

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.testeapi.ui.components.borderColorByType
import com.example.testeapi.ui.viewmodel.PokemonViewModel

@Composable
fun CardDetailsScreen(pokemonName: String, viewModel: PokemonViewModel = viewModel()) {
    val pokemonDetails by viewModel.pokemonDetails.collectAsState()
    val context = LocalContext.current
    val gifEnabledLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.build()

    LaunchedEffect(pokemonName) {
        viewModel.getPokemonCardDetails(pokemonName)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF2F2F2)
    ) {
        if (pokemonDetails == null) {
            LoadingScreen()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .size(width = 600.dp, height = 350.dp)
                        .border(
                            width = 10.dp,
                            color = borderColorByType(pokemonDetails!!.types[0].type.name),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(Color(0XFFE9D3B2))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(pokemonDetails!!.sprites.other.showdown.frontDefault)
                                .crossfade(true)
                                .build(),
                            imageLoader = gifEnabledLoader,
                            contentDescription = null,
                            modifier = Modifier
                                .size(300.dp)
                                .padding(16.dp)
                        )

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = "Id: ${pokemonDetails!!.id}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Nome: ${pokemonName.replaceFirstChar { it.uppercase() }}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Altura: ${pokemonDetails!!.height}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Peso: ${pokemonDetails!!.weight}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Row {
                                Text(
                                    text = "Tipos: ",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color = borderColorByType(pokemonDetails!!.types[0].type.name)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = pokemonDetails!!.types[0].type.name.replaceFirstChar { it.uppercase() },
                                        color = Color.White,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(5.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                pokemonDetails!!.types.getOrNull(1)?.let { type ->
                                    Box(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(color = borderColorByType(type.type.name)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = type.type.name.replaceFirstChar { it.uppercase() },
                                            color = Color.White,
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(5.dp),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}