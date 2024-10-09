package com.example.dogday.models
import java.util.UUID


data class Dog(
    val dogId: String = UUID.randomUUID().toString(),  // Generate a random UID
    val name: String,
    val nickName: String = "",
    val breed: String,
    val birthday: Long,
    val breeder: String = ""
)