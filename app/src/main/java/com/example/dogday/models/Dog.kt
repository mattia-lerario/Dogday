package com.example.dogday
import java.util.UUID


data class Dog(
    val dogId: String = UUID.randomUUID().toString(),  // Generate a random UID
    val name: String,
    val breed: String
)