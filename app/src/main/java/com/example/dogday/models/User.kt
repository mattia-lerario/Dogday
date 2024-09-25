package com.example.dogday

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val dogs: Map<String, Dog> = emptyMap()
)
