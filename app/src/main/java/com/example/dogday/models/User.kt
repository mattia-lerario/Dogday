package com.example.dogday

import com.example.dogday.models.Dog

data class User(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val birthday: Long = 0L,
    val dogs: Map<String, Dog> = emptyMap() // Assuming dogs is a map of Dog objects
) {
    // No-argument constructor is automatically generated because of default values
}