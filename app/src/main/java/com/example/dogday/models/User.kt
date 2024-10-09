package com.example.dogday

import com.example.dogday.models.Dog

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val dogs: Map<String, Dog> = emptyMap()
)
