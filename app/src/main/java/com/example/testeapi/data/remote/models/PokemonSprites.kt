package com.example.testeapi.data.remote.models

import com.google.gson.annotations.SerializedName

data class PokemonSprites(
    @SerializedName("front_default")
    val frontDefault: String,
    val other: OtherSprites
)

data class OtherSprites(
    val showdown: ShowdownSprites
)

data class ShowdownSprites(
    @SerializedName("front_default")
    val frontDefault: String
)