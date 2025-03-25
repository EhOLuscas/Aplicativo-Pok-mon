package com.example.testeapi.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.testeapi.data.remote.MyApi
import com.example.testeapi.data.remote.models.PaginationState
import com.example.testeapi.data.remote.models.PokemonDetails
import com.example.testeapi.data.remote.models.PokemonResponse
import com.example.testeapi.data.remote.models.TypesResponse
import com.example.testeapi.room.AppDatabase
import com.example.testeapi.room.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

class PokemonViewModel(application: Application) : AndroidViewModel(application) {
    private val BASE_URL = "https://pokeapi.co/api/v2/"
    private val TAG = "CHECK_RESPONSE"

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _pokemonList = MutableStateFlow<List<PokemonDetails>>(emptyList())
    val pokemonList: StateFlow<List<PokemonDetails>> = _pokemonList

    private val _pagedPokemon = MutableStateFlow<List<PokemonDetails>>(emptyList())
    val pagedPokemon: StateFlow<List<PokemonDetails>> = _pagedPokemon

    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails: StateFlow<PokemonDetails?> = _pokemonDetails

    private val _uiState = MutableStateFlow(PaginationState())
    val uiState: StateFlow<PaginationState> = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _types = MutableStateFlow<List<String>>(emptyList())
    val types: StateFlow<List<String>> = _types

    private val _selectedType = MutableStateFlow("")
    val selectedType: StateFlow<String> = _selectedType

    private val _pagedFavorites = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val pagedFavorites: StateFlow<List<PokemonEntity>> = _pagedFavorites

    private val _favoriteUiState = MutableStateFlow(PaginationState())
    val favoriteUiState: StateFlow<PaginationState> = _favoriteUiState.asStateFlow()

    private val pageSize = 8

    private val db = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java, "pokemon-database"
    ).build()
    private val pokemonDao = db.pokemonDao()

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyApi::class.java)

    init {
        getAllPokemon()
        getPokemonTypes()
    }

    private fun getAllPokemon() {
        _loading.value = true

        api.getPokemon().enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val tempList = mutableListOf<PokemonDetails>()
                        val totalPokemon = body.results.size

                        body.results.forEach { pokemonUrl ->
                            api.getPokemonDetails(pokemonUrl.url).enqueue(object : Callback<PokemonDetails> {
                                override fun onResponse(call: Call<PokemonDetails>, response: Response<PokemonDetails>) {
                                    if (response.isSuccessful) {
                                        response.body()?.let { details ->
                                            tempList.add(details)

                                            if (tempList.size == totalPokemon) {
                                                Log.i(TAG, "Requisição realizada com sucesso!")
                                                val sortedList = tempList.sortedBy { it.id }
                                                _pokemonList.value = sortedList

                                                updatePagedPokemon(0)

                                                _loading.value = false
                                            }
                                        }
                                    }
                                }

                                override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {
                                    Log.i(TAG, "Erro ao buscar detalhes: ${t.message}")
                                    _loading.value = false
                                }
                            })
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Log.i(TAG, "Erro na requisição: ${t.message}")
                _loading.value = false
            }
        })
    }

    fun getPokemonCardDetails(pokemonName: String) {
        api.getCardDetails(pokemonName).enqueue(object : Callback<PokemonDetails> {
            override fun onResponse(call: Call<PokemonDetails>, response: Response<PokemonDetails>) {
                if (response.isSuccessful) {
                    response.body()?.let { cardDetails ->
                        Log.i(TAG, "Requisição realizada com sucesso! Detalhes do Pokémon buscados!")
                        _pokemonDetails.value = cardDetails
                    }
                }
            }

            override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {
                Log.i(TAG, "Erro ao buscar detalhes: ${t.message}")
            }
        })
    }

    private fun getPokemonTypes(){
        api.getPokemonType().enqueue(object : Callback<TypesResponse>{
            override fun onResponse(call: Call<TypesResponse>, response: Response<TypesResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { typesResponse ->
                        Log.i(TAG, "Requisição realizada com sucesso! Tipos do Pokémon bs=uscados!")
                        _types.value = typesResponse.results.map { it.name }
                    }
                }
            }

            override fun onFailure(call: Call<TypesResponse>, t: Throwable) {
                Log.i(TAG, "Erro ao buscar tipos: ${t.message}")
            }

        })
    }

    fun onTypeFilterChange(type: String) {
        _selectedType.value = type
        updatePagedPokemon(0)
    }

    private fun updatePagedPokemon(page: Int) {
        val filteredList = if (_selectedType.value.isEmpty()) {
            _pokemonList.value
        } else {
            _pokemonList.value.filter { pokemon ->
                pokemon.types.any { it.type.name.equals(_selectedType.value, ignoreCase = true) }
            }
        }

        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, filteredList.size)
        _pagedPokemon.value = filteredList.subList(startIndex, endIndex)

        _uiState.value = _uiState.value.copy(
            currentPage = page,
            hasNextPage = endIndex < filteredList.size
        )
    }

    fun nextPage() {
        if (_uiState.value.hasNextPage) {
            updatePagedPokemon(_uiState.value.currentPage + 1)
        }
    }

    fun previousPage() {
        if (_uiState.value.currentPage > 0) {
            updatePagedPokemon(_uiState.value.currentPage - 1)
        }
    }

    private fun onSearch(query: String) {
        _query.value = query
        if (query.isEmpty()) {
            updatePagedPokemon(0)
        } else {
            val filteredList = _pokemonList.value.filter { pokemon ->
                pokemon.name.contains(query, ignoreCase = true)
            }
            _pagedPokemon.value = filteredList
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
        onSearch(query)
    }

    fun addPokemon(pokemon: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonDao.insert(pokemon)
        }
    }

    fun getFavorites(): Flow<List<PokemonEntity>> {
        return pokemonDao.getFavorites()
    }

    fun removePokemon(pokemon: PokemonEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonDao.delete(pokemon)
        }
    }

    fun isPokemonSaved(name: String): Flow<Boolean> {
        return pokemonDao.isPokemonSaved(name)
    }

    fun printFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonDao.getFavorites().collect { favorites ->
                Log.d("PokemonDB", "Favoritos: $favorites")
            }
        }
    }
}