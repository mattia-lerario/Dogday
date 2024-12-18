package com.example.dogday.models
import java.util.UUID


data class Dog(
    val dogId: String = UUID.randomUUID().toString(),
    val name: String,
    val nickName: String = "",
    val breed: String,
    val birthday: Long,
    val breeder: String = "",
    val imageUrl: String? = null,
    val vetLog: List<VetNote> = emptyList()

)