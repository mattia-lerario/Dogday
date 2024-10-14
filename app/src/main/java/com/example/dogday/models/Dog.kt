package com.example.dogday
import java.util.Date
import java.util.UUID


data class Dog(
    val dogId: String = UUID.randomUUID().toString(),  // Generate a random UID
    val name: String,
    val breed: String,
    val vetLog: VetLog? = null
)

data class VetLog(
    val dog: Dog,
    val vetName: String?,
    val date: String?,
    val notes: String,
)