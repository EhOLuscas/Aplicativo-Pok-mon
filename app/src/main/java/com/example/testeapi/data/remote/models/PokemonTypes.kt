package com.example.testeapi.data.remote.models

data class TypesResponse(
    val results: List<Type>
)

data class TypeSlot(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String
)