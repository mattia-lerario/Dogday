package com.example.dogday

import com.example.dogday.models.Dog

data class User(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val birthday: Long = 0L,
    val dogs: Map<String, Dog> = emptyMap()
) {
    // No-argument constructor is automatically generated because of default values
}