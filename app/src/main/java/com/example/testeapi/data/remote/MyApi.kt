package com.example.testeapi.data.remote

import com.example.testeapi.data.remote.models.PokemonDetails
import com.example.testeapi.data.remote.models.PokemonResponse
import com.example.testeapi.data.remote.models.TypesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface MyApi {
    @GET("pokemon?limit=151&offset=0")
    fun getPokemon(): Call<PokemonResponse>

    @GET
    fun getPokemonDetails(@Url url: String): Call<PokemonDetails>

    @GET("pokemon/{name}")
    fun getCardDetails(@Path("name") name: String): Call<PokemonDetails>

    @GET("type")
    fun getPokemonType(): Call<TypesResponse>
}