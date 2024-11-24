package com.example.dogday.models

import com.google.firebase.firestore.GeoPoint

data class Breeder(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0, 0.0),
    val ownerName: String = "",
    val contactInfo: String = "",
    val description: String = "",
    val dogBreeds: ArrayList<String> = arrayListOf(),
    val imageUrl: String = "",
    val rating: Double = 0.0,                 // New Field
    val userRatingsTotal: Int = 0             // New Field
)



